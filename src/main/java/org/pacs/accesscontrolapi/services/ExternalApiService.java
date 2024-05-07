package org.pacs.accesscontrolapi.services;

import org.pacs.accesscontrolapi.models.policymodels.AccessPolicyModel;
import org.pacs.accesscontrolapi.models.requestmodels.AccessPointModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {
    private final WebClient webClient1;
    private final WebClient webClient2;

    public ExternalApiService() {
        this.webClient1 = WebClient.builder()
                .baseUrl("http://localhost:8085/access-policies")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.webClient2 = WebClient.builder()
                .baseUrl("http://localhost:8082/access-points-attributes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AccessPolicyModel fetchAccessPolicyByLocation(String location) {
        return webClient1.get()
                .uri("/find/location/{location}", location)
                .retrieve()
                .bodyToMono(AccessPolicyModel.class)
                .block();
    }

    public void updateLiveAccessPointAttributesById(String location, AccessPointModel accessPointModel) {
        webClient2.put()
                .uri("/live-update/{location}", location)
                .bodyValue(accessPointModel)
                .retrieve()
                .toBodilessEntity()
                .block();
//                .then()
//                .subscribe();
    }
}
