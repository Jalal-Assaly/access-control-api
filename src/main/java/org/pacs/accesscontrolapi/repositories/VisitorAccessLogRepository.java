package org.pacs.accesscontrolapi.repositories;


import org.pacs.accesscontrolapi.documents.VisitorAccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitorAccessLogRepository extends MongoRepository<VisitorAccessLog, String> {
    List<VisitorAccessLog> findVisitorAccessLogByAccessRequestModel_VisitorModel_Id(String userId);
}
