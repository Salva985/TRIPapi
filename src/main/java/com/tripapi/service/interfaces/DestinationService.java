package com.tripapi.service.interfaces;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;

import java.util.List;

public interface DestinationService {
    DestinationResponseDTO create(DestinationRequestDTO request);
    DestinationResponseDTO getById(Long id);
    List<DestinationResponseDTO> list(String country, String search);
    DestinationResponseDTO update(Long id, DestinationRequestDTO request);
    void delete(Long id);
}