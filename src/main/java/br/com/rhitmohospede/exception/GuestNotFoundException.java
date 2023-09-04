package br.com.rhitmohospede.exception;

public class GuestNotFoundException extends APIException {

    public GuestNotFoundException(String message) {
        super(message);
    }
}
