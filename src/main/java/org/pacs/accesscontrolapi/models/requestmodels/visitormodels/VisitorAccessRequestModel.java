package org.pacs.accesscontrolapi.models.requestmodels.visitormodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.pacs.accesscontrolapi.models.requestmodels.AccessPointModel;

@Setter
@Getter
public class VisitorAccessRequestModel {
    @NotNull
    @Valid
    @JsonProperty("UAT")
    private VisitorModel visitorModel;

    @NotNull
    @Valid
    @JsonProperty("APA")
    private AccessPointModel accessPointModel;

    @NotNull
    @Valid
    @JsonProperty("NC")
    private String nonce;
}
