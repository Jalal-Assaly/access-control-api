package org.example.accesscontrolapi.models.requestmodels.visitormodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.example.accesscontrolapi.models.requestmodels.AccessRequestModel;

@Setter
@Getter
public class VisitorAccessRequestModel extends AccessRequestModel {
    @NotNull
    @JsonProperty("userAttributes")
    private VisitorModel visitorModel;

    public VisitorAccessRequestModel(VisitorModel visitorModel, AccessPointModel accessPointModel) {
        super(accessPointModel);
        this.visitorModel = visitorModel;
    }
}
