package br.com.rhitmohospede.enums;

public enum ErrorMessages {

    NO_GUEST_FOUND_MESSAGE("No guests were found in the database."),
    AN_UNKNOWN_ERROR_MESSAGE("An unknown error occurred while saving guest."),
    GUEST_ALREADY_REGISTERED_MESSAGE("Guest already registered."),
    GUEST_NOT_FOUND("Guest not found");

    ErrorMessages(String errorMessage) {

    }
}
