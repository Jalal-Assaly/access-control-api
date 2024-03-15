package org.example.accesscontrolapi.services;

import org.example.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class PolicyInformationPoint {
    public EnvironmentModel getEnvironmentAttributes() {
        return new EnvironmentModel(
                LocalTime.now(),
                LocalDateTime.now().getDayOfWeek().toString(),
                false
        );
    }
}
