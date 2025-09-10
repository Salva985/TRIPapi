package com.tripapi.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripapi.controller.ItineraryDayController;
import com.tripapi.dto.ItineraryDay.ItineraryDayRequestDTO;
import com.tripapi.dto.ItineraryDay.ItineraryDayResponseDTO;
import com.tripapi.service.interfaces.ItineraryDayService;
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
class ItineraryDayControllerTest {

    private static final String BASE = "/api/itinerary-days";

    @Mock
    private ItineraryDayService itineraryDayService;

    @InjectMocks
    private ItineraryDayController itineraryDayController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itineraryDayController)
                // .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createTest() throws Exception {
        ItineraryDayRequestDTO req = ItineraryDayRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 2))
                .dayNumber(2)
                .title("Old Town Walk")
                .notes("Gothic Quarter + tapas")
                .build();

        ItineraryDayResponseDTO res = ItineraryDayResponseDTO.builder()
                .id(100L)
                .tripId(1L)
                .tripName("Summer Escape")
                .date(req.getDate())
                .dayNumber(req.getDayNumber())
                .title(req.getTitle())
                .notes(req.getNotes())
                .build();

        when(itineraryDayService.create(any(ItineraryDayRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/100")))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.tripId").value(1))
                .andExpect(jsonPath("$.title").value("Old Town Walk"));

        verify(itineraryDayService, times(1)).create(any(ItineraryDayRequestDTO.class));
    }

    @Test
    void findAllTest() throws Exception {
        ItineraryDayResponseDTO d1 = ItineraryDayResponseDTO.builder()
                .id(1L).tripId(1L).tripName("Summer Escape")
                .date(LocalDate.of(2025, 7, 2)).dayNumber(2).title("Old Town Walk").notes("Notes").build();

        when(itineraryDayService.findAll()).thenReturn(List.of(d1));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].tripName").value("Summer Escape"));

        verify(itineraryDayService, times(1)).findAll();
    }

    @Test
    void findByIdTest() throws Exception {
        ItineraryDayResponseDTO d = ItineraryDayResponseDTO.builder()
                .id(42L).tripId(2L).tripName("Conference Rome")
                .date(LocalDate.of(2025, 9, 6)).dayNumber(2).title("Colosseum").notes("Skip-the-line").build();

        when(itineraryDayService.findById(42L)).thenReturn(d);

        mockMvc.perform(get(BASE + "/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.title").value("Colosseum"));

        verify(itineraryDayService).findById(42L);
    }

    @Test
    void updateTest() throws Exception {
        ItineraryDayRequestDTO req = ItineraryDayRequestDTO.builder()
                .tripId(1L)
                .date(LocalDate.of(2025, 7, 3))
                .dayNumber(3)
                .title("Beach Day")
                .notes("Barceloneta in the morning")
                .build();

        ItineraryDayResponseDTO res = ItineraryDayResponseDTO.builder()
                .id(5L).tripId(1L).tripName("Summer Escape")
                .date(req.getDate()).dayNumber(req.getDayNumber())
                .title(req.getTitle()).notes(req.getNotes())
                .build();

        when(itineraryDayService.update(eq(5L), any(ItineraryDayRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put(BASE + "/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Beach Day"));

        verify(itineraryDayService).update(eq(5L), any(ItineraryDayRequestDTO.class));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(itineraryDayService).delete(9L);

        mockMvc.perform(delete(BASE + "/{id}", 9L))
                .andExpect(status().isNoContent());

        verify(itineraryDayService).delete(9L);
    }
}