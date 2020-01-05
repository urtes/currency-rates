package com.urte.currencyrates.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CurrencyRepository mockCurrencyRepository;

    @MockBean
    CurrencyService mockCurrencyService;

    @Test
    public void testGetRates() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currencies"))
                .andExpect(view().name("rates"));
        verify(mockCurrencyRepository, times(1)).getAllForToday();
    }

    @Test
    public void testGetHistory() throws Exception {
        List<CurrencyByDate> currenciesByDate = new ArrayList<>(Arrays.asList(
                new CurrencyByDate(LocalDate.now(), "TEST1", new BigDecimal("1")),
                new CurrencyByDate(LocalDate.now(), "TEST2", new BigDecimal("2"))
        ));

        doReturn(currenciesByDate).when(mockCurrencyRepository).findAllByCodeOrderByDateDesc(anyString());

        mockMvc.perform(get("/history/AUD"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("code", "history"))
                .andExpect(view().name("history"));
        verify(mockCurrencyRepository,
                times(1)).findAllByCodeOrderByDateDesc(anyString());
    }

    @Test
    public void testGetCalculator() throws Exception {
        mockMvc.perform(get("/calculator"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "codes",
                        "conversionRequest"))
                .andExpect(view().name("calculator"));
        verify(mockCurrencyRepository, times(1)).getCodes();
    }
}
