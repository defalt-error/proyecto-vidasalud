package cl.duoc.vidasalud.catalog.service;

import cl.duoc.vidasalud.catalog.exception.BusinessRuleException;
import cl.duoc.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.duoc.vidasalud.catalog.model.Box;
import cl.duoc.vidasalud.catalog.model.MedicalService;
import cl.duoc.vidasalud.catalog.model.dto.*;
import cl.duoc.vidasalud.catalog.repository.BoxRepository;
import cl.duoc.vidasalud.catalog.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final BoxRepository boxRepository;

    // --- PRESTACIONES (SERVICES) ---

    @Transactional(readOnly = true)
    public List<MedicalServiceResponseDTO> getAllServices(String category, Boolean onlyActive) {
        List<MedicalService> services;
        if (category != null && !category.isBlank()) {
            services = medicalServiceRepository.findByCategoryAndActiveTrue(category);
        } else if (Boolean.TRUE.equals(onlyActive)) {
            services = medicalServiceRepository.findByActiveTrue();
        } else {
            services = medicalServiceRepository.findAll();
        }
        return services.stream()
                .map(MedicalServiceResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicalServiceResponseDTO getServiceById(Long id) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la prestación médica con ID: " + id));
        return MedicalServiceResponseDTO.fromEntity(service);
    }

    @Transactional
    public MedicalServiceResponseDTO createService(CreateMedicalServiceDTO dto) {
        if (medicalServiceRepository.existsByCode(dto.getCode())) {
            throw new BusinessRuleException("Ya existe una prestación registrada con el código: " + dto.getCode());
        }

        MedicalService service = MedicalService.builder()
                .code(dto.getCode().toUpperCase())
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .availableQuota(dto.getAvailableQuota())
                .active(true)
                .build();

        MedicalService saved = medicalServiceRepository.save(service);
        log.info("Prestación médica creada exitosamente: {} ({})", saved.getName(), saved.getCode());
        return MedicalServiceResponseDTO.fromEntity(saved);
    }

    @Transactional
    public MedicalServiceResponseDTO updateService(Long id, UpdateServiceDTO dto) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la prestación médica con ID: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            service.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            service.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            service.setPrice(dto.getPrice());
        }
        if (dto.getAvailableQuota() != null) {
            service.setAvailableQuota(dto.getAvailableQuota());
        }
        if (dto.getActive() != null) {
            service.setActive(dto.getActive());
        }

        MedicalService updated = medicalServiceRepository.save(service);
        log.info("Prestación médica actualizada ID {}: precio={}, cupo={}", id, updated.getPrice(), updated.getAvailableQuota());
        return MedicalServiceResponseDTO.fromEntity(updated);
    }

    @Transactional
    public void deleteService(Long id) {
        MedicalService service = medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la prestación médica con ID: " + id));
        service.setActive(false);
        medicalServiceRepository.save(service);
        log.info("Prestación médica desactivada ID: {}", id);
    }

    // --- BOXES CLÍNICOS ---

    @Transactional(readOnly = true)
    public List<BoxResponseDTO> getAllBoxes(Boolean onlyActive) {
        List<Box> boxes = Boolean.TRUE.equals(onlyActive) 
                ? boxRepository.findByActiveTrue() 
                : boxRepository.findAll();
        return boxes.stream()
                .map(BoxResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public BoxResponseDTO createBox(CreateBoxDTO dto) {
        if (boxRepository.existsByCode(dto.getCode())) {
            throw new BusinessRuleException("Ya existe un box registrado con el código: " + dto.getCode());
        }

        Box box = Box.builder()
                .code(dto.getCode().toUpperCase())
                .name(dto.getName())
                .centerId(dto.getCenterId() != null ? dto.getCenterId() : "CENTRO-01")
                .specialty(dto.getSpecialty() != null ? dto.getSpecialty() : "General")
                .active(true)
                .build();

        Box saved = boxRepository.save(box);
        log.info("Box clínico creado exitosamente: {} ({})", saved.getName(), saved.getCode());
        return BoxResponseDTO.fromEntity(saved);
    }
}
