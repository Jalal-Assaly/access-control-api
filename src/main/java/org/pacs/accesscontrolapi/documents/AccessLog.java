package org.pacs.accesscontrolapi.documents;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("accessLogs")
public class AccessLog {
    @Transient
    public static final String SEQUENCE_NAME = "access_policies_sequence";

    @Id
    private String id;
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;

    @JsonProperty("accessRequest")
    private AccessRequestModel accessRequestModel;
    @JsonProperty("accessPolicy")
    private AccessPolicyModel accessPolicyModel;
    @JsonProperty("accessResponse")
    private AccessResponseModel accessResponseModel;

    public AccessLog(AccessRequestModel accessRequestModel, AccessPolicyModel accessPolicyModel, AccessResponseModel accessResponseModel) {
        this.accessRequestModel = accessRequestModel;
        this.accessPolicyModel = accessPolicyModel;
        this.accessResponseModel = accessResponseModel;
    }
}
