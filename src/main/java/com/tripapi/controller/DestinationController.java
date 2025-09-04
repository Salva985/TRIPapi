package com.tripapi.controller;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.service.DestinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService service;

    @PostMapping
    public ResponseEntity<DestinationResponseDTO> create(@Valid @RequestBody DestinationRequestDTO body) {
        DestinationResponseDTO created = service.create(body);
        return ResponseEntity.created(URI.create("/api/destinations/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public DestinationResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<DestinationResponseDTO> list(
            @RequestParam(required = false) String country,
            @RequestParam(required = false, name = "search") String search
    ) {
        return service.list(country, search);
    }

    @PutMapping("/{id}")
    public DestinationResponseDTO update(@PathVariable Long id,
                                         @Valid @RequestBody DestinationRequestDTO body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}