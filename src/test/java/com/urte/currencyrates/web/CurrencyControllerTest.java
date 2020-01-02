package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CurrencyControllerTest {

    @Autowired
    MockMvc mockMvc;

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
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        mockMvc.perform(get("/history/AUD"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("history"))
                .andExpect(view().name("history"));
        verify(mockCurrencyRepository, times(1)).findAllByCodeOrderByDateDesc(codeCaptor.capture());
        assertEquals("AUD", codeCaptor.getValue());
    }
}
