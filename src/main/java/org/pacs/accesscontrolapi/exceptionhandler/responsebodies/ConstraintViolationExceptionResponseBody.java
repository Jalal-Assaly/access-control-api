package org.pacs.accesscontrolapi.exceptionhandler.responsebodies;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstraintViolationExceptionResponseBody {

    @JsonProperty("timestamp")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime dateTime;
    @JsonProperty("status")
    private final Integer status;
    @JsonProperty("message")
    private String errorMessage;


    public ConstraintViolationExceptionResponseBody(HttpStatusCode status, ConstraintViolationException exception) {
        this.dateTime = LocalDateTime.now();
        this.status = status.value();
        this.errorMessage = "Some attributes are missing or undefined";
    }
}