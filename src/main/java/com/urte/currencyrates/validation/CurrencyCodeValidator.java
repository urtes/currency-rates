package com.urte.currencyrates.validation;

import com.urte.currencyrates.data.CurrencyRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyCodeValidator implements ConstraintValidator<CurrencyCodeConstraint, String> {

    CurrencyRepository currencyRepository;

    public CurrencyCodeValidator(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        return currencyRepository.existsByCode(code);
    }
}
