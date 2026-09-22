package cl.duoc.vidasalud.catalog.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceDTO {

    private String name;
    private String description;

    @PositiveOrZero(message = "El precio debe ser un valor positivo o cero")
    private BigDecimal price;

    @Min(value = 0, message = "El cupo no puede ser negativo")
    private Integer availableQuota;

    private Boolean active;
}
