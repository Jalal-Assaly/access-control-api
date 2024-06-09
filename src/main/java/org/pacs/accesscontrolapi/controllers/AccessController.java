package org.pacs.accesscontrolapi.controllers;

import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.EmployeeAccessLog;
import org.pacs.accesscontrolapi.documents.VisitorAccessLog;
import org.pacs.accesscontrolapi.models.historymodels.EmployeeHistoryModel;
import org.pacs.accesscontrolapi.models.historymodels.VisitorHistoryModel;
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

    @GetMapping("/list/logs/employees")
    public ResponseEntity<List<EmployeeAccessLog>> getAllEmployeeAccessLogs() {
        List<EmployeeAccessLog> employeeAccessLogs = accessLogService.getAllEmployeeAccessLogs();
        return new ResponseEntity<>(employeeAccessLogs, HttpStatus.OK);
    }

    @GetMapping("/list/logs/visitors")
    public ResponseEntity<List<VisitorAccessLog>> getAllVisitorAccessLogs() {
        List<VisitorAccessLog> visitorAccessLogs = accessLogService.getAllVisitorAccessLogs();
        return new ResponseEntity<>(visitorAccessLogs, HttpStatus.OK);
    }

    @GetMapping("/employee/list/history/id/{id}")
    public ResponseEntity<List<EmployeeHistoryModel>> getEmployeeHistory(@PathVariable String id) {
        List<EmployeeHistoryModel> employeeAccessLogs = accessLogService.getEmployeeHistory(id);
        return new ResponseEntity<>(employeeAccessLogs, HttpStatus.OK);
    }

    @GetMapping("/visitor/list/history/id/{id}")
    public ResponseEntity<List<VisitorHistoryModel>> getVisitorHistory(@PathVariable String id) {
        List<VisitorHistoryModel> visitorAccessLogs = accessLogService.getVisitorHistory(id);
        return new ResponseEntity<>(visitorAccessLogs, HttpStatus.OK);
    }

    @GetMapping("/fetch/environment")
    public ResponseEntity<EnvironmentModel> getEnvironment() {
        EnvironmentModel environmentModel = pip.getEnvironmentModel();
        return new ResponseEntity<>(environmentModel, HttpStatus.OK);
    }

    @PutMapping("/request/employee")
    public ResponseEntity<AccessResponseModel> evaluateEmployeeAccessRequest(@RequestBody EmployeeAccessRequestModel requestModel) {
        AccessResponseModel accessResponseModel = pdp.evaluateEmployeeAccessRequest(requestModel, requestModel.getEmployeeModel().getId(), requestModel.getNonce());
        return new ResponseEntity<>(accessResponseModel, HttpStatus.OK);
    }

    @PutMapping("/request/visitor")
    public ResponseEntity<AccessResponseModel> evaluateVisitorAccessRequest(@RequestBody VisitorAccessRequestModel requestModel) {
        AccessResponseModel accessResponseModel = pdp.evaluateVisitorAccessRequest(requestModel, requestModel.getVisitorModel().getId(), requestModel.getNonce());
        return new ResponseEntity<>(accessResponseModel, HttpStatus.OK);
    }

    @PutMapping("/activate-emergency")
    public ResponseEntity<Void> activateEmergency(@RequestParam(name = "emergency_status") EmergencyStatus emergencyStatus) {
        pip.setEnvironmentEmergencyStatus(emergencyStatus);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("delete/employee/log/{id}")
    public ResponseEntity<Void> deleteEmployeeLog(@PathVariable String id) {
        accessLogService.deleteEmployeeLog(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("delete/visitor/log/{id}")
    public ResponseEntity<Void> deleteVisitorLog(@PathVariable String id) {
        accessLogService.deleteVisitorLog(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
