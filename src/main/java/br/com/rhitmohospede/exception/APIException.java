package br.com.rhitmohospede.exception;

import lombok.Getter;

@Getter
public abstract class APIException extends RuntimeException {

    protected APIException(String message) {
        super(message);
    }

    protected APIException(Throwable cause, String message) {
        super(message, cause);
    }
}
