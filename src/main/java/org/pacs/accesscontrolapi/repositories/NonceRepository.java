package org.pacs.accesscontrolapi.repositories;


import org.pacs.accesscontrolapi.documents.NonceTracker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NonceRepository extends MongoRepository<NonceTracker, String> {
}
