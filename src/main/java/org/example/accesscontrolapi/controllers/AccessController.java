package org.example.accesscontrolapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.example.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.example.accesscontrolapi.services.PolicyDecisionPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access-control/request")
public class AccessController {

    private final PolicyDecisionPoint pdp;

    @PutMapping("/employee")
    public ResponseEntity<AccessResponseModel> evaluateEmployeeAccessRequest(@RequestBody EmployeeAccessRequestModel requestModel) {
        Boolean decision = pdp.evaluateAccessRequest(requestModel);
        return new ResponseEntity<>(new AccessResponseModel(decision), HttpStatus.OK);
    }

    @PutMapping("/visitor")
    public ResponseEntity<AccessResponseModel> evaluateVisitorAccessRequest(@RequestBody VisitorAccessRequestModel requestModel) {
        Boolean decision = pdp.evaluateAccessRequest(requestModel);
        return new ResponseEntity<>(new AccessResponseModel(decision), HttpStatus.OK);
    }
}
