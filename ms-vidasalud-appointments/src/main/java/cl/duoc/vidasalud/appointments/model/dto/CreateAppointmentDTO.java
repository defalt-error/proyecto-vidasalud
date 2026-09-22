package cl.duoc.vidasalud.appointments.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAppointmentDTO {

    @NotBlank(message = "El ID del paciente es obligatorio")
    private String patientId;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String patientName;

    private String patientRut;

    @NotBlank(message = "El ID del servicio es obligatorio")
    private String serviceId;

    @NotBlank(message = "El nombre del servicio es obligatorio")
    private String serviceName;

    private String centerId;
    private String boxId;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    private LocalDateTime appointmentDate;

    private String reason;
    private String notes;
    private String createdBy;
}
