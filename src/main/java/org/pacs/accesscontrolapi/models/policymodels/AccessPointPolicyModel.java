package org.pacs.accesscontrolapi.models.policymodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointPolicyModel {
    @NotBlank
    @JsonProperty("ALC")
    private String location;
    @NotNull
    @JsonProperty("AOL")
    private Integer maxOccupancyLevel;
}
