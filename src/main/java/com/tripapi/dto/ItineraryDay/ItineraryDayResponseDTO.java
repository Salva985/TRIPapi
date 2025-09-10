package com.tripapi.dto.ItineraryDay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryDayResponseDTO {

    private Long id;

    private Long tripId;
    private String tripName;

    private LocalDate date;
    private Integer dayNumber;
    private String title;
    private String notes;
}
