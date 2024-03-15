package org.example.accesscontrolapi.models.policymodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPolicyModel {

    private String department;
    private List<String> allowedRoles;
    private Integer minimumYearsOfExperience;
    private List<String> allowedClearanceLevels;
    private List<String> allowedEmploymentStatus;
}
