package org.example.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessRequestModel {
    @NotNull
    @JsonProperty("userAttributes")
    private UserRequestModel userRequestModel;
    @NotNull
    @JsonProperty("accessPointAttributes")
    private AccessPointRequestModel accessPointRequestModel;
}
