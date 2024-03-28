package org.pacs.accesscontrolapi.services;

import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.AccessLog;
import org.pacs.accesscontrolapi.documents.DatabaseSequence;
import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessRequestModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.pacs.accesscontrolapi.repositories.AccessLogRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final MongoOperations mongoOperations;
    private final AccessLogRepository accessLogRepository;

    public List<AccessLog> getAllAccessLogs() {
        return accessLogRepository.findAll();
    }

    public void logAccess(AccessRequestModel accessRequestModel, AccessPolicyModel accessPolicyModel, AccessResponseModel accessResponseModel) {
        AccessLog logEntry = new AccessLog(accessRequestModel, accessPolicyModel, accessResponseModel);
        logEntry.setId(generateSequence());
        logEntry.setDateTime(LocalDateTime.now());
        accessLogRepository.save(logEntry);
    }

    private String generateSequence() {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(AccessLog.SEQUENCE_NAME)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return String.valueOf(!Objects.isNull(counter) ? counter.getSeq() : 1);
    }
}
