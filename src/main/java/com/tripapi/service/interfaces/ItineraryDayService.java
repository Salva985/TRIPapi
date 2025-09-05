package com.tripapi.service.interfaces;

import com.tripapi.dto.ItineraryDay.ItineraryDayRequestDTO;
import com.tripapi.dto.ItineraryDay.ItineraryDayResponseDTO;

import java.util.List;

public interface ItineraryDayService {

    List<ItineraryDayResponseDTO> findAll();

    ItineraryDayResponseDTO findById(Long id);

    ItineraryDayResponseDTO create(ItineraryDayRequestDTO dto);

    ItineraryDayResponseDTO update(Long id, ItineraryDayRequestDTO dto);

    void delete(Long id);
}