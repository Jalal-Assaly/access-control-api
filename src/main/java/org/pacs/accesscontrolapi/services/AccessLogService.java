package org.pacs.accesscontrolapi.services;

import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.EmployeeAccessLog;
import org.pacs.accesscontrolapi.documents.DatabaseSequence;
import org.pacs.accesscontrolapi.documents.VisitorAccessLog;
import org.pacs.accesscontrolapi.models.logmodels.EmployeeHistoryModel;
import org.pacs.accesscontrolapi.models.logmodels.VisitorHistoryModel;
import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.employeemodels.EmployeeAccessRequestModel;
import org.pacs.accesscontrolapi.models.requestmodels.visitormodels.VisitorAccessRequestModel;
import org.pacs.accesscontrolapi.models.responsemodels.AccessResponseModel;
import org.pacs.accesscontrolapi.repositories.EmployeeAccessLogRepository;
import org.pacs.accesscontrolapi.repositories.VisitorAccessLogRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final MongoOperations mongoOperations;
    private final EmployeeAccessLogRepository employeeAccessLogRepository;
    private final VisitorAccessLogRepository visitorAccessLogRepository;

    public List<EmployeeAccessLog> getAllEmployeeAccessLogs() {
        return employeeAccessLogRepository.findAll();
    }

    public List<VisitorAccessLog> getAllVisitorAccessLogs() {
        return visitorAccessLogRepository.findAll();
    }

    public List<EmployeeHistoryModel> getEmployeeHistory(String userId) {
        List<EmployeeAccessLog> employeeAccessLogs = employeeAccessLogRepository.findEmployeeAccessLogsByAccessRequestModel_EmployeeModel_Id(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return employeeAccessLogs.stream()
                .map(log -> new EmployeeHistoryModel(
                        log.getAccessRequestModel().getEmployeeModel().getId(),
                        log.getAccessRequestModel().getAccessPointModel().getLocation(),
                        log.getDateTime().format(formatter),
                        log.getAccessResponseModel().getDecision().toString()
                ))
                .collect(Collectors.toList());
    }

    public List<VisitorHistoryModel> getVisitorHistory(String userId) {
        List<VisitorAccessLog> visitorAccessLogs = visitorAccessLogRepository.findVisitorAccessLogByAccessRequestModel_VisitorModel_Id(userId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return visitorAccessLogs.stream()
                .map(log -> new VisitorHistoryModel(
                        log.getAccessRequestModel().getVisitorModel().getId(),
                        log.getAccessRequestModel().getAccessPointModel().getLocation(),
                        log.getDateTime().format(formatter),
                        log.getAccessResponseModel().getDecision().toString()
                ))
                .collect(Collectors.toList());
    }


    public void logEmployeeAccess(EmployeeAccessRequestModel accessRequestModel, AccessPolicyModel accessPolicyModel, AccessResponseModel accessResponseModel) {
        EmployeeAccessLog logEntry = new EmployeeAccessLog(accessRequestModel, accessPolicyModel, accessResponseModel);
        logEntry.setId(generateEmployeeLogsSequence());
        logEntry.setDateTime(LocalDateTime.now());
        employeeAccessLogRepository.save(logEntry);
    }

    public void logVisitorAccess(VisitorAccessRequestModel accessRequestModel, AccessPolicyModel accessPolicyModel, AccessResponseModel accessResponseModel) {
        VisitorAccessLog logEntry = new VisitorAccessLog(accessRequestModel, accessPolicyModel, accessResponseModel);
        logEntry.setId(generateVisitorLogsSequence());
        logEntry.setDateTime(LocalDateTime.now());
        visitorAccessLogRepository.save(logEntry);
    }

    private String generateEmployeeLogsSequence() {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(EmployeeAccessLog.SEQUENCE_NAME)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return String.valueOf(!Objects.isNull(counter) ? counter.getSeq() : 1);
    }

    private String generateVisitorLogsSequence() {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(VisitorAccessLog.SEQUENCE_NAME)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return String.valueOf(!Objects.isNull(counter) ? counter.getSeq() : 1);
    }
}
