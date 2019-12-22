package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CurrencyController {

    private CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @GetMapping("/")
    public String getRates(Model model) {
        Iterable<CurrencyByDate> currenciesByDate = currencyRepository.selectAllForToday();
        model.addAttribute("currencies", currenciesByDate);
        return "rates";
    }

    @GetMapping("/history/{code}")
    public String getHistory(@PathVariable String code, Model model) {
        Iterable<CurrencyByDate> currencyHistory = currencyRepository.findAllByCodeOrderByDateDesc(code);
        model.addAttribute("history", currencyHistory);
        return "history";
    }

    @GetMapping("/counter")
    public String getCounter(Model model) {
        return "counter";
    }
}
