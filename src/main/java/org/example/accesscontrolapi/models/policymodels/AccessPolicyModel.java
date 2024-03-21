package org.example.accesscontrolapi.models.policymodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class AccessPolicyModel {
    private String id;
    private Set<UserPolicyModel> userAttributesSet;
    private AccessPointPolicyModel accessPointAttributes;
}
