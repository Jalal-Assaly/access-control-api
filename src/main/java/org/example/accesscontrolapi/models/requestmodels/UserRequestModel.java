package org.example.accesscontrolapi.models.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRequestModel {
    private String id;
    private String role;
    private String department;
    private TimeSchedule timeSchedule;
    private Integer yearsOfExperience;
    private String clearanceLevel;
    private String employmentStatus;
}