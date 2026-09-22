package cl.duoc.vidasalud.catalog.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBoxDTO {

    @NotBlank(message = "El código del box es obligatorio")
    private String code;

    @NotBlank(message = "El nombre del box es obligatorio")
    private String name;

    private String centerId;
    private String specialty;
}
