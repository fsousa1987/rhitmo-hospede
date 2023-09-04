package br.com.rhitmohospede.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RoomNotFoundException extends APIException {

    public RoomNotFoundException(String message) {
        super(message);
    }
}
