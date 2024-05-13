package org.pacs.accesscontrolapi.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.pacs.accesscontrolapi.models.policymodels.*;
import org.pacs.accesscontrolapi.models.requestmodels.AccessPointModel;
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
    private final NonceService nonceService;

    public AccessResponseModel evaluateEmployeeAccessRequest(@Valid EmployeeAccessRequestModel requestModel, String userId, String nonce) {
        // Check if nonces are matching
        Boolean isValidNonce = nonceService.verifyNonce(userId, nonce);
        if (!isValidNonce) {
            return new AccessResponseModel(false);
        }

        System.out.println(isValidNonce);

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());

        // Extract user and access point policies
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Fetch user attributes from request and evaluate
        EmployeeModel employeeModel = requestModel.getEmployeeModel();
        boolean isSatisfiedUserPolicy = evaluateEmployeePolicy(employeeModel, userPolicyModelSet);
        boolean isSatisfiedAccessPoint = evaluateAccessPointPolicy(accessPointModel, accessPointPolicyModel);
        boolean isSatisfiedEnvironment = evaluateEnvironmentConditions(employeeModel);

        EmergencyStatus emergencyStatus = pip.getEnvironmentModel().getEmergencyStatus();
        AccessResponseModel accessResponseModel;

        if(emergencyStatus.equals(EmergencyStatus.NO_EMERGENCY)) {
            // Evaluate flags
            Boolean decision = isSatisfiedUserPolicy && isSatisfiedAccessPoint && isSatisfiedEnvironment;

            // Update access point attributes
            apiService.updateLiveAccessPointAttributesById(accessPointModel.getLocation(), accessPointModel); // updates access point attributes

            // Create access response model
            accessResponseModel = new AccessResponseModel(decision);

            // Log access attempt
            accessLogService.logEmployeeAccess(requestModel, accessPolicyModel, accessResponseModel);

        } else if(emergencyStatus.equals(EmergencyStatus.EMERGENCY_CLOSED)) {
            accessResponseModel = new AccessResponseModel(false);

        } else if(emergencyStatus.equals(EmergencyStatus.EMERGENCY_OPENED)) {
            accessResponseModel = new AccessResponseModel(true);

        } else {
            accessResponseModel = new AccessResponseModel(false);
        }

        // Return access decision
        return accessResponseModel;
    }

    public AccessResponseModel evaluateVisitorAccessRequest(@Valid VisitorAccessRequestModel requestModel, String userId, String nonce) {
        // Check if nonces are matching
        Boolean isValidNonce = nonceService.verifyNonce(userId, nonce);
        if (!isValidNonce) {
            return new AccessResponseModel(false);
        }

        System.out.println(isValidNonce);

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        System.out.println(accessPointModel);

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());

        System.out.println(accessPolicyModel);

        // Extract user and access point policies
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Fetch user attributes from request and evaluate
        VisitorModel visitorModel = requestModel.getVisitorModel();
        boolean isSatisfiedUserPolicy = evaluateVisitorPolicy(visitorModel, userPolicyModelSet);
        boolean isSatisfiedAccessPoint = evaluateAccessPointPolicy(accessPointModel, accessPointPolicyModel);
        boolean isSatisfiedEnvironment = evaluateEnvironmentConditions(visitorModel);


        System.out.println(isSatisfiedUserPolicy);
        System.out.println(isSatisfiedAccessPoint);
        System.out.println(isSatisfiedEnvironment);

        EmergencyStatus emergencyStatus = pip.getEnvironmentModel().getEmergencyStatus();

        System.out.println(emergencyStatus);

        AccessResponseModel accessResponseModel;

        if(emergencyStatus.equals(EmergencyStatus.NO_EMERGENCY)) {
            // Evaluate flags
            Boolean decision = isSatisfiedUserPolicy && isSatisfiedAccessPoint && isSatisfiedEnvironment;

            // Update access point attributes
            apiService.updateLiveAccessPointAttributesById(accessPointModel.getLocation(), accessPointModel); // updates access point attributes

            // Create access response model
            accessResponseModel = new AccessResponseModel(decision);

            // Log access attempt
            accessLogService.logVisitorAccess(requestModel, accessPolicyModel, accessResponseModel);

        } else if(emergencyStatus.equals(EmergencyStatus.EMERGENCY_CLOSED)) {
            accessResponseModel = new AccessResponseModel(false);

        } else if(emergencyStatus.equals(EmergencyStatus.EMERGENCY_OPENED)) {
            accessResponseModel = new AccessResponseModel(true);

        } else {
            accessResponseModel = new AccessResponseModel(false);
        }

        // Return access decision
        return accessResponseModel;
    }

    private Boolean evaluateEmployeePolicy(EmployeeModel employeeModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equalsIgnoreCase(employeeModel.getDepartment()))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(employeeModel.getRole()))
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
                .anyMatch(accessPointPolicy -> accessPointPolicy.getMaxOccupancyLevel() >= accessPointModel.getOccupancyLevel())
                && accessPointModel.getIsTampered().equals(false);
    }

    private Boolean evaluateEnvironmentConditions(UserModel userModel) {
        LocalTime startTime = userModel.getTimeSchedule().getStartTime();
        LocalTime endTime = userModel.getTimeSchedule().getEndTime();
        Set<String> daysOfWeek = userModel.getTimeSchedule().getDaysOfWeek();
        EnvironmentModel environmentAttributesModel = pip.getEnvironmentModel();

        return environmentAttributesModel.getCurrentTime().isAfter(startTime) &&
                environmentAttributesModel.getCurrentTime().isBefore(endTime) &&
                daysOfWeek.stream().anyMatch(day -> day.substring(0,3).equalsIgnoreCase(environmentAttributesModel.getCurrentDayOfWeek().substring(0,3)));
    }
}
