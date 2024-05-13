package org.pacs.accesscontrolapi.models.logmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorHistoryModel {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("LC")
    private String location;
    @JsonProperty("TA")
    private String timeAccess;
    @JsonProperty("DS")
    private String decision;
}
