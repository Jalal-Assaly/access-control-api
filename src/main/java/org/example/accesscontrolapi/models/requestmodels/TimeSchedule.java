package org.example.accesscontrolapi.models.requestmodels;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class TimeSchedule {
    private LocalTime startTime;
    private LocalTime endTime;
    private Set<String> daysOfWeek;
}