package org.pacs.accesscontrolapi.models.requestmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class TimeSchedule {
    @NotNull
    @JsonProperty("ST")
    private LocalTime startTime;
    @NotNull
    @JsonProperty("ET")
    private LocalTime endTime;
    @NotEmpty
    @JsonProperty("DW")
    private Set<String> daysOfWeek;
}