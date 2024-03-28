package org.pacs.accesscontrolapi.models.policymodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EnvironmentModel {
    @NotNull
    private LocalTime currentTime;
    @NotBlank
    private String currentDayOfWeek;
    @NotNull
    private Boolean emergencyStatus;
}
