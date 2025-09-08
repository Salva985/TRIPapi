package com.tripapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SIGHTSEEING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivitySightseeing extends Activity {

    @Column(nullable = false, length = 150)
    private String landmarkName;

    @Column(length = 100)
    private String location;
}
