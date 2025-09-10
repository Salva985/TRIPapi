package com.tripapi.dto.Activity;

import com.tripapi.enums.ActivityType;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponseDTO {
    private Long id;

    private Long tripId;
    private String tripName;

    private LocalDate date;
    private String title;
    private String notes;

    /**
     * SIGHTSEEING / ADVENTURE / CULTURAL
     */
    private ActivityType type;

    // ----- Subtype-(nullable) -----
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