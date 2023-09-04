package br.com.rhitmohospede.service.utils;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.exception.InvalidDateException;
import br.com.rhitmohospede.exception.InvalidStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Validators {

    public static void isStatusProvidedValid(String upperCaseStatus) {
        boolean isStatusValid = Arrays.stream(Status.values()).anyMatch(status -> status.toString().equals(upperCaseStatus));

        if (!isStatusValid) {
            throw new InvalidStatusException(String.format("Status %s is not valid", upperCaseStatus));
        }
    }

    public static Map<String, String> createMapOfDates(String initialDate, String finalDate) {
        Map<String, String> dates = new HashMap<>();
        dates.put("initialDate", initialDate);
        dates.put("finalDate", finalDate);
        return dates;
    }

    public static void verifyMapHasValidDates(Map<String, String> dates) {
        dates.forEach((key, value) -> validateDateProvided(value));
    }

    public static void validateDateProvided(String dateValue) {
        String datePattern = "yyyy-MM-dd";

        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

        try {
            sdf.setLenient(false);
            sdf.parse(dateValue);
        } catch (ParseException e) {
            throw new InvalidDateException("Invalid date param or date field provided");
        }
    }
}
