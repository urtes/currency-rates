package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.transitional.ResultRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CurrencyController {

    private CurrencyRepository currencyRepository;

    public CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
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

    @GetMapping("/counter")
    public String getCounter(Model model) {
        model.addAttribute("codes", currencyRepository.getCodes());
        model.addAttribute("resultRequest", new ResultRequest());
        return "counter";
    }

    @PostMapping("/counter")
    public String count(@ModelAttribute ResultRequest resultRequest){
        ResultRequest request = resultRequest;
        return "counter";
    }
}
