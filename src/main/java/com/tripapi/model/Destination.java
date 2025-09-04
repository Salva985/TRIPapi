package com.tripapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "City is required")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "Country is required")
    @Column(nullable = false, length = 100)
    private String country;

    @NotBlank(message = "Timezone is required")
    @Column(nullable = false, length = 60)
    private String timezone;

    @NotBlank(message = "Currency code is required")
    @Column(nullable = false, length = 10)
    private String currencyCode;
}
