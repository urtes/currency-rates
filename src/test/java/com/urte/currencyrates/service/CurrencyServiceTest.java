package com.urte.currencyrates.service;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ConversionRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {

    CurrencyRepository mockCurrencyRepository = Mockito.mock(CurrencyRepository.class);
    CurrencyClient mockCurrencyClient = Mockito.mock(CurrencyClient.class);
    CurrencyService currencyService = new CurrencyService(mockCurrencyRepository, mockCurrencyClient);

    @Test
    public void testConvert() {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setAmount(2l);
        when(mockCurrencyRepository.findFirstByCodeOrderByDateDesc(conversionRequest.getCode()))
                .thenReturn(new CurrencyByDate(LocalDate.now(), "TEST", new BigDecimal("5")));
        assertEquals(new BigDecimal("5"), currencyService.convert(conversionRequest).getRate());
        assertEquals(new BigDecimal("10"), currencyService.convert(conversionRequest).getCount());
    }
}
