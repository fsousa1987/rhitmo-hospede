package br.com.rhitmohospede.service.utils;

import br.com.rhitmohospede.enums.Status;
import br.com.rhitmohospede.exception.InvalidStatusException;

import java.util.Arrays;

public class Validators {

    public static void isStatusProvidedValid(String upperCaseStatus) {
        boolean isStatusValid = Arrays.stream(Status.values()).anyMatch(status -> status.toString().equals(upperCaseStatus));

        if (!isStatusValid) {
            throw new InvalidStatusException(String.format("Status %s is not valid", upperCaseStatus));
        }
    }
}
