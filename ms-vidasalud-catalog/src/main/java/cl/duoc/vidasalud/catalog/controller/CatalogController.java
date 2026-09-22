package cl.duoc.vidasalud.catalog.controller;

import cl.duoc.vidasalud.catalog.model.dto.*;
import cl.duoc.vidasalud.catalog.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    // --- PRESTACIONES (SERVICES) ---

    @GetMapping("/services")
    public ResponseEntity<List<MedicalServiceResponseDTO>> getAllServices(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyActive) {
        return ResponseEntity.ok(catalogService.getAllServices(category, onlyActive));
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<MedicalServiceResponseDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getServiceById(id));
    }

    @PostMapping("/services")
    public ResponseEntity<MedicalServiceResponseDTO> createService(@Valid @RequestBody CreateMedicalServiceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createService(dto));
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<MedicalServiceResponseDTO> updateService(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateServiceDTO dto) {
        return ResponseEntity.ok(catalogService.updateService(id, dto));
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        catalogService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    // --- BOXES CLÍNICOS ---

    @GetMapping("/boxes")
    public ResponseEntity<List<BoxResponseDTO>> getAllBoxes(
            @RequestParam(required = false, defaultValue = "false") Boolean onlyActive) {
        return ResponseEntity.ok(catalogService.getAllBoxes(onlyActive));
    }

    @PostMapping("/boxes")
    public ResponseEntity<BoxResponseDTO> createBox(@Valid @RequestBody CreateBoxDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createBox(dto));
    }
}
