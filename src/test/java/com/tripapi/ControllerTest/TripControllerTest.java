package com.tripapi.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripapi.controller.TripController;
import com.tripapi.dto.Trip.TripRequestDTO;
import com.tripapi.dto.Trip.TripResponseDTO;
import com.tripapi.enums.TripType;
import com.tripapi.service.interfaces.TripService;
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
class TripControllerTest {

    private static final String BASE = "/api/trips";

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(tripController)
                // .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // for LocalDate
    }

    @Test
    void createTest() throws Exception {
        TripRequestDTO req = TripRequestDTO.builder()
                .name("Summer Escape")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 10))
                .destinationId(1L)
                .tripType(TripType.LEISURE)
                .notes("Family trip")
                .build();

        TripResponseDTO res = TripResponseDTO.builder()
                .id(100L)
                .name("Summer Escape")
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 10))
                .destinationId(1L)
                .destinationCity("Barcelona")   // adapt to your DTO fields
                .tripType(TripType.LEISURE)
                .notes("Family trip")
                .build();

        when(tripService.create(any(TripRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/100")))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("Summer Escape"))
                .andExpect(jsonPath("$.tripType").value("LEISURE"));

        verify(tripService, times(1)).create(any(TripRequestDTO.class));
    }

    @Test
    void findAllTest() throws Exception {
        TripResponseDTO t1 = TripResponseDTO.builder()
                .id(1L).name("Rome Conf")
                .startDate(LocalDate.of(2025, 9, 5))
                .endDate(LocalDate.of(2025, 9, 8))
                .destinationId(2L)
                .destinationCity("Rome")
                .tripType(TripType.BUSINESS)
                .build();

        when(tripService.findAll()).thenReturn(List.of(t1));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].destinationCity").value("Rome"));

        verify(tripService).findAll();
    }

    @Test
    void findByIdTest() throws Exception {
        TripResponseDTO res = TripResponseDTO.builder()
                .id(42L).name("City Break")
                .startDate(LocalDate.of(2025, 3, 1))
                .endDate(LocalDate.of(2025, 3, 4))
                .destinationId(5L)
                .destinationCity("London")
                .tripType(TripType.LEISURE)
                .build();

        when(tripService.findById(42L)).thenReturn(res);

        mockMvc.perform(get(BASE + "/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.destinationCity").value("London"));

        verify(tripService).findById(42L);
    }

    @Test
    void updateTest() throws Exception {
        TripRequestDTO req = TripRequestDTO.builder()
                .name("Paris Getaway")
                .startDate(LocalDate.of(2025, 5, 10))
                .endDate(LocalDate.of(2025, 5, 15))
                .destinationId(3L)
                .tripType(TripType.LEISURE)
                .notes("Updated notes")
                .build();

        TripResponseDTO res = TripResponseDTO.builder()
                .id(7L)
                .name("Paris Getaway")
                .startDate(LocalDate.of(2025, 5, 10))
                .endDate(LocalDate.of(2025, 5, 15))
                .destinationId(3L)
                .destinationCity("Paris")
                .tripType(TripType.LEISURE)
                .notes("Updated notes")
                .build();

        when(tripService.update(eq(7L), any(TripRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put(BASE + "/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.destinationCity").value("Paris"));

        verify(tripService).update(eq(7L), any(TripRequestDTO.class));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(tripService).delete(9L);

        mockMvc.perform(delete(BASE + "/{id}", 9L))
                .andExpect(status().isNoContent());   // requires @ResponseStatus(NO_CONTENT) or ResponseEntity.noContent()

        verify(tripService).delete(9L);
    }
}