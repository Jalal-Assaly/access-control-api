package org.pacs.accesscontrolapi.exceptionhandler;

import org.pacs.accesscontrolapi.exceptionhandler.customexceptions.AttributesMismatchException;
import org.pacs.accesscontrolapi.exceptionhandler.responsebodies.AttributesMismatchExceptionBody;
import org.pacs.accesscontrolapi.exceptionhandler.responsebodies.WebClientResponseExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<WebClientResponseExceptionBody>
    handleWebClientResponseException(WebClientResponseException exception){

        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        WebClientResponseExceptionBody body =
                new WebClientResponseExceptionBody(exception.getResponseBodyAsString());

        return new ResponseEntity<>(body,status);
    }

    @ExceptionHandler(AttributesMismatchException.class)
    public ResponseEntity<AttributesMismatchExceptionBody> handleEntityNotFoundException(AttributesMismatchException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        AttributesMismatchExceptionBody entityNotFoundExceptionResponseBody = new AttributesMismatchExceptionBody(status, exception);
        return new ResponseEntity<>(entityNotFoundExceptionResponseBody, status);
    }
}
