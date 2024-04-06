package org.pacs.accesscontrolapi.models.requestmodels.employeemodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.pacs.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.pacs.accesscontrolapi.models.requestmodels.UserModel;

@Setter
@Getter
public class EmployeeModel extends UserModel {

    @NotNull
    private Integer yearsOfExperience;
    @NotBlank
    private String employmentStatus;

    public EmployeeModel(String id, String role, String department, TimeSchedule timeSchedule, Integer yearsOfExperience, String clearanceLevel, String employmentStatus) {
        super(id, role,department, timeSchedule, clearanceLevel);
        this.yearsOfExperience = yearsOfExperience;
        this.employmentStatus = employmentStatus;
    }
}