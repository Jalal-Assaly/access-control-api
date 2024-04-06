package org.pacs.accesscontrolapi.models.requestmodels.employeemodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.pacs.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessRequestModel;

@Setter
@Getter
public class EmployeeAccessRequestModel extends AccessRequestModel {
    @NotNull
    @Valid
    @JsonProperty("userAttributes")
    private EmployeeModel employeeModel;

    public EmployeeAccessRequestModel(EmployeeModel employeeModel, AccessPointModel accessPointModel) {
        super(accessPointModel);
        this.employeeModel = employeeModel;
    }
}
