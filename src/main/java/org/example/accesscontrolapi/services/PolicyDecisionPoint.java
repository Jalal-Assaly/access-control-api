package org.example.accesscontrolapi.services;

import lombok.RequiredArgsConstructor;
import org.example.accesscontrolapi.exceptionhandler.customexceptions.AttributesMismatchException;
import org.example.accesscontrolapi.models.policymodels.AccessPointPolicyModel;
import org.example.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.example.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.example.accesscontrolapi.models.policymodels.UserPolicyModel;
import org.example.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.example.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.UserModel;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeModel;
import org.example.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.visitormodels.VisitorModel;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PolicyDecisionPoint {

    private final PolicyInformationPoint pip;
    private final ExternalApiService apiService;

    public Boolean evaluateAccessRequest(AccessRequestModel requestModel) {

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        Boolean isSatisfiedUserPolicy = false;
        Boolean isSatisfiedAccessPoint = false;
        Boolean isSatisfiedEnvironment = false;

        try {
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
        } catch (NullPointerException exception) {
            throw new AttributesMismatchException("Some attributes are missing in request or access policy");
        }
        return isSatisfiedUserPolicy && isSatisfiedAccessPoint && isSatisfiedEnvironment;
    }

    private Boolean evaluateEmployeePolicy(EmployeeModel employeeModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equalsIgnoreCase(employeeModel.getDepartment()))
                .peek(userPolicy -> System.out.println("Department filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(employeeModel.getRole()))
                .peek(userPolicy -> System.out.println("Role filter passed"))
                .filter(userPolicy -> userPolicy.getMinimumYearsOfExperience() <= employeeModel.getYearsOfExperience())
                .peek(userPolicy -> System.out.println("Years of experience filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(employeeModel.getClearanceLevel()))
                .peek(userPolicy -> System.out.println("Clearance level filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedEmploymentStatus().contains(employeeModel.getEmploymentStatus()))
                .peek(userPolicy -> System.out.println("Employment status filter passed"))
                .findAny()
                .isPresent();
    }

    private Boolean evaluateVisitorPolicy(VisitorModel visitorModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(visitorModel.getRole()))
                .peek(userPolicy -> System.out.println("Role filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(visitorModel.getClearanceLevel()))
                .peek(userPolicy -> System.out.println("Clearance level filter passed"))
                .findAny()
                .isPresent();
    }

    private Boolean evaluateAccessPointPolicy(AccessPointModel accessPointModel, AccessPointPolicyModel accessPointPolicyModel) {
        return Stream.of(accessPointPolicyModel)
                .filter(accessPointPolicy -> accessPointPolicy.getLocation().equalsIgnoreCase(accessPointModel.getLocation()))
                .peek(accessPointPolicy -> System.out.println("Location filter passed"))
                .filter(accessPointPolicy -> accessPointPolicy.getOccupancyLevel() >= accessPointModel.getOccupancyLevel())
                .peek(accessPointPolicy -> System.out.println("Occupancy level filter passed"))
                .findAny()
                .isPresent() && accessPointModel.getIsTampered().equals(false);
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
