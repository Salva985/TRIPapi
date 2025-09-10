package com.tripapi.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tripapi.controller.BudgetController;
import com.tripapi.dto.Budget.BudgetRequestDTO;
import com.tripapi.dto.Budget.BudgetResponseDTO;
import com.tripapi.enums.BudgetCategory;
import com.tripapi.enums.CurrencyCode;
import com.tripapi.service.interfaces.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    private static final String BASE = "/api/budgets";

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(budgetController)
                .build();
        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createTest() throws Exception {
        BudgetRequestDTO req = BudgetRequestDTO.builder()
                .tripId(1L)
                .plannedAmount(new BigDecimal("500.00"))
                .spentAmount(new BigDecimal("120.50"))
                .currencyCode(CurrencyCode.EUR)
                .category(BudgetCategory.FOOD)
                .notes("Meals for first week")
                .build();

        BudgetResponseDTO res = BudgetResponseDTO.builder()
                .id(100L)
                .tripId(1L)
                .tripName("Summer Escape")
                .plannedAmount(new BigDecimal("500.00"))
                .spentAmount(new BigDecimal("120.50"))
                .currencyCode(CurrencyCode.EUR)
                .category(BudgetCategory.FOOD)
                .notes("Meals for first week")
                .build();

        when(budgetService.create(any(BudgetRequestDTO.class))).thenReturn(res);

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(BASE + "/100")))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.tripId").value(1))
                .andExpect(jsonPath("$.currencyCode").value("EUR"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.plannedAmount").value(500.00))
                .andExpect(jsonPath("$.spentAmount").value(120.50));

        verify(budgetService, times(1)).create(any(BudgetRequestDTO.class));
    }

    @Test
    void findAllTest() throws Exception {
        BudgetResponseDTO one = BudgetResponseDTO.builder()
                .id(1L).tripId(1L).tripName("Summer Escape")
                .plannedAmount(new BigDecimal("800.00"))
                .spentAmount(new BigDecimal("760.45"))
                .currencyCode(CurrencyCode.EUR)
                .category(BudgetCategory.TRANSPORT)
                .notes("Flights")
                .build();

        when(budgetService.findAll()).thenReturn(List.of(one));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].tripName").value("Summer Escape"))
                .andExpect(jsonPath("$[0].category").value("TRANSPORT"));

        verify(budgetService).findAll();
    }

    @Test
    void findByIdTest() throws Exception {
        BudgetResponseDTO res = BudgetResponseDTO.builder()
                .id(5L).tripId(1L).tripName("Summer Escape")
                .plannedAmount(new BigDecimal("350.00"))
                .spentAmount(new BigDecimal("90.00"))
                .currencyCode(CurrencyCode.EUR)
                .category(BudgetCategory.FOOD)
                .notes("Tapas budget")
                .build();

        when(budgetService.findById(5L)).thenReturn(res);

        mockMvc.perform(get(BASE + "/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.plannedAmount").value(350.00));

        verify(budgetService).findById(5L);
    }

    @Test
    void updateTest() throws Exception {
        BudgetRequestDTO req = BudgetRequestDTO.builder()
                .tripId(2L)
                .plannedAmount(new BigDecimal("1000.00"))
                .spentAmount(new BigDecimal("200.00"))
                .currencyCode(CurrencyCode.USD)
                .category(BudgetCategory.ACCOMMODATION)
                .notes("Hotel in NYC")
                .build();

        BudgetResponseDTO res = BudgetResponseDTO.builder()
                .id(7L).tripId(2L).tripName("Conference NYC")
                .plannedAmount(new BigDecimal("1000.00"))
                .spentAmount(new BigDecimal("200.00"))
                .currencyCode(CurrencyCode.USD)
                .category(BudgetCategory.ACCOMMODATION)
                .notes("Hotel in NYC")
                .build();

        when(budgetService.update(eq(7L), any(BudgetRequestDTO.class))).thenReturn(res);

        mockMvc.perform(put(BASE + "/{id}", 7L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.tripId").value(2))
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.category").value("ACCOMMODATION"));

        verify(budgetService).update(eq(7L), any(BudgetRequestDTO.class));
    }

    @Test
    void deleteTest() throws Exception {
        doNothing().when(budgetService).delete(9L);

        mockMvc.perform(delete(BASE + "/{id}", 9L))
                .andExpect(status().isNoContent());

        verify(budgetService).delete(9L);
    }
}