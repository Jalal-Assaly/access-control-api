package org.example.accesscontrolapi;

import org.example.accesscontrolapi.models.policymodels.AccessPointPolicyModel;
import org.example.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.example.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.example.accesscontrolapi.models.policymodels.UserPolicyModel;
import org.example.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeModel;
import org.example.accesscontrolapi.services.ExternalApiService;
import org.example.accesscontrolapi.services.PolicyDecisionPoint;
import org.example.accesscontrolapi.services.PolicyInformationPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PolicyDecisionPointTest {

    @Mock
    private PolicyInformationPoint pip;
    @Mock
    private ExternalApiService apiService;
    @InjectMocks
    private PolicyDecisionPoint pdp;

    Set<UserPolicyModel> userPolicyModelSet;
    AccessPointPolicyModel accessPointPolicyModel;
    AccessPolicyModel accessPolicyModel;
    EnvironmentModel environmentModel;
    @BeforeEach
    void init() {
        // Policy Models
        userPolicyModelSet = Set.of(
                new UserPolicyModel(
                        "HR",
                        List.of("Manager", "Assistant", "Recruiter"),
                        5,
                        List.of("Level 3", "Level 4", "Level 5"),
                        List.of("Full time", "Part time")
                ),
                new UserPolicyModel(
                        "R&D",
                        List.of("Manager", "Researcher", "Product Manager", "Tech lead"),
                        3,
                        List.of("Level 2", "Level 3", "Level 4", "Level 5"),
                        List.of("Full time", "Part time", "Collaborator")
                )
        );

        accessPointPolicyModel = new AccessPointPolicyModel(
                "C311",
                50
        );

        accessPolicyModel = new AccessPolicyModel("1", userPolicyModelSet, accessPointPolicyModel);

        environmentModel = new EnvironmentModel(
                LocalTime.now(),
                LocalDate.now().getDayOfWeek().toString(),
                false);
    }

    @Test
    void evaluateAccessRequest_Success() {
        // Request Models
        EmployeeModel employeeModel = new EmployeeModel(
                "23",
                "Manager",
                "HR",
                new TimeSchedule(
                        LocalTime.of(0, 0),
                        LocalTime.of(23, 59),
                        Set.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")),
                7,
                "Level 3",
                "Full time"
        );
        AccessPointModel accessPointModel = new AccessPointModel(
                "2",
                "C311",
                false,
                30
        );
        EmployeeAccessRequestModel employeeAccessRequestModel = new EmployeeAccessRequestModel(employeeModel, accessPointModel);

        when(apiService.fetchAccessPolicyByLocation(
                employeeAccessRequestModel
                        .getAccessPointModel()
                        .getLocation())
        ).thenReturn(accessPolicyModel);
        when(pip.getEnvironmentAttributes()).thenReturn(environmentModel);

        Boolean hasAccess = pdp.evaluateAccessRequest(employeeAccessRequestModel);

        verify(apiService).fetchAccessPolicyByLocation(any(String.class));
        verify(pip).getEnvironmentAttributes();
        assertThat(hasAccess).isTrue();
    }

    @Test
    void evaluateAccessRequest_AllConditionsNotMet_Failed() {
        // Request Models
        EmployeeModel employeeModel = new EmployeeModel(
                "23",
                "Technician",
                "IT",
                new TimeSchedule(
                        LocalTime.of(3, 0),
                        LocalTime.of(4, 0),
                        Set.of("Wednesday")),
                0,
                "Level 1",
                "Unemployed"
        );

        AccessPointModel accessPointModel = new AccessPointModel(
                "2",
                "C311",
                true,
                1000
        );

        EmployeeAccessRequestModel employeeAccessRequestModel = new EmployeeAccessRequestModel(employeeModel, accessPointModel);

        when(apiService.fetchAccessPolicyByLocation(
                employeeAccessRequestModel
                        .getAccessPointModel()
                        .getLocation())
        ).thenReturn(accessPolicyModel);
        when(pip.getEnvironmentAttributes()).thenReturn(environmentModel);

        Boolean hasAccess = pdp.evaluateAccessRequest(employeeAccessRequestModel);

        verify(apiService).fetchAccessPolicyByLocation(any(String.class));
        verify(pip).getEnvironmentAttributes();
        assertThat(hasAccess).isFalse();
    }

    @Test
    void evaluateAccessRequest_AccessPolicyNotFound_Failed() {
        // Request Models
        EmployeeModel employeeModel = new EmployeeModel(
                "23",
                "Manager",
                "HR",
                new TimeSchedule(
                        LocalTime.of(0, 0),
                        LocalTime.of(23, 59),
                        Set.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")),
                7,
                "Level 3",
                "Full time"
        );
        AccessPointModel accessPointModel = new AccessPointModel(
                "2",
                "D414", // ID not existing in policy
                false,
                30
        );

        EmployeeAccessRequestModel employeeAccessRequestModel = new EmployeeAccessRequestModel(employeeModel, accessPointModel);

        when(apiService.fetchAccessPolicyByLocation(
                employeeAccessRequestModel
                        .getAccessPointModel()
                        .getLocation())
        ).thenThrow(WebClientResponseException.class);

        // Act and Assert
        assertThatThrownBy(() -> pdp.evaluateAccessRequest(employeeAccessRequestModel))
                .isInstanceOf(WebClientResponseException.class)
                .hasMessage(null);
    }
}
