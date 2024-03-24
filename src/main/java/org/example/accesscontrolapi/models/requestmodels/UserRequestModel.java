package org.example.accesscontrolapi.models.requestmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestModel {
    @NotBlank
    private String id;
    @NotBlank
    private String role;
    @NotBlank
    private String department;
    @NotNull
    private TimeSchedule timeSchedule;
    @NotNull
    private Integer yearsOfExperience;
    @NotBlank
    private String clearanceLevel;
    @NotBlank
    private String employmentStatus;
}