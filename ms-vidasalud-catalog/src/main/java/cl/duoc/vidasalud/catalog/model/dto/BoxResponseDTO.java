package cl.duoc.vidasalud.catalog.model.dto;

import cl.duoc.vidasalud.catalog.model.Box;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxResponseDTO {

    private Long id;
    private String code;
    private String name;
    private String centerId;
    private String specialty;
    private Boolean active;

    public static BoxResponseDTO fromEntity(Box entity) {
        return BoxResponseDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .centerId(entity.getCenterId())
                .specialty(entity.getSpecialty())
                .active(entity.getActive())
                .build();
    }
}
