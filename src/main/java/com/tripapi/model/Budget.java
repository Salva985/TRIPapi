package com.tripapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Trio is required")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @NotNull(message = "Planned amount is required")
    @PositiveOrZero(message = "Planned amount cannot be negative")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal plannedAmount;

    @PositiveOrZero(message = "Spent amount cannot be negative")
    @Column(precision = 12, scale = 2)
    private BigDecimal spentAmount;

    @NotNull(message = "Currency code is required")
    @Column(nullable = false, length = 3)
    private String currencyCode; // "EUR", "USD"

    @Column(length = 500)
    private String notes;
}
