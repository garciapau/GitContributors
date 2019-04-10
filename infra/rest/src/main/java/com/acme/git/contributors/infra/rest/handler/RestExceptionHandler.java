package com.acme.git.contributors.infra.rest.handler;

import com.acme.git.contributors.application.exception.IncorrectValuesException;
import com.acme.git.contributors.infra.rest.model.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IncorrectValuesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleIncorrectValuesException(IncorrectValuesException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildApiErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ApiError> handleRestClientException(HttpClientErrorException.Forbidden ex) {
        ResponseEntity<ApiError> errorResponseEntity = ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiErrorResponse(ex.getResponseBodyAsString()));
        return errorResponseEntity;
    }
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ApiError> handleRestClientException(HttpClientErrorException.NotFound ex) {
        System.out.println("---- Fallback to Cache ----- ");
        ResponseEntity<ApiError> errorResponseEntity = ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiErrorResponse(ex.getResponseBodyAsString()));
        return errorResponseEntity;
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ApiError> handleRestClientException(RestClientException ex) {
        ResponseEntity<ApiError> errorResponseEntity = ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(buildApiErrorResponse(ex.getMessage()));
        return errorResponseEntity;
    }

    private ApiError buildApiErrorResponse(String message) {
        return new ApiError.ApiErrorBuilder()
                .withMessage(message)
                .build();
    }

}
