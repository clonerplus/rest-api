package ir.sobhan.restapi.controller.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // 1. Create payload containing exception details
        HttpStatus httpStatus = e.getStatus();
        ApiException apiException = new ApiException (e.getMessage(), e, httpStatus,
                ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        // 2. Return response entity
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
