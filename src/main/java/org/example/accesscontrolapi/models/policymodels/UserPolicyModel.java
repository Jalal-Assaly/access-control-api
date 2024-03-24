package org.example.accesscontrolapi.models.policymodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPolicyModel {
    @NotBlank
    private String department;
    @NotEmpty
    private List<String> allowedRoles;
    @NotNull
    private Integer minimumYearsOfExperience;
    @NotEmpty
    private List<String> allowedClearanceLevels;
    @NotEmpty
    private List<String> allowedEmploymentStatus;
}
