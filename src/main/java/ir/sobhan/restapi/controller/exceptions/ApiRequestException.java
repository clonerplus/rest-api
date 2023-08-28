package ir.sobhan.restapi.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiRequestException extends RuntimeException {
    private final HttpStatus httpStatus;
    public ApiRequestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
    public ApiRequestException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
    public HttpStatus getStatus() {
        return httpStatus;
    }
    public ResponseEntity<String> getResponseEntity() {
        return ResponseEntity.status(this.getStatus()).body(this.getMessage());
    }
}
