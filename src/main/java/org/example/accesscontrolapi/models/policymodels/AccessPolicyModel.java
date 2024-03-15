package org.example.accesscontrolapi.models.policymodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AccessPolicyModel {
    private String id;
    private AccessPointPolicyModel accessPointAttributes;
    private Set<UserPolicyModel> userAttributesSet;
}
