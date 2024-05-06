package org.pacs.accesscontrolapi.services;

import lombok.Getter;
import org.pacs.accesscontrolapi.models.policymodels.EmergencyStatus;
import org.pacs.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Service
public class PolicyInformationPoint {

    private final EnvironmentModel environmentModel;

    public PolicyInformationPoint() {
        this.environmentModel = new EnvironmentModel(
                LocalTime.now(),
                LocalDateTime.now().getDayOfWeek().toString().substring(0,3),
                EmergencyStatus.NO_EMERGENCY);
    }

    public void setEnvironmentEmergencyStatus(EmergencyStatus emergencyStatus) {
        environmentModel.setEmergencyStatus(emergencyStatus);
    }
}
