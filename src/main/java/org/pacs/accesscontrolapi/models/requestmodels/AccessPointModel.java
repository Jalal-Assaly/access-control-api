package org.pacs.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointModel {
    @NotBlank
    @JsonProperty("ID")
    private String id;
    @NotBlank
    @JsonProperty("LC")
    private String location;
    @NotNull
    @JsonProperty("IT")
    private Boolean isTampered;
    @NotNull
    @JsonProperty("OL")
    private Integer occupancyLevel;
}
