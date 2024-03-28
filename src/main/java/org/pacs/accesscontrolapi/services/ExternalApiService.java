package org.pacs.accesscontrolapi.services;

import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {
    private final WebClient webClient;

    public ExternalApiService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8085/access-policies")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AccessPolicyModel fetchAccessPolicyByLocation(String location) {
        return webClient.get()
                .uri("/find/location/{location}", location)
                .retrieve()
                .bodyToMono(AccessPolicyModel.class)
                .block();
    }
}
