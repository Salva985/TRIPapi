package com.tripapi.service.interfaces;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.dto.Trip.TripRequestDTO;
import com.tripapi.dto.Trip.TripResponseDTO;

import java.util.List;

public interface TripService {

    List<TripResponseDTO> findAll();

    TripResponseDTO findById(Long id);

    TripResponseDTO create(TripRequestDTO dto);

    TripResponseDTO update(Long id, TripRequestDTO dto);

    void delete(Long id);
}
