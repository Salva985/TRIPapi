package com.tripapi.service.interfaces;

import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.dto.common.PagedResponse;

import java.util.List;

public interface ActivityService {
    List<ActivityResponseDTO> findAll();
    ActivityResponseDTO findById(Long id);
    ActivityResponseDTO create(ActivityRequestDTO dto);
    ActivityResponseDTO update(Long id, ActivityRequestDTO dto);
    void delete(Long id);

    PagedResponse<ActivityResponseDTO> findAll(String search, int page, int pageSize);
}