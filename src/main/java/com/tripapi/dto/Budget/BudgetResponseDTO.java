package com.tripapi.dto.Budget;

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
public class BudgetResponseDTO {

    private Long Id;

    private Long tripId;
    private String tripName;

    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private String currencyCode; //  "EUR", "USD"

    private String notes;
}
