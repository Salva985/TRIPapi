package com.tripapi.controller;

import com.tripapi.dto.Budget.BudgetRequestDTO;
import com.tripapi.dto.Budget.BudgetResponseDTO;
import com.tripapi.service.interfaces.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public List<BudgetResponseDTO> findAll() {
        return budgetService.findAll();
    }

    @GetMapping("/{id}")
    public BudgetResponseDTO findById(@PathVariable Long id) {
        return budgetService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> create(@Valid @RequestBody BudgetRequestDTO body) {
        BudgetResponseDTO created = budgetService.create(body);
        return ResponseEntity
                .created(URI.create("/api/budgets/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public BudgetResponseDTO update(@PathVariable Long id,
                                    @Valid @RequestBody BudgetRequestDTO body) {
        return budgetService.update(id, body);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        budgetService.delete(id);
    }
}
