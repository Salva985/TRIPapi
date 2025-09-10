package com.tripapi.controller;

import com.tripapi.dto.ItineraryDay.ItineraryDayRequestDTO;
import com.tripapi.dto.ItineraryDay.ItineraryDayResponseDTO;
import com.tripapi.service.interfaces.ItineraryDayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/itinerary-days")
public class ItineraryDayController {

    @Autowired
    private ItineraryDayService itineraryDayService;

    @GetMapping
    public List<ItineraryDayResponseDTO> findAll() {
        return itineraryDayService.findAll();
    }

    @GetMapping("/{id}")
    public ItineraryDayResponseDTO findById(@PathVariable Long id) {
        return itineraryDayService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ItineraryDayResponseDTO> create(@Valid @RequestBody ItineraryDayRequestDTO body) {
        ItineraryDayResponseDTO created = itineraryDayService.create(body);
        return ResponseEntity.created(URI.create("/api/itinerary-days/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ItineraryDayResponseDTO update(@PathVariable Long id, @Valid @RequestBody ItineraryDayRequestDTO body) {
        return itineraryDayService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        itineraryDayService.delete(id);
    }
}
