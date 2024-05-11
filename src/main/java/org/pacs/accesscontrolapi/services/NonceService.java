package org.pacs.accesscontrolapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.NonceTracker;
import org.pacs.accesscontrolapi.repositories.NonceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NonceService {

    private final NonceRepository nonceRepository;

    public Boolean verifyNonce(String userID, String nonce) {
        // First, fetch nonce from trackers database
        NonceTracker nonceTracker = nonceRepository.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User ID not found"));

        // Check
        Integer index = nonceTracker.getIndex();
        String existingNonce = nonceTracker.getNonceSequence().get(index);
        if(existingNonce.equals(nonce)) {
            nonceTracker.setIndex(index + 1);
            nonceRepository.save(nonceTracker);
            return true;
        } else {
            return false;
        }
    }
}