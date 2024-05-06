package org.pacs.accesscontrolapi.models.policymodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserPolicyModel {
    @NotBlank
    @JsonProperty("ADP")
    private String department;
    @NotEmpty
    @JsonProperty("ARL")
    private List<String> allowedRoles;
    @NotEmpty
    @JsonProperty("ACL")
    private List<String> allowedClearanceLevels;
    @NotEmpty
    @JsonProperty("AES")
    private List<String> allowedEmploymentStatus;
}
