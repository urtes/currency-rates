package com.urte.currencyrates.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.transitional.ConversionRequest;
import com.urte.currencyrates.transitional.ConversionResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
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
        mockMvc.perform(get("/history/AUD"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("history"))
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

    @Test
    public void testCalculate() throws Exception {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setCode("TEST");
        conversionRequest.setAmount(new BigDecimal("5"));
        ConversionResult conversionResult = new ConversionResult(new BigDecimal("2"), new BigDecimal("10"));

        when(mockCurrencyService.convert(any(ConversionRequest.class)))
                .thenReturn(conversionResult);

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
