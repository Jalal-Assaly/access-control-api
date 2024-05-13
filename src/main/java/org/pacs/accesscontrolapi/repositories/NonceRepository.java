package org.pacs.accesscontrolapi.repositories;


import org.pacs.accesscontrolapi.documents.NonceTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NonceRepository extends MongoRepository<NonceTracker, String> {
    Optional<NonceTracker> findNonceTrackerByUserID(String userId);
}
