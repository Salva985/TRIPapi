package com.tripapi.dto.Trip;

import jakarta.validation.constraints.NotBlank;
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
public class TripRequestDTO {

    @NotBlank(message = "Trip name is required")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "Start date is required")
    private LocalDate endDate;

    @NotNull(message = "Destination id is required")
    private Long destinationId;

    private String notes;
}
