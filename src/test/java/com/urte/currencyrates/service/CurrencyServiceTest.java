package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ConversionRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurrencyServiceTest {

    CurrencyRepository mockCurrencyRepository = Mockito.mock(CurrencyRepository.class);
    CurrencyClient mockCurrencyClient = Mockito.mock(CurrencyClient.class);
    CurrencyService currencyService = new CurrencyService(mockCurrencyRepository, mockCurrencyClient);
    List<CurrencyByDate> currenciesByDate = new ArrayList<>(Arrays.asList(
            new CurrencyByDate(LocalDate.now(), "TEST1", new BigDecimal("1")),
            new CurrencyByDate(LocalDate.now(), "TEST2", new BigDecimal("2"))
    ));

    @Test
    public void testConvert() {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setAmount(2l);

        when(mockCurrencyRepository.findFirstByCodeOrderByDateDesc(conversionRequest.getCode()))
                .thenReturn(new CurrencyByDate(LocalDate.now(), "TEST", new BigDecimal("5")));
        assertEquals(new BigDecimal("5"), currencyService.convert(conversionRequest).getRate());
        assertEquals(new BigDecimal("10"), currencyService.convert(conversionRequest).getCount());
    }

    @Test
    public void testGetCurrenciesForPeriod() {
        currencyService.period = 2;
        currencyService.getCurrenciesForPeriod();

        when(mockCurrencyClient.getCurrenciesByDate(any(LocalDate.class))).thenReturn(currenciesByDate);
        verify(mockCurrencyClient, times(2)).getCurrenciesByDate(any(LocalDate.class));
        assertEquals(4, currencyService.getCurrenciesForPeriod().size());
        assertEquals(new BigDecimal("1"), currencyService.getCurrenciesForPeriod().get(0).getRate());
        assertEquals(new BigDecimal("2"), currencyService.getCurrenciesForPeriod().get(3).getRate());
        assertEquals("TEST1", currencyService.getCurrenciesForPeriod().get(0).getCode());
        assertEquals("TEST2", currencyService.getCurrenciesForPeriod().get(3).getCode());
    }

    @Test
    public void testSave() {
        currencyService.save(currenciesByDate);

        verify(mockCurrencyRepository, times(2)).save(any(CurrencyByDate.class));
    }
}
