package com.tripapi.service.impl;

import com.tripapi.dto.Budget.BudgetRequestDTO;
import com.tripapi.dto.Budget.BudgetResponseDTO;
import com.tripapi.model.Budget;
import com.tripapi.model.Trip;
import com.tripapi.repository.BudgetRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.service.interfaces.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripapi.enums.BudgetCategory;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public List<BudgetResponseDTO> findAll() {
        return budgetRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public BudgetResponseDTO findById(Long id ) {
        Budget b = budgetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Budget not found with ID: " + id));
        return toDTO(b);
    }

    @Override
    public BudgetResponseDTO create(BudgetRequestDTO dto) {
        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(()-> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        Budget entity = Budget.builder()
                .trip(tripRef)
                .plannedAmount(dto.getPlannedAmount())
                .spentAmount(dto.getPlannedAmount())
                .currencyCode(dto.getCurrencyCode())
                .category(dto.getCategory())
                .notes(dto.getNotes())
                .build();

        return toDTO(budgetRepository.save(entity));
    }

    @Override
    public BudgetResponseDTO update(Long id, BudgetRequestDTO dto) {
        Budget b = budgetRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Budge not found with ID: " + id));

        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(()-> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        b.setTrip(tripRef);
        b.setPlannedAmount(dto.getPlannedAmount());
        b.setSpentAmount(dto.getSpentAmount());
        b.setCurrencyCode(dto.getCurrencyCode());
        b.setCategory(dto.getCategory());
        b.setNotes(dto.getNotes());

        return toDTO(budgetRepository.save(b));
    }

    @Override
    public void delete(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget not found with ID: " + id);
        }
        budgetRepository.deleteById(id);
    }

    // ---- mapping helper ----
    private BudgetResponseDTO toDTO(Budget b) {
        return new BudgetResponseDTO(
                b.getId(),
                b.getTrip().getId(),
                b.getTrip().getName(),
                b.getPlannedAmount(),
                b.getSpentAmount(),
                b.getCurrencyCode(),
                b.getCategory(),
                b.getNotes()
        );
    }

    // Inline exception (MarineConservation style)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
}
