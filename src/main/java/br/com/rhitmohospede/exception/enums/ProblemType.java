package br.com.rhitmohospede.exception.enums;

import lombok.Getter;

@Getter
public enum ProblemType {

    RESOURCE_NOT_FOUND("Resource not found"),
    INVALID_STATUS_PROVIDED("Invalid status provided"),
    INVALID_DATA("Invalid data"),
    BUSINESS_ERROR("Business rule violation"),
    INVALID_DATE("Invalid date"),
    ROOM_ALREADY_REGISTERED("Room already registered"),
    INCOMPREHENSIBLE_MESSAGE("Incomprehensible message"),
    INVALID_PARAMETER("Invalid parameter");

    private final String title;

    ProblemType(String title) {
        this.title = title;
    }
}
