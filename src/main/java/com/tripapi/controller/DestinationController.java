package com.tripapi.controller;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.service.interfaces.DestinationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public List<DestinationResponseDTO> findAll(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String search
    ) {
        return destinationService.findAll();
    }

    @GetMapping("/{id}")
    public DestinationResponseDTO findById(@PathVariable Long id) {
        return destinationService.findById(id);
    }

    @PostMapping
    public ResponseEntity<DestinationResponseDTO> create(@Valid @RequestBody DestinationRequestDTO body) {
        DestinationResponseDTO created = destinationService.create(body);
        return ResponseEntity.created(URI.create("/api/destinations/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public DestinationResponseDTO update(@PathVariable Long id,
                                         @Valid @RequestBody DestinationRequestDTO body) {
        return destinationService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        destinationService.delete(id);
    }
}