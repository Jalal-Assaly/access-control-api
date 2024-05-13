package org.pacs.accesscontrolapi.repositories;


import org.pacs.accesscontrolapi.documents.EmployeeAccessLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeAccessLogRepository extends MongoRepository<EmployeeAccessLog, String> {
    List<EmployeeAccessLog> findEmployeeAccessLogsByAccessRequestModel_EmployeeModel_Id(String userId);
}
