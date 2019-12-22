package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.transitional.ResultRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
public class CurrencyController {

    private CurrencyRepository currencyRepository;
    private CurrencyService currencyService;

    public CurrencyController(CurrencyRepository currencyRepository,
                              CurrencyService currencyService) {
        this.currencyRepository = currencyRepository;
        this.currencyService = currencyService;
    }

    @GetMapping("/")
    public String getRates(Model model) {
        Iterable<CurrencyByDate> currenciesByDate = currencyRepository.getAllForToday();
        model.addAttribute("currencies", currenciesByDate);
        return "rates";
    }

    @GetMapping("/history/{code}")
    public String getHistory(@PathVariable String code, Model model) {
        Iterable<CurrencyByDate> currencyHistory = currencyRepository.findAllByCodeOrderByDateDesc(code);
        model.addAttribute("history", currencyHistory);
        return "history";
    }

    @GetMapping("/calculator")
    public String getCalculator(Model model) {
        model.addAttribute("codes", currencyRepository.getCodes());
        model.addAttribute("resultRequest", new ResultRequest());
        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculate(@ModelAttribute ResultRequest resultRequest,
                            Model model){
        model.addAttribute("codes", currencyRepository.getCodes());
        BigDecimal result = currencyService.calculate(resultRequest);
        model.addAttribute("result", result);
        return "calculator";
    }
}
