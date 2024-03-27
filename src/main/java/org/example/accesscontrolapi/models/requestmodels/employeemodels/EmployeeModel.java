package org.example.accesscontrolapi.models.requestmodels.employeemodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.example.accesscontrolapi.models.requestmodels.UserModel;

@Setter
@Getter
public class EmployeeModel extends UserModel {
    @NotBlank
    private String department;
    @NotNull
    private Integer yearsOfExperience;
    @NotBlank
    private String employmentStatus;

    public EmployeeModel(String id, String role, String department, TimeSchedule timeSchedule, Integer yearsOfExperience, String clearanceLevel, String employmentStatus) {
        super(id, role, timeSchedule, clearanceLevel);
        this.department = department;
        this.yearsOfExperience = yearsOfExperience;
        this.employmentStatus = employmentStatus;
    }
}