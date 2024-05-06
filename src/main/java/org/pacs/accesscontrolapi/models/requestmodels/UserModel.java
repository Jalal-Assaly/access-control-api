package org.pacs.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class UserModel {
    @NotBlank
    @JsonProperty("ID")
    private String id;
    @NotBlank
    @JsonProperty("RL")
    private String role;
    @NotBlank
    @JsonProperty("DP")
    private String department;
    @NotNull
    @JsonProperty("TS")
    @Valid
    private TimeSchedule timeSchedule;
    @NotBlank
    @JsonProperty("CL")
    private String clearanceLevel;
}
