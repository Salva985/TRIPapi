package com.tripapi.dto.Destination;

import com.tripapi.enums.CurrencyCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationResponseDTO {
    private Long id;
    private String city;
    private String country;
    private String timezone;
    private CurrencyCode currencyCode;
}