package br.com.rhitmohospede.exception;

public class ReservationNotFoundException extends APIException {

    public ReservationNotFoundException(String message) {
        super(message);
    }
}