package org.pacs.accesscontrolapi.models.policymodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
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
    @JsonProperty("ID")
    private String id;

    @NotEmpty
    @Valid
    @JsonProperty("UAS")
    private Set<UserPolicyModel> userAttributesSet;

    @NotNull
    @Valid
    @JsonProperty("APA")
    private AccessPointPolicyModel accessPointAttributes;
}
