package com.tripapi.dto.Trip;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponseDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private Long destinationId;
    private String destinationCity;
    private String destinationCountry;

    private String notes;
}
