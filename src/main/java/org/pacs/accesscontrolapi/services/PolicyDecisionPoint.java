package org.pacs.accesscontrolapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.pacs.accesscontrolapi.models.policymodels.AccessPointPolicyModel;
import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.pacs.accesscontrolapi.models.policymodels.UserPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.pacs.accesscontrolapi.models.requestmodels.UserModel;
import org.pacs.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.pacs.accesscontrolapi.models.requestmodels.employeemodels.EmployeeModel;
import org.pacs.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.pacs.accesscontrolapi.models.requestmodels.visitormodels.VisitorModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Validated
@RequiredArgsConstructor
public class PolicyDecisionPoint {

    private final PolicyInformationPoint pip;
    private final AccessLogService accessLogService;
    private final ExternalApiService apiService;

    public AccessResponseModel evaluateAccessRequest(@Valid AccessRequestModel requestModel) {

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());

        // Extract user and access point policies
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Define access flags
        boolean isSatisfiedUserPolicy = false;
        boolean isSatisfiedAccessPoint = false;
        boolean isSatisfiedEnvironment = false;

        // Fetch user attributes from request and evaluate
        if (requestModel instanceof EmployeeAccessRequestModel employeeRequest) {
            EmployeeModel employeeModel = employeeRequest.getEmployeeModel();
            isSatisfiedUserPolicy = evaluateEmployeePolicy(employeeModel, userPolicyModelSet);
            isSatisfiedAccessPoint = evaluateAccessPointPolicy(accessPointModel, accessPointPolicyModel);
            isSatisfiedEnvironment = evaluateEnvironmentConditions(employeeModel);
        } else if (requestModel instanceof VisitorAccessRequestModel visitorRequest) {
            VisitorModel visitorModel = visitorRequest.getVisitorModel();
            isSatisfiedUserPolicy = evaluateVisitorPolicy(visitorModel, userPolicyModelSet);
            isSatisfiedAccessPoint = evaluateAccessPointPolicy(accessPointModel, accessPointPolicyModel);
            isSatisfiedEnvironment = evaluateEnvironmentConditions(visitorModel);
        }

        // Evaluate flags
        Boolean decision = isSatisfiedUserPolicy && isSatisfiedAccessPoint && isSatisfiedEnvironment;

        // Create access response model
        AccessResponseModel accessResponseModel = new AccessResponseModel(decision);

        // Log access attempt
        accessLogService.logAccess(requestModel, accessPolicyModel, accessResponseModel);

        // Return access decision
        return accessResponseModel;
    }

    private Boolean evaluateEmployeePolicy(EmployeeModel employeeModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equalsIgnoreCase(employeeModel.getDepartment()))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(employeeModel.getRole()))
                .filter(userPolicy -> userPolicy.getMinimumYearsOfExperience() <= employeeModel.getYearsOfExperience())
                .filter(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(employeeModel.getClearanceLevel()))
                .anyMatch(userPolicy -> userPolicy.getAllowedEmploymentStatus().contains(employeeModel.getEmploymentStatus()));
    }

    private Boolean evaluateVisitorPolicy(VisitorModel visitorModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equalsIgnoreCase(visitorModel.getDepartment()))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(visitorModel.getRole()))
                .anyMatch(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(visitorModel.getClearanceLevel()));
    }

    private Boolean evaluateAccessPointPolicy(AccessPointModel accessPointModel, AccessPointPolicyModel accessPointPolicyModel) {
        return Stream.of(accessPointPolicyModel)
                .filter(accessPointPolicy -> accessPointPolicy.getLocation().equalsIgnoreCase(accessPointModel.getLocation()))
                .anyMatch(accessPointPolicy -> accessPointPolicy.getOccupancyLevel() >= accessPointModel.getOccupancyLevel())
                && accessPointModel.getIsTampered().equals(false);
    }

    private Boolean evaluateEnvironmentConditions(UserModel userModel) {
        LocalTime startTime = userModel.getTimeSchedule().getStartTime();
        LocalTime endTime = userModel.getTimeSchedule().getEndTime();
        Set<String> daysOfWeek = userModel.getTimeSchedule().getDaysOfWeek();
        EnvironmentModel environmentAttributesModel = pip.getEnvironmentAttributes();

        return environmentAttributesModel.getCurrentTime().isAfter(startTime) &&
                environmentAttributesModel.getCurrentTime().isBefore(endTime) &&
                daysOfWeek.stream().anyMatch(day -> day.equalsIgnoreCase(environmentAttributesModel.getCurrentDayOfWeek()));
    }
}
