package cl.duoc.vidasalud.catalog.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMedicalServiceDTO {

    @NotBlank(message = "El código es obligatorio")
    private String code;

    @NotBlank(message = "El nombre de la prestación es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio debe ser un valor positivo o cero")
    private BigDecimal price;

    @NotNull(message = "El cupo disponible es obligatorio")
    @Min(value = 0, message = "El cupo no puede ser negativo")
    private Integer availableQuota;
}
