package cl.duoc.vidasalud.catalog.model.dto;

import cl.duoc.vidasalud.catalog.model.MedicalService;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalServiceResponseDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer availableQuota;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MedicalServiceResponseDTO fromEntity(MedicalService entity) {
        return MedicalServiceResponseDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .price(entity.getPrice())
                .availableQuota(entity.getAvailableQuota())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
