package org.example.accesscontrolapi.models.requestmodels;

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
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @NotEmpty
    private Set<String> daysOfWeek;
}