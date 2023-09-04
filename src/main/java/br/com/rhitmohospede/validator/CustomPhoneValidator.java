package br.com.rhitmohospede.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomPhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public void initialize(Phone customPhone) {
        ConstraintValidator.super.initialize(customPhone);
    }

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^(\\d{2})\\D*(\\d{5}|\\d{4})\\D*(\\d{4})$";

        if (phoneField == null) {
            return false;
        }

        return phoneField.matches(regex);
    }
}
