package com.tripapi.dto.Budget;

import com.tripapi.enums.CurrencyCode;
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

    private Long id;

    private Long tripId;
    private String tripName;

    private BigDecimal plannedAmount;
    private BigDecimal spentAmount;
    private CurrencyCode currencyCode; //  "EUR", "USD"
    private BudgetCategory category;

    private String notes;
}
