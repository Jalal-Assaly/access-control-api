package org.example.accesscontrolapi.models.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointRequestModel {
    private String id;
    private String location;
    private Boolean isTampered;
    private Integer occupancyLevel;
}
