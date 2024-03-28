package org.example.accesscontrolapi.documents;

import lombok.Data;
import org.example.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.example.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.example.accesscontrolapi.models.responsemodels.AccessResponseModel;
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
    private LocalDateTime dateTime;

    private AccessRequestModel accessRequestModel;
    private AccessPolicyModel accessPolicyModel;
    private AccessResponseModel accessResponseModel;

    public AccessLog(AccessRequestModel accessRequestModel, AccessPolicyModel accessPolicyModel, AccessResponseModel accessResponseModel) {
        this.accessRequestModel = accessRequestModel;
        this.accessPolicyModel = accessPolicyModel;
        this.accessResponseModel = accessResponseModel;
    }
}
