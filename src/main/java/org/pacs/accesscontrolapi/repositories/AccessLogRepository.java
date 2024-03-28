package org.pacs.accesscontrolapi.repositories;


import org.pacs.accesscontrolapi.documents.AccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessLogRepository extends MongoRepository<AccessLog, Long> {
}
