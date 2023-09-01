package br.com.rhitmohospede.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class CustomDateValidator implements ConstraintValidator<CustomDateConstraint, LocalDate> {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public void initialize(CustomDateConstraint customDate) {
        ConstraintValidator.super.initialize(customDate);
    }

    @Override
    public boolean isValid(LocalDate customDateField, ConstraintValidatorContext ctx) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            sdf.setLenient(false);
            sdf.parse(customDateField.toString());
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
