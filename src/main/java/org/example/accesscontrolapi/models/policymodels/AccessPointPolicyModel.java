package org.example.accesscontrolapi.models.policymodels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointPolicyModel {
    private String location;
    private Integer occupancyLevel;
}
