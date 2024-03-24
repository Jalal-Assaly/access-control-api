package org.example.accesscontrolapi.controllers;

import lombok.RequiredArgsConstructor;
import org.example.accesscontrolapi.models.requestmodels.AccessRequestModel;
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
@RequestMapping("/access-control")
public class AccessController {

    private final PolicyDecisionPoint pdp;

    @PutMapping("/request")
    public ResponseEntity<AccessResponseModel> evaluateAccessRequest(@RequestBody AccessRequestModel requestModel) {
        Boolean decision = pdp.evaluateAccessRequest(requestModel);
        String reason = (decision) ? "Attributes are all matching" : "Attributes do not match access policy";
        return new ResponseEntity<>(new AccessResponseModel(decision, reason), HttpStatus.OK);
    }
}
