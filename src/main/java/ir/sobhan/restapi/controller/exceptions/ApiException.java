package ir.sobhan.restapi.controller.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime tinestamp;

    public ApiException(String message, Throwable throwable, HttpStatus httpStatus, ZonedDateTime tinestamp) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.tinestamp = tinestamp;
    }
}
