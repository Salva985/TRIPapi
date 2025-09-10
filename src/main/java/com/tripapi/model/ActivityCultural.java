package com.tripapi.model;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CULTURAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActivityCultural extends Activity {

    @Column(nullable = false, length = 150)
    private String eventName;

    @Column(length = 100)
    private String organizer;
}
