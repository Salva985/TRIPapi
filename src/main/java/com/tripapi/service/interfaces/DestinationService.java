package com.tripapi.service.interfaces;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;

import java.util.List;

public interface DestinationService {

    List<DestinationResponseDTO> findAll(); // optional filters

    DestinationResponseDTO findById(Long id);

    DestinationResponseDTO create(DestinationRequestDTO dto);

    DestinationResponseDTO update(Long id, DestinationRequestDTO dto);

    void delete(Long id);
}