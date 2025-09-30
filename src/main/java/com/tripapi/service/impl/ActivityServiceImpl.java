package com.tripapi.service.impl;

import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.enums.ActivityType;
import com.tripapi.model.Activity;
import com.tripapi.model.ActivityAdventure;
import com.tripapi.model.ActivityCultural;
import com.tripapi.model.ActivitySightseeing;
import com.tripapi.model.Trip;
import com.tripapi.repository.ActivityRepository;
import com.tripapi.repository.TripRepository;
import com.tripapi.service.interfaces.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tripapi.dto.common.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        ActivityType type = dto.getType();
        if (type == null) {
            throw new BadRequestException("Type is required (SIGHTSEEING, ADVENTURE, CULTURAL).");
        }

        Activity entity = buildSubclass(type);

        // common fields
        entity.setTrip(tripRef);
        entity.setDate(dto.getDate());
        entity.setTitle(dto.getTitle());
        entity.setNotes(dto.getNotes());
        entity.setType(type); // enum stored on base entity

        // subtype-specific fields
        applySubtypeFields(entity, dto);

        return toDTO(activityRepository.save(entity));
    }

    @Override
    public ActivityResponseDTO update(Long id, ActivityRequestDTO dto) {
        Activity a = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with ID: " + id));

        // keep subtype stable (don’t allow changing type)
        ActivityType currentType = a.getType();
        if (dto.getType() != null && dto.getType() != currentType) {
            throw new BadRequestException("Changing activity type is not supported. Delete and create a new activity.");
        }

        Trip tripRef = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with ID: " + dto.getTripId()));

        // common fields
        a.setTrip(tripRef);
        a.setDate(dto.getDate());
        a.setTitle(dto.getTitle());
        a.setNotes(dto.getNotes());

        // subtype-specific fields
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

    @Override
    public PagedResponse<ActivityResponseDTO> findAll(String search, int page, int pageSize) {
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), Math.max(pageSize, 1));
        String q = (search == null) ? "" : search.trim();

        Page<Activity> pageResult = activityRepository.search(q, pageable);

        List<ActivityResponseDTO> items = pageResult.getContent()
                .stream()
                .map(this::toDTO)
                .toList();

        return new PagedResponse<>(
                items,
                new PagedResponse.Meta(
                        page,                      // 1-based page for the client
                        pageSize,
                        pageResult.getTotalElements()
                )
        );
    }

    // -------- helpers --------

    private Activity buildSubclass(ActivityType type) {
        return switch (type) {
            case SIGHTSEEING -> new ActivitySightseeing();
            case ADVENTURE   -> new ActivityAdventure();
            case CULTURAL    -> new ActivityCultural();
            case OTHER    -> new Activity();
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
                .type(a.getType()); // enum directly

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


    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
    }
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) { super(message); }
    }
}