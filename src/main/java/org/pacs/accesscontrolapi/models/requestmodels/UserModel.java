package org.pacs.accesscontrolapi.models.requestmodels;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class UserModel {
    @NotBlank
    private String id;
    @NotBlank
    private String role;
    @NotNull
    @Valid
    private TimeSchedule timeSchedule;
    @NotBlank
    private String clearanceLevel;
}
