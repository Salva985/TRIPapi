package com.tripapi.dto.Budget;

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
public class BudgetResponseDTO {

    private Long Id;

    private Long tripId;
    private String tripName;

    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private String currencyCode; //  "EUR", "USD"
    private BudgetCategory category;

    private String notes;
}
