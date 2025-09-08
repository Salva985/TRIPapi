package com.tripapi.service.interfaces;

import com.tripapi.dto.Budget.BudgetRequestDTO;
import com.tripapi.dto.Budget.BudgetResponseDTO;

import java.util.List;

public interface BudgetService {

    List<BudgetResponseDTO> findAll();

    BudgetResponseDTO findById(Long id);

    BudgetResponseDTO create(BudgetRequestDTO dto);

    BudgetResponseDTO update(Long id, BudgetRequestDTO dto);

    void delete(Long id);
}
