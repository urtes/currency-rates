package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrencyController {

    private CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/rates")
    public String getRates(Model model) {
        Iterable<CurrencyByDate> currenciesByDate = currencyRepository.findAll();
        model.addAttribute("currencies", currenciesByDate);
        return "rates";
    }
}
