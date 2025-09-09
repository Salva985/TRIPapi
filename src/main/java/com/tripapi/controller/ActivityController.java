package com.tripapi.controller;

import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.service.interfaces.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<ActivityResponseDTO> findAll() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public ActivityResponseDTO findById(@PathVariable Long id) {
        return activityService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ActivityResponseDTO> create(@Valid @RequestBody ActivityRequestDTO body) {
        ActivityResponseDTO created = activityService.create(body);
        return ResponseEntity.created(URI.create("/api/activities/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    public ActivityResponseDTO update(@PathVariable Long id, @Valid @RequestBody ActivityRequestDTO body) {
        return activityService.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        activityService.delete(id);
    }
}
