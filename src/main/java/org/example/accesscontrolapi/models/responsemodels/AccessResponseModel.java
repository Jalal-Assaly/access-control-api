package org.example.accesscontrolapi.models.responsemodels;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessResponseModel {
    private Boolean decision;
    private String reason;
}
