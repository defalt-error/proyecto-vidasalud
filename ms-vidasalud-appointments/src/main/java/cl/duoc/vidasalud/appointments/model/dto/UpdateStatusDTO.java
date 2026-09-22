package cl.duoc.vidasalud.appointments.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusDTO {

    @NotBlank(message = "El nuevo estado es obligatorio")
    private String status;

    private String boxId;
    private String notes;
    private String updatedBy;
}
