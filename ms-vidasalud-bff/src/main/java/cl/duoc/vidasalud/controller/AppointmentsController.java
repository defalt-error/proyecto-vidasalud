package cl.duoc.vidasalud.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentsController {

    private final RestClient appointmentsRestClient;

    public AppointmentsController(@Qualifier("appointmentsRestClient") RestClient appointmentsRestClient) {
        this.appointmentsRestClient = appointmentsRestClient;
    }

    @GetMapping
    public ResponseEntity<?> getAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @AuthenticationPrincipal Jwt jwt) {
        try {
            return appointmentsRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/appointments")
                            .queryParamIfPresent("status", java.util.Optional.ofNullable(status))
                            .queryParamIfPresent("patientId", java.util.Optional.ofNullable(patientId))
                            .queryParamIfPresent("from", java.util.Optional.ofNullable(from))
                            .queryParamIfPresent("to", java.util.Optional.ofNullable(to))
                            .build())
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            return appointmentsRestClient.get()
                    .uri("/api/appointments/{id}", id)
                    .retrieve()
                    .toEntity(Object.class);
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ex.getResponseBodyAsString());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, Object> payload) {
        try {
            return appointmentsRestClient.post()
                    .uri("/api/appointments")
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

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            return appointmentsRestClient.put()
                    .uri("/api/appointments/{id}/status", id)
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