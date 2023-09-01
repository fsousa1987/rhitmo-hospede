package br.com.rhitmohospede.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IntegrationException extends APIException {

    public IntegrationException(String message) {
        super(message);
    }
}
