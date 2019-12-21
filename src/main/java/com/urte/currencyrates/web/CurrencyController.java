package com.urte.currencyrates.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CurrencyController {

    @GetMapping("/rates")
    public String getRates() {
        return "rates";
    }
}
