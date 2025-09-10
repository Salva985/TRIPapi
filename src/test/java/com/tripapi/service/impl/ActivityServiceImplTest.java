package com.tripapi.service.impl;

import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.enums.ActivityType;
import com.tripapi.model.*;
import com.tripapi.repository.ActivityRepository;
import com.tripapi.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock private ActivityRepository activityRepository;
    @Mock private TripRepository tripRepository;

    @InjectMocks private ActivityServiceImpl service;

    private Trip trip;

    @BeforeEach
    void setup() {
        trip = Trip.builder().id(1L).name("Summer Escape").build();
    }


    @Test
    void createTest() {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 14))
                .title("Park Güell Tour")
                .notes("Buy tickets")
                .type(ActivityType.SIGHTSEEING)
                .landmarkName("Park Güell")
                .location("Barcelona")
                .build();

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> {
            ActivitySightseeing s = (ActivitySightseeing) inv.getArgument(0);
            s.setId(100L);
            return s;
        });

        ActivityResponseDTO res = service.create(req);

        assertEquals(100L, res.getId());
        assertEquals(ActivityType.SIGHTSEEING, res.getType());
        assertEquals("Park Güell", res.getLandmarkName());

        verify(tripRepository).findById(1L);
        verify(activityRepository).save(any(Activity.class));
        verifyNoMoreInteractions(activityRepository, tripRepository);
    }

    @Test
    void createMissingType_Test() {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.now())
                .title("No type")
                .notes("x")
                .type(null)
                .build();

        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThrows(ActivityServiceImpl.BadRequestException.class, () -> service.create(req));
        verify(tripRepository).findById(1L);
        verifyNoInteractions(activityRepository);
    }


    @Test
    void findAllTest() {
        ActivitySightseeing s = ActivitySightseeing.builder()
                .id(1L).trip(trip).date(LocalDate.now())
                .title("Sagrada").notes("Pre-book")
                .landmarkName("Sagrada Família").location("Barcelona")
                .build();
        s.setType(ActivityType.SIGHTSEEING); // IMPORTANT for mapper

        ActivityAdventure a = ActivityAdventure.builder()
                .id(2L).trip(trip).date(LocalDate.now())
                .title("Kayak").notes("Sunscreen")
                .difficultyLevel("LOW").equipmentRequired("Life vest")
                .build();
        a.setType(ActivityType.ADVENTURE); // IMPORTANT for mapper

        when(activityRepository.findAll()).thenReturn(List.of(s, a));

        var list = service.findAll();

        assertEquals(2, list.size());
        assertEquals(ActivityType.SIGHTSEEING, list.get(0).getType());
        assertEquals(ActivityType.ADVENTURE, list.get(1).getType());
        verify(activityRepository).findAll();
    }

    @Test
    void findByIdTest() {
        ActivityCultural c = ActivityCultural.builder()
                .id(5L).trip(trip).date(LocalDate.now())
                .title("Opera").notes("Formal")
                .eventName("La Traviata").organizer("Liceu")
                .build();
        c.setType(ActivityType.CULTURAL); // IMPORTANT

        when(activityRepository.findById(5L)).thenReturn(Optional.of(c));

        var res = service.findById(5L);

        assertEquals(5L, res.getId());
        assertEquals(ActivityType.CULTURAL, res.getType());
        assertEquals("La Traviata", res.getEventName());
        verify(activityRepository).findById(5L);
    }

    @Test
    void findByIdNotFound_Test() {
        when(activityRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ActivityServiceImpl.ResourceNotFoundException.class, () -> service.findById(999L));
        verify(activityRepository).findById(999L);
    }


    @Test
    void updateTest() {
        // existing SIGHTSEEING
        ActivitySightseeing existing = ActivitySightseeing.builder()
                .id(10L).trip(trip).date(LocalDate.of(2025, 7, 10))
                .title("Old").notes("old")
                .landmarkName("Old landmark").location("Old location")
                .build();
        existing.setType(ActivityType.SIGHTSEEING);

        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 12))
                .title("New title")
                .notes("new")
                .type(ActivityType.SIGHTSEEING) // same subtype
                .landmarkName("New landmark")
                .location("New location")
                .build();

        when(activityRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(activityRepository.save(any(Activity.class))).thenAnswer(inv -> inv.getArgument(0));

        var res = service.update(10L, req);

        assertEquals(10L, res.getId());
        assertEquals("New title", res.getTitle());
        assertEquals(ActivityType.SIGHTSEEING, res.getType());
        assertEquals("New landmark", res.getLandmarkName());

        verify(activityRepository).findById(10L);
        verify(tripRepository).findById(1L);
        verify(activityRepository).save(any(Activity.class));
    }

    @Test
    void updateChangeSubtype_Test() {
        ActivityAdventure existing = ActivityAdventure.builder()
                .id(7L).trip(trip).date(LocalDate.now())
                .title("Via Ferrata").notes("old")
                .difficultyLevel("LOW").equipmentRequired("Helmet")
                .build();
        existing.setType(ActivityType.ADVENTURE);

        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.now())
                .title("Try change")
                .notes("no")
                .type(ActivityType.SIGHTSEEING) // different
                .landmarkName("Something")
                .build();

        when(activityRepository.findById(7L)).thenReturn(Optional.of(existing));

        assertThrows(ActivityServiceImpl.BadRequestException.class, () -> service.update(7L, req));
        verify(activityRepository).findById(7L);
        verifyNoInteractions(tripRepository);
    }


    @Test
    void deleteTest() {
        when(activityRepository.existsById(12L)).thenReturn(true);

        service.delete(12L);

        verify(activityRepository).existsById(12L);
        verify(activityRepository).deleteById(12L);
    }

    @Test
    void deleteNotExists_Test() {
        when(activityRepository.existsById(404L)).thenReturn(false);
        assertThrows(ActivityServiceImpl.ResourceNotFoundException.class, () -> service.delete(404L));
        verify(activityRepository).existsById(404L);
        verify(activityRepository, never()).deleteById(anyLong());
    }
}