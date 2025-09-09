package com.tripapi.service.impl;

import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.enums.ActivityType;
import com.tripapi.model.*;
import com.tripapi.repository.ActivityRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.service.interfaces.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private TripRepository tripRepository;

    @Override
    public List<ActivityResponseDTO> findAll() {
        return activityRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ActivityResponseDTO findById(Long id) {
        Activity a = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));
        return toDTO(a);
    }

    @Override
    public ActivityResponseDTO create(ActivityRequestDTO dto) {
        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        Activity entity = buildSubclass(dto.getType());
        // common fields
        entity.setTrip(tripRef);
        entity.setDate(dto.getDate());
        entity.setTitle(dto.getTitle());
        entity.setNotes(dto.getNotes());
        entity.setType(dto.getType());

        // subtype fields
        applySubtypeFields(entity, dto);

        return toDTO(activityRepository.save(entity));
    }

    @Override
    public ActivityResponseDTO update(Long id, ActivityRequestDTO dto) {
        Activity a = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));

        // do not allow changing the subtype (and therefore the type enum)
        if (a.getType() != dto.getType()) {
            throw new BadRequestException("Changing activity type is not supported. Delete and create a new activity.");
        }

        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        // common fields
        a.setTrip(tripRef);
        a.setDate(dto.getDate());
        a.setTitle(dto.getTitle());
        a.setNotes(dto.getNotes()); // ✅ was description before

        // subtype fields
        applySubtypeFields(a, dto);

        return toDTO(activityRepository.save(a));
    }

    @Override
    public void delete(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity not found with ID: " + id);
        }
        activityRepository.deleteById(id);
    }

    // -------- helpers --------

    private Activity buildSubclass(ActivityType type) {
        if (type == null) {
            throw new BadRequestException("Type is required (SIGHTSEEING, ADVENTURE, CULTURAL).");
        }
        return switch (type) {
            case SIGHTSEEING -> new ActivitySightseeing();
            case ADVENTURE   -> new ActivityAdventure();
            case CULTURAL    -> new ActivityCultural();
        };
    }

    private void applySubtypeFields(Activity entity, ActivityRequestDTO dto) {
        if (entity instanceof ActivitySightseeing s) {
            s.setLandmarkName(dto.getLandmarkName());
            s.setLocation(dto.getLocation());
        } else if (entity instanceof ActivityAdventure a) {
            a.setDifficultyLevel(dto.getDifficultyLevel());
            a.setEquipmentRequired(dto.getEquipmentRequired());
        } else if (entity instanceof ActivityCultural c) {
            c.setEventName(dto.getEventName());
            c.setOrganizer(dto.getOrganizer());
        }
    }

    private ActivityResponseDTO toDTO(Activity a) {
        ActivityResponseDTO.ActivityResponseDTOBuilder b = ActivityResponseDTO.builder()
                .id(a.getId())
                .tripId(a.getTrip().getId())
                .tripName(a.getTrip().getName())
                .date(a.getDate())
                .title(a.getTitle())
                .notes(a.getNotes())
                .type(a.getType());

        if (a instanceof ActivitySightseeing s) {
            b.landmarkName(s.getLandmarkName())
                    .location(s.getLocation());
        } else if (a instanceof ActivityAdventure adv) {
            b.difficultyLevel(adv.getDifficultyLevel())
                    .equipmentRequired(adv.getEquipmentRequired());
        } else if (a instanceof ActivityCultural c) {
            b.eventName(c.getEventName())
                    .organizer(c.getOrganizer());
        }
        return b.build();
    }

    // Inline exceptions (MarineConservation style)
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) { super(message); }
    }
}