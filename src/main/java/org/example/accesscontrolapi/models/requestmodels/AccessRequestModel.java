package org.example.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessRequestModel {
    @JsonProperty("userAttributes")
    private UserRequestModel userRequestModel;
    @JsonProperty("accessPointAttributes")
    private AccessPointRequestModel accessPointRequestModel;
}
