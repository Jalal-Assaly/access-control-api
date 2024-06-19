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

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());

        // Extract user and access point policies
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Fetch user attributes from request and evaluate
        EmployeeModel employeeModel = requestModel.getEmployeeModel();

        System.out.println(employeeModel);

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

        // Fetch access point attributes from request
        AccessPointModel accessPointModel = requestModel.getAccessPointModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointModel.getLocation());

        // Extract user and access point policies
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Fetch user attributes from request and evaluate
        VisitorModel visitorModel = requestModel.getVisitorModel();

        boolean isSatisfiedUserPolicy = evaluateVisitorPolicy(visitorModel, userPolicyModelSet);
        boolean isSatisfiedAccessPoint = evaluateAccessPointPolicy(accessPointModel, accessPointPolicyModel);
        boolean isSatisfiedEnvironment = evaluateEnvironmentConditions(visitorModel);

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
                .peek(userPolicy -> System.out.println("Department filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(employeeModel.getRole()))
                .peek(userPolicy -> System.out.println("Role filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(employeeModel.getClearanceLevel()))
                .peek(userPolicy -> System.out.println("Clearance level filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedEmploymentStatus().contains(employeeModel.getEmploymentStatus()))
                .peek(userPolicy -> System.out.println("Employment status filter passed"))
                .findAny()
                .isPresent();
    }

    private Boolean evaluateVisitorPolicy(VisitorModel visitorModel, Set<UserPolicyModel> userPolicyModelSet) {
        return userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equalsIgnoreCase(visitorModel.getDepartment()))
                .peek(userPolicy -> System.out.println("Department filter passed"))
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
                .filter(accessPointPolicy -> accessPointPolicy.getMaxOccupancyLevel() >= accessPointModel.getOccupancyLevel())
                .peek(accessPointPolicy -> System.out.println("Occupancy level filter passed"))
                .filter(accessPointPolicy -> accessPointModel.getIsTampered().equals(false))
                .peek(accessPointPolicy -> System.out.println("Tampered detection passed"))
                .findAny()
                .isPresent();
    }

    private Boolean evaluateEnvironmentConditions(UserModel userModel) {
        LocalTime startTime = userModel.getTimeSchedule().getStartTime();
        LocalTime endTime = userModel.getTimeSchedule().getEndTime();
        Set<String> daysOfWeek = userModel.getTimeSchedule().getDaysOfWeek();

        EnvironmentModel environmentAttributesModel = pip.getEnvironmentModel();

        // Update current time in attributes
        environmentAttributesModel.setCurrentTime(LocalTime.now());
        LocalTime currentTime = environmentAttributesModel.getCurrentTime();
        String currentDayOfWeek = environmentAttributesModel.getCurrentDayOfWeek().substring(0, 3);

        boolean isTimeWithinRange;
        if (startTime.isBefore(endTime)) {
            // Time schedule is within the same day
            isTimeWithinRange = !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
        } else {
            // Time schedule spans across midnight
            isTimeWithinRange = !currentTime.isBefore(startTime) || !currentTime.isAfter(endTime);
        }

        boolean isDayOfWeekMatch = daysOfWeek.stream()
                .anyMatch(day -> day.substring(0, 3).equalsIgnoreCase(currentDayOfWeek));

        boolean decision = isTimeWithinRange && isDayOfWeekMatch;

        if(decision) {
            System.out.println("Time schedule filter passed");
        }

        return decision;
    }
}
