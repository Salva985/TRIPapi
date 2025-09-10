package com.tripapi.dto.ItineraryDay;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDayRequestDTO {

    @NotNull(message = "Trip id is required")
    private Long tripId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Day number is required")
    private Integer dayNumber;

    private String title;
    private String notes;
}
