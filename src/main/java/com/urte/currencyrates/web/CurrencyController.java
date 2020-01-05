package com.urte.currencyrates.web;

import com.urte.currencyrates.data.CurrencyRepository;
import com.urte.currencyrates.domain.CurrencyByDate;
import com.urte.currencyrates.service.CurrencyService;
import com.urte.currencyrates.transitional.ConversionRequest;
import com.urte.currencyrates.transitional.ConversionResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Validated
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
        List<CurrencyByDate> currenciesByDate = currencyRepository.getAllForToday();

        if(currenciesByDate == null || currenciesByDate.size() == 0) {
            model.addAttribute("link", "/");
            return "error";
        }

        model.addAttribute("currencies", currenciesByDate);
        return "rates";
    }

    @GetMapping("/history/{code}")
    public String getHistory(@PathVariable String code, Model model) {
        List<CurrencyByDate> currencyHistory = currencyRepository.findAllByCodeOrderByDateDesc(code);

        if(currencyHistory == null || currencyHistory.size() == 0) {
            model.addAttribute("link", String.format("/history/{%s}", code));
            return "error";
        }

        model.addAttribute("code", currencyHistory.get(0).getCode());
        model.addAttribute("history", currencyHistory);
        return "history";
    }

    @GetMapping("/calculator")
    public String getCalculator(Model model) {
        List<String> codes = currencyRepository.getCodes();

        if(codes == null || codes.size() == 0) {
            model.addAttribute("link", "/calculator");
            return "error";
        }

        model.addAttribute("codes", codes);
        model.addAttribute("conversionRequest", new ConversionRequest());
        return "calculator";
    }

    @PostMapping("/calculator")
    public String calculate(@ModelAttribute ConversionRequest conversionRequest,
                            Model model){
        List<String> codes = currencyRepository.getCodes();
        ConversionResult conversionResult = currencyService.convert(conversionRequest);

        if(codes == null || codes.size() == 0 || conversionResult == null) {
            model.addAttribute("link", "/calculator");
            return "error";
        }

        model.addAttribute("codes", codes);
        model.addAttribute("conversionRequest", conversionRequest);
        model.addAttribute("conversionResult", conversionResult);
        return "calculator";
    }
}
