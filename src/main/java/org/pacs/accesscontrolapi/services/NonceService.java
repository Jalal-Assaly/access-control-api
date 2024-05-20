package org.pacs.accesscontrolapi.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.pacs.accesscontrolapi.documents.NonceTracker;
import org.pacs.accesscontrolapi.repositories.NonceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NonceService {

    private final NonceRepository nonceRepository;

    public Boolean verifyNonce(String userID, String nonce) {

        System.out.println(userID);
        // First, fetch nonce from trackers database
        NonceTracker nonceTracker = nonceRepository.findNonceTrackerByUserID(userID)
                .orElseThrow(() -> new EntityNotFoundException("User ID not found"));

        // Fetch nonce
        Integer index = nonceTracker.getIndex();

        List<String> nonceSequence = nonceTracker.getNonceSequence();
        List<String> remainingNonces = nonceTracker.getNonceSequence().subList(index+1, nonceSequence.size());

        System.out.println(nonce);
        System.out.println(remainingNonces);

        if(remainingNonces.contains(nonce)) {
            Integer updatedNonceIndex = nonceSequence.indexOf(nonce);
            nonceTracker.setIndex(updatedNonceIndex);
            nonceRepository.save(nonceTracker);
            return true;
        } else {
            return false;
        }
    }
}