package com.tripapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADVENTURE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class ActivityAdventure extends Activity {

    @Column(nullable = false, length = 50)
    private String difficultyLevel;

    @Column(length = 200)
    private String equipmentRequired;
}
