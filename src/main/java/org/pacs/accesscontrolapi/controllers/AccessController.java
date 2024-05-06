package org.pacs.accesscontrolapi.controllers;

import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.AccessLog;
import org.pacs.accesscontrolapi.models.policymodels.EmergencyStatus;
import org.pacs.accesscontrolapi.models.policymodels.EnvironmentModel;
import org.pacs.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.pacs.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.pacs.accesscontrolapi.services.AccessLogService;
import org.pacs.accesscontrolapi.services.PolicyDecisionPoint;
import org.pacs.accesscontrolapi.services.PolicyInformationPoint;
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
    private final PolicyInformationPoint pip;

    @GetMapping("list/logs")
    public ResponseEntity<List<AccessLog>> getAllAccessLogs() {
        List<AccessLog> accessLogs = accessLogService.getAllAccessLogs();
        return new ResponseEntity<>(accessLogs, HttpStatus.OK);
    }

    @GetMapping("fetch/environment")
    public ResponseEntity<EnvironmentModel> getEnvironment() {
        EnvironmentModel environmentModel = pip.getEnvironmentModel();
        return new ResponseEntity<>(environmentModel, HttpStatus.OK);
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

    @PutMapping("/activate-emergency")
    public ResponseEntity<Void> activateEmergency(@RequestParam(name = "emergency_status") EmergencyStatus emergencyStatus) {
        pip.setEnvironmentEmergencyStatus(emergencyStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
