package org.example.accesscontrolapi.services;

import lombok.RequiredArgsConstructor;
import org.example.accesscontrolapi.models.policymodels.AccessPointPolicyModel;
import org.example.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.example.accesscontrolapi.models.policymodels.UserPolicyModel;
import org.example.accesscontrolapi.models.requestmodels.AccessPointRequestModel;
import org.example.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.example.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.example.accesscontrolapi.models.requestmodels.UserRequestModel;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PolicyDecisionPoint {

    private final PolicyInformationPoint pip;
    private final ExternalApiService apiService;

    public Boolean evaluateAccessRequest(AccessRequestModel requestModel) {

        // Fetch all request models
        UserRequestModel userRequestModel = requestModel.getUserRequestModel();
        AccessPointRequestModel accessPointRequestModel = requestModel.getAccessPointRequestModel();

        // Fetch all policy models
        AccessPolicyModel accessPolicyModel = apiService.fetchAccessPolicyByLocation(accessPointRequestModel.getLocation());
        Set<UserPolicyModel> userPolicyModelSet = accessPolicyModel.getUserAttributesSet();
        AccessPointPolicyModel accessPointPolicyModel = accessPolicyModel.getAccessPointAttributes();

        // Fetch environment attributes
        EnvironmentModel environmentAttributesModel = pip.getEnvironmentAttributes();

        // Evaluate user policy against user request
        Boolean isSatisfiedUserPolicy = userPolicyModelSet.stream()
                .filter(userPolicy -> userPolicy.getDepartment().equals(userRequestModel.getDepartment()))
                .peek(userPolicy -> System.out.println("Department filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedRoles().contains(userRequestModel.getRole()))
                .peek(userPolicy -> System.out.println("Role filter passed"))
                .filter(userPolicy -> userPolicy.getMinimumYearsOfExperience() <= userRequestModel.getYearsOfExperience())
                .peek(userPolicy -> System.out.println("Years of experience filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedClearanceLevels().contains(userRequestModel.getClearanceLevel()))
                .peek(userPolicy -> System.out.println("Clearance level filter passed"))
                .filter(userPolicy -> userPolicy.getAllowedEmploymentStatus().contains(userRequestModel.getEmploymentStatus()))
                .peek(userPolicy -> System.out.println("Employment status filter passed"))
                .findAny()
                .isPresent();

        // Evaluate access point policy against access point request
        Boolean isSatisfiedAccessPoint = Stream.of(accessPointPolicyModel)
                .filter(accessPointPolicy -> accessPointPolicy.getLocation().equals(accessPointRequestModel.getLocation()))
                .peek(accessPointPolicy -> System.out.println("Location filter passed"))
                .filter(accessPointPolicy -> accessPointPolicy.getOccupancyLevel() >= accessPointRequestModel.getOccupancyLevel())
                .peek(accessPointPolicy -> System.out.println("Occupancy level filter passed"))
                .findAny()
                .isPresent();


        LocalTime startTime = userRequestModel.getTimeSchedule().getStartTime();
        LocalTime endTime = userRequestModel.getTimeSchedule().getEndTime();
        Set<String> daysOfWeek = userRequestModel.getTimeSchedule().getDaysOfWeek();

        Boolean isSatisfiedEnvironment = environmentAttributesModel.getCurrentTime().isAfter(startTime) &&
                environmentAttributesModel.getCurrentTime().isBefore(endTime) &&
                daysOfWeek.stream().anyMatch(day -> day.equalsIgnoreCase(environmentAttributesModel.getCurrentDayOfWeek()));

        return isSatisfiedUserPolicy && isSatisfiedAccessPoint && isSatisfiedEnvironment;
    }
}
