package com.urte.currencyrates.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.transitional.ConversionRequest;
import com.urte.currencyrates.transitional.ConversionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    List<CurrencyByDate> currenciesByDate = new ArrayList<>(Arrays.asList(
            new CurrencyByDate(LocalDate.now(), "TEST1", new BigDecimal("1")),
            new CurrencyByDate(LocalDate.now(), "TEST2", new BigDecimal("2"))
    ));

    List<String> codes = new ArrayList<>(Arrays.asList("TEST1", "TEST2"));

    @Test
    public void testGetRates() throws Exception {
        doReturn(currenciesByDate).when(mockCurrencyRepository).getAllForToday();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currencies"))
                .andExpect(view().name("rates"));
        verify(mockCurrencyRepository, times(1)).getAllForToday();
    }

    @Test
    public void testGetHistory() throws Exception {
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
        doReturn(codes).when(mockCurrencyRepository).getCodes();

        mockMvc.perform(get("/calculator"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "codes",
                        "conversionRequest"))
                .andExpect(view().name("calculator"));
        verify(mockCurrencyRepository, times(1)).getCodes();
    }

    @Test
    public void testCalculate() throws Exception {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setCode("TEST");
        conversionRequest.setAmount(new BigDecimal("5"));
        ConversionResult conversionResult = new ConversionResult(new BigDecimal("2"), new BigDecimal("10"));

        doReturn(codes).when(mockCurrencyRepository).getCodes();
        doReturn(conversionResult).when(mockCurrencyService).convert(any(ConversionRequest.class));

        mockMvc.perform(post("/calculator")
                .content(objectMapper.writeValueAsString(conversionRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists(
                        "codes",
                        "conversionRequest",
                        "conversionResult"))
                .andExpect(view().name("calculator"));
        verify(mockCurrencyRepository, times(1)).getCodes();
        verify(mockCurrencyService, times(1)).convert(any(ConversionRequest.class));
    }
}
