package org.pacs.accesscontrolapi.models.policymodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointPolicyModel {
    @NotBlank
    private String location;
    @NotNull
    private Integer occupancyLevel;
}
