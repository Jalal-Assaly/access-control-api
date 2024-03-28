package org.pacs.accesscontrolapi.models.policymodels;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("userAttributesSet")
    private Set<UserPolicyModel> userAttributesSet;

    @NotNull
    @JsonProperty("accessPointAttributes")
    private AccessPointPolicyModel accessPointAttributes;
}
