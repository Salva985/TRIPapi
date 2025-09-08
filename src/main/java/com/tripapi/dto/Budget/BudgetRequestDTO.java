package com.tripapi.dto.Budget;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.tripapi.enums.BudgetCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequestDTO {

    @NotNull(message = "Trip ID is required")
    private Long tripId;

    @NotNull(message = "Planned amount is required")
    @PositiveOrZero(message = "Planned amount cannot be negative")
    private BigDecimal plannedAmount;

    @PositiveOrZero(message = "Spent amount cannot be negative")
    private BigDecimal spentAmount;

    @NotNull(message = "Currency code is required")
    private String currencyCode; //  "EUR", "USD"

    @NotNull(message = "Category is required")
    private BudgetCategory category;

    private String notes;
}
