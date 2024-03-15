package org.example.accesscontrolapi.models.policymodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EnvironmentModel {
    private LocalTime currentTime;
    private String currentDayOfWeek;
    private Boolean emergencyStatus;
}
