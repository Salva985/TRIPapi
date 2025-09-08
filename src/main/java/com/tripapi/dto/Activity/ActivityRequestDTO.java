package com.tripapi.dto.Activity;

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
public class ActivityRequestDTO {
    @NotNull(message = "Trip id is required")
    private Long tripId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    /**
     * Allowed: SIGHTSEEING, ADVENTURE, CULTURAL
     */
    @NotBlank(message = "Type is required")
    private String type;

    // ----- Subtype-(optional) -----
    // SIGHTSEEING
    private String landmarkName;
    private String location;

    // ADVENTURE
    private String difficultyLevel;
    private String equipmentRequired;

    // CULTURAL
    private String eventName;
    private String organizer;
}