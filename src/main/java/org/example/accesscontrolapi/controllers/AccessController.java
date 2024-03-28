package org.example.accesscontrolapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.accesscontrolapi.documents.AccessLog;
import org.example.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.example.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.example.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.example.accesscontrolapi.services.AccessLogService;
import org.example.accesscontrolapi.services.PolicyDecisionPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/access-control")
public class AccessController {

    private final AccessLogService accessLogService;
    private final PolicyDecisionPoint pdp;

    @GetMapping("list/logs")
    public ResponseEntity<List<AccessLog>> getAllAccessLogs() {
        List<AccessLog> accessLogs = accessLogService.getAllAccessLogs();
        return new ResponseEntity<>(accessLogs, HttpStatus.OK);
    }

    @PutMapping("/request/employee")
    public ResponseEntity<AccessResponseModel> evaluateEmployeeAccessRequest(@RequestBody EmployeeAccessRequestModel requestModel) {
        AccessResponseModel accessResponseModel = pdp.evaluateAccessRequest(requestModel);
        return new ResponseEntity<>(accessResponseModel, HttpStatus.OK);
    }

    @PutMapping("/request/visitor")
    public ResponseEntity<AccessResponseModel> evaluateVisitorAccessRequest(@RequestBody VisitorAccessRequestModel requestModel) {
        AccessResponseModel accessResponseModel = pdp.evaluateAccessRequest(requestModel);
        return new ResponseEntity<>(accessResponseModel, HttpStatus.OK);
    }
}
