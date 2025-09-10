package com.tripapi.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripapi.controller.ActivityController;
import com.tripapi.dto.Activity.ActivityRequestDTO;
import com.tripapi.dto.Activity.ActivityResponseDTO;
import com.tripapi.enums.ActivityType;
import com.tripapi.service.interfaces.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    private static final String BASE = "/api/activities";

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityController activityController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(activityController)
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    // ---------- CREATE - one per subtype ----------

    @Test
    void createSightseeingTest() throws Exception {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 14))
                .title("Park Güell Tour")
                .notes("Buy tickets online")
                .type(ActivityType.SIGHTSEEING)
                .landmarkName("Park Güell")
                .location("Barcelona")
                .build();

        ActivityResponseDTO res = ActivityResponseDTO.builder()
                .id(10L)
                .tripId(1L)
                .tripName("Summer Escape")
                .date(req.getDate())
                .title(req.getTitle())
                .notes(req.getNotes())
                .type(ActivityType.SIGHTSEEING)
                .landmarkName("Park Güell")
                .location("Barcelona")
                .build();

        when(activityService.create(any(ActivityRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/10")))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.type").value("SIGHTSEEING"))
                .andExpect(jsonPath("$.landmarkName").value("Park Güell"))
                .andExpect(jsonPath("$.location").value("Barcelona"));

        verify(activityService, times(1)).create(any(ActivityRequestDTO.class));
    }

    @Test
    void createAdventureTest() throws Exception {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 16))
                .title("Via Ferrata")
                .notes("Bring gloves")
                .type(ActivityType.ADVENTURE)
                .difficultyLevel("MEDIUM")
                .equipmentRequired("Helmet")
                .build();

        ActivityResponseDTO res = ActivityResponseDTO.builder()
                .id(11L)
                .tripId(1L)
                .tripName("Summer Escape")
                .date(req.getDate())
                .title(req.getTitle())
                .notes(req.getNotes())
                .type(ActivityType.ADVENTURE)
                .difficultyLevel("MEDIUM")
                .equipmentRequired("Helmet")
                .build();

        when(activityService.create(any(ActivityRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/11")))
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.type").value("ADVENTURE"))
                .andExpect(jsonPath("$.difficultyLevel").value("MEDIUM"))
                .andExpect(jsonPath("$.equipmentRequired").value("Helmet"));

        verify(activityService, times(1)).create(any(ActivityRequestDTO.class));
    }

    @Test
    void createCulturalTest() throws Exception {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 18))
                .title("Opera Night")
                .notes("Black tie optional")
                .type(ActivityType.CULTURAL)
                .eventName("La Traviata")
                .organizer("Gran Teatre del Liceu")
                .build();

        ActivityResponseDTO res = ActivityResponseDTO.builder()
                .id(12L)
                .tripId(1L)
                .tripName("Summer Escape")
                .date(req.getDate())
                .title(req.getTitle())
                .notes(req.getNotes())
                .type(ActivityType.CULTURAL)
                .eventName("La Traviata")
                .organizer("Gran Teatre del Liceu")
                .build();

        when(activityService.create(any(ActivityRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/12")))
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.type").value("CULTURAL"))
                .andExpect(jsonPath("$.eventName").value("La Traviata"))
                .andExpect(jsonPath("$.organizer").value("Gran Teatre del Liceu"));

        verify(activityService, times(1)).create(any(ActivityRequestDTO.class));
    }

    @Test
    void findAllTest() throws Exception {
        ActivityResponseDTO s = ActivityResponseDTO.builder()
                .id(1L).tripId(1L).tripName("Summer Escape")
                .date(LocalDate.of(2025, 7, 4))
                .title("Sagrada Família Tour")
                .notes("Pre-book tickets")
                .type(ActivityType.SIGHTSEEING)
                .landmarkName("Sagrada Família")
                .location("Barcelona")
                .build();

        ActivityResponseDTO a = ActivityResponseDTO.builder()
                .id(2L).tripId(1L).tripName("Summer Escape")
                .date(LocalDate.of(2025, 7, 6))
                .title("Kayak in Costa Brava")
                .notes("Bring sunscreen")
                .type(ActivityType.ADVENTURE)
                .difficultyLevel("LOW")
                .equipmentRequired("Life vest")
                .build();

        when(activityService.findAll()).thenReturn(List.of(s, a));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].type").value("SIGHTSEEING"))
                .andExpect(jsonPath("$[1].type").value("ADVENTURE"));

        verify(activityService).findAll();
    }

    @Test
    void findByIdTest() throws Exception {
        ActivityResponseDTO res = ActivityResponseDTO.builder()
                .id(2L).tripId(1L).tripName("Summer Escape")
                .date(LocalDate.of(2025, 7, 6))
                .title("Kayak in Costa Brava")
                .notes("Bring sunscreen")
                .type(ActivityType.ADVENTURE)
                .difficultyLevel("LOW")
                .equipmentRequired("Life vest")
                .build();

        when(activityService.findById(2L)).thenReturn(res);

        mockMvc.perform(get(BASE + "/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.type").value("ADVENTURE"))
                .andExpect(jsonPath("$.equipmentRequired").value("Life vest"));

        verify(activityService).findById(2L);
    }

    @Test
    void updateTest() throws Exception {
        ActivityRequestDTO req = ActivityRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 18))
                .title("Opera Night (Updated)")
                .notes("Dress code: formal")
                .type(ActivityType.CULTURAL) // type must match existing subtype
                .eventName("La Traviata")
                .organizer("Gran Teatre del Liceu")
                .build();

        ActivityResponseDTO res = ActivityResponseDTO.builder()
                .id(12L)
                .tripId(1L)
                .tripName("Summer Escape")
                .date(req.getDate())
                .title(req.getTitle())
                .notes(req.getNotes())
                .type(ActivityType.CULTURAL)
                .eventName("La Traviata")
                .organizer("Gran Teatre del Liceu")
                .build();

        when(activityService.update(eq(12L), any(ActivityRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put(BASE + "/{id}", 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.title").value("Opera Night (Updated)"))
                .andExpect(jsonPath("$.type").value("CULTURAL"));

        verify(activityService).update(eq(12L), any(ActivityRequestDTO.class));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(activityService).delete(99L);

        mockMvc.perform(delete(BASE + "/{id}", 99L))
                .andExpect(status().isNoContent());

        verify(activityService).delete(99L);
    }
}