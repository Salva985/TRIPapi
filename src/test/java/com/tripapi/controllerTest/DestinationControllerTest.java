package com.tripapi.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripapi.controller.DestinationController;
import com.tripapi.dto.Destination.DestinationRequestDTO;
import com.tripapi.dto.Destination.DestinationResponseDTO;
import com.tripapi.enums.CurrencyCode;
import com.tripapi.service.interfaces.DestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DestinationControllerTest {

    private static final String BASE = "/api/destinations";

    @Mock
    private DestinationService destinationService;

    @InjectMocks
    private DestinationController destinationController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(destinationController)
                // .setControllerAdvice(new GlobalExceptionHandler()) // if you have one
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createTest() throws Exception {
        DestinationRequestDTO req = DestinationRequestDTO.builder()
                .city("Barcelona")
                .country("Spain")
                .timezone("Europe/Madrid")
                .currencyCode(CurrencyCode.EUR)
                .build();

        DestinationResponseDTO res = DestinationResponseDTO.builder()
                .id(1L)
                .city("Barcelona")
                .country("Spain")
                .timezone("Europe/Madrid")
                .currencyCode(CurrencyCode.EUR)
                .build();

        when(destinationService.create(any(DestinationRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.city").value("Barcelona"))
                .andExpect(jsonPath("$.currencyCode").value("EUR"));

        verify(destinationService, times(1)).create(any(DestinationRequestDTO.class));
    }

    @Test
    void findAllTest() throws Exception {
        DestinationResponseDTO res = DestinationResponseDTO.builder()
                .id(1L).city("Rome").country("Italy").timezone("Europe/Rome").currencyCode(CurrencyCode.EUR).build();

        when(destinationService.findAll()).thenReturn(List.of(res));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].city").value("Rome"));

        verify(destinationService, times(1)).findAll();
    }

    @Test
    void findByIdTest() throws Exception {
        DestinationResponseDTO res = DestinationResponseDTO.builder()
                .id(42L).city("London").country("UK").timezone("Europe/London").currencyCode(CurrencyCode.GBP).build();

        when(destinationService.findById(42L)).thenReturn(res);

        mockMvc.perform(get(BASE + "/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.currencyCode").value("GBP"));

        verify(destinationService).findById(42L);
    }

    @Test
    void updateTest() throws Exception {
        DestinationRequestDTO req = DestinationRequestDTO.builder()
                .city("Paris").country("France").timezone("Europe/Paris").currencyCode(CurrencyCode.EUR).build();

        DestinationResponseDTO res = DestinationResponseDTO.builder()
                .id(3L).city("Paris").country("France").timezone("Europe/Paris").currencyCode(CurrencyCode.EUR).build();

        when(destinationService.update(eq(3L), any(DestinationRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put(BASE + "/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.city").value("Paris"));

        verify(destinationService).update(eq(3L), any(DestinationRequestDTO.class));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(destinationService).delete(9L);

        mockMvc.perform(delete(BASE + "/{id}", 9L))
                .andExpect(status().isNoContent());

        verify(destinationService).delete(9L);
    }
}