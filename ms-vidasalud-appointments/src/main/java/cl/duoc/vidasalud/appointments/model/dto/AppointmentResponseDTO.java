package cl.duoc.vidasalud.appointments.model.dto;

import cl.duoc.vidasalud.appointments.model.Appointment;
import cl.duoc.vidasalud.appointments.model.AppointmentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponseDTO {

    private Long id;
    private String patientId;
    private String patientName;
    private String patientRut;
    private String serviceId;
    private String serviceName;
    private String centerId;
    private String boxId;
    private LocalDateTime appointmentDate;
    private AppointmentStatus status;
    private String reason;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static AppointmentResponseDTO fromEntity(Appointment a) {
        return AppointmentResponseDTO.builder()
                .id(a.getId())
                .patientId(a.getPatientId())
                .patientName(a.getPatientName())
                .patientRut(a.getPatientRut())
                .serviceId(a.getServiceId())
                .serviceName(a.getServiceName())
                .centerId(a.getCenterId())
                .boxId(a.getBoxId())
                .appointmentDate(a.getAppointmentDate())
                .status(a.getStatus())
                .reason(a.getReason())
                .notes(a.getNotes())
                .createdBy(a.getCreatedBy())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
