package org.pacs.accesscontrolapi.models.requestmodels.employeemodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.pacs.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.pacs.accesscontrolapi.models.requestmodels.UserModel;

@Setter
@Getter
public class EmployeeModel extends UserModel {

    @NotBlank
    @JsonProperty("ES")
    private String employmentStatus;

    public EmployeeModel(String id, String role, String department, TimeSchedule timeSchedule, String clearanceLevel, String employmentStatus) {
        super(id, role,department, timeSchedule, clearanceLevel);
        this.employmentStatus = employmentStatus;
    }
}