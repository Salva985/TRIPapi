package com.tripapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.tripapi.enums.TripType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Trip name is required")
    @Column(nullable = false, length = 120)
    private String name;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TripType tripType;
    
    @Column(length = 500)
    private String notes;

    @OneToMany(
            mappedBy = "trip",
            cascade = CascadeType.ALL,      // propagate REMOVE to children
            orphanRemoval = true            // delete rows when parent is removed
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Activity> activities = new ArrayList<>();
}
