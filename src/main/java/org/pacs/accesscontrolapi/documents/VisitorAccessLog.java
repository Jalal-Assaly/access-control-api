package org.pacs.accesscontrolapi.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("visitorAccessLogs")
@RequiredArgsConstructor
public class VisitorAccessLog {
    @Transient
    public static final String SEQUENCE_NAME = "visitor_logs_sequence";

    @Id
    private String id;
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;

    @JsonProperty("accessRequest")
    private final VisitorAccessRequestModel accessRequestModel;
    @JsonProperty("accessPolicy")
    private final AccessPolicyModel accessPolicyModel;
    @JsonProperty("accessResponse")
    private final AccessResponseModel accessResponseModel;
}
