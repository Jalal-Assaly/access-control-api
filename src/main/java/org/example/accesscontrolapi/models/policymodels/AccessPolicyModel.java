package org.example.accesscontrolapi.models.policymodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AccessPolicyModel {
    @NotBlank
    private String id;
    @NotEmpty
    private Set<UserPolicyModel> userAttributesSet;
    @NotNull
    private AccessPointPolicyModel accessPointAttributes;
}
