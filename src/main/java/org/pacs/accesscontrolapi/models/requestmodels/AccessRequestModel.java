package org.pacs.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AccessRequestModel {
    @NotNull
    @Valid
    @JsonProperty("accessPointAttributes")
    private AccessPointModel accessPointModel;
}
