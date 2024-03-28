package org.example.accesscontrolapi.repositories;


import org.example.accesscontrolapi.documents.AccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessLogRepository extends MongoRepository<AccessLog, Long> {
}
