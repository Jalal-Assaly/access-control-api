package org.example.accesscontrolapi.models.requestmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessPointModel {
    @NotBlank
    private String id;
    @NotBlank
    private String location;
    @NotNull
    private Boolean isTampered;
    @NotNull
    private Integer occupancyLevel;
}
