package br.com.rhitmohospede.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GuestNotFoundException extends APIException {

    public GuestNotFoundException(String message) {
        super(message);
    }
}
