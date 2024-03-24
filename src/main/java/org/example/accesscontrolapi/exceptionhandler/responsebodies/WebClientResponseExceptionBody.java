package org.example.accesscontrolapi.exceptionhandler.responsebodies;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebClientResponseExceptionBody {
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private String dateTime;
    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String errorMessages;

    @JsonIgnore
    String exception;

    public WebClientResponseExceptionBody(String exception) {
        this.exception = exception;
        formatException(this.exception);
    }

    private void formatException(String exception) {
        String exceptionFormatted = exception.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = exceptionFormatted.split(",");
        this.dateTime = exceptionFormatted.substring(10, 29);
        this.status = parts[1].split(":")[1].trim();
        this.errorMessages = parts[2].split(":")[1].trim();
    }
}
