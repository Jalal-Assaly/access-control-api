package org.example.accesscontrolapi.exceptionhandler.responsebodies;


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
    private  String dateTime;
    @JsonProperty("status")
    private  String status;
    @JsonProperty("message")
    private  String errorMessages;

    @JsonIgnore
    String exception;

    public WebClientResponseExceptionBody(String exception){
        this.exception = exception;
        formatException(this.exception);
    }

    private void formatException(String exception){

        String exceptionFormated = exception.replace("{", "").replace("}", "").replace("\"", "");
        String[] parts = exceptionFormated.split(",");
        this.dateTime= exceptionFormated.substring(10,29);
        this.status = parts[1].split(":")[1].trim();
        this.errorMessages = parts[2].split(":")[1].trim();

    }




}
