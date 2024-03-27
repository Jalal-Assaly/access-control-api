package org.example.accesscontrolapi.models.requestmodels.visitormodels;

import org.example.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.example.accesscontrolapi.models.requestmodels.UserModel;


public class VisitorModel extends UserModel {
    public VisitorModel(String id, String role, TimeSchedule timeSchedule, String clearanceLevel) {
        super(id, role, timeSchedule, clearanceLevel);
    }
}
