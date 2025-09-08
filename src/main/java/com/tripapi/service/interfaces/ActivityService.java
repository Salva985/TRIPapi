package com.tripapi.service.interfaces;

import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;

import java.util.List;

public interface ActivityService {
    List<ActivityResponseDTO> findAll();
    ActivityResponseDTO findById(Long id);
    ActivityResponseDTO create(ActivityRequestDTO dto);
    ActivityResponseDTO update(Long id, ActivityRequestDTO dto);
    void delete(Long id);
}