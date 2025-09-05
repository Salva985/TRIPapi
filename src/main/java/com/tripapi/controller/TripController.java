package com.tripapi.controller;

import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.dto.Trip.TripRequestDTO;
import com.tripapi.dto.Trip.TripResponseDTO;
import com.tripapi.service.interfaces.TripService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping
    public List<TripResponseDTO> findAll() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    public TripResponseDTO findById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TripResponseDTO> create(@Valid @RequestBody TripRequestDTO body) {
        TripResponseDTO created = tripService.create(body);
        return ResponseEntity.created(URI.create("/api/destinations/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public TripResponseDTO update(@PathVariable Long id,
                                  @Valid @RequestBody TripRequestDTO body) {
        return tripService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        tripService.delete(id);
    }
}
