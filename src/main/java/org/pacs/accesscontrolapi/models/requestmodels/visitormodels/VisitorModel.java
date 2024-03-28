package org.pacs.accesscontrolapi.models.requestmodels.visitormodels;

import org.pacs.accesscontrolapi.models.requestmodels.TimeSchedule;
import org.pacs.accesscontrolapi.models.requestmodels.UserModel;


public class VisitorModel extends UserModel {
    public VisitorModel(String id, String role, TimeSchedule timeSchedule, String clearanceLevel) {
        super(id, role, timeSchedule, clearanceLevel);
    }
}
