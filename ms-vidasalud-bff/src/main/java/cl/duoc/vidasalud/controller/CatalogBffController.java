package cl.duoc.vidasalud.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
public class CatalogBffController {

    private final RestClient catalogRestClient;

    public CatalogBffController(@Qualifier("catalogRestClient") RestClient catalogRestClient) {
        this.catalogRestClient = catalogRestClient;
    }

    @GetMapping("/services")
    public ResponseEntity<?> getServices(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyActive) {
        try {
            return catalogRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/catalog/services")
                            .queryParamIfPresent("category", java.util.Optional.ofNullable(category))
                            .queryParam("onlyActive", onlyActive)
                            .build())
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        try {
            return catalogRestClient.get()
                    .uri("/api/catalog/services/{id}", id)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @PostMapping("/services")
    public ResponseEntity<?> createService(@RequestBody Map<String, Object> payload) {
        try {
            return catalogRestClient.post()
                    .uri("/api/catalog/services")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<?> updateService(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            return catalogRestClient.put()
                    .uri("/api/catalog/services/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            return catalogRestClient.delete()
                    .uri("/api/catalog/services/{id}", id)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @GetMapping("/boxes")
    public ResponseEntity<?> getBoxes(@RequestParam(required = false, defaultValue = "false") Boolean onlyActive) {
        try {
            return catalogRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/catalog/boxes")
                            .queryParam("onlyActive", onlyActive)
                            .build())
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @PostMapping("/boxes")
    public ResponseEntity<?> createBox(@RequestBody Map<String, Object> payload) {
        try {
            return catalogRestClient.post()
                    .uri("/api/catalog/boxes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }
}
