package cl.duoc.vidasalud.appointments.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ATENCIONES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private String patientId;

    @Column(name = "paciente_nombre", nullable = false)
    private String patientName;

    @Column(name = "paciente_rut")
    private String patientRut;

    @Column(name = "servicio_id", nullable = false)
    private String serviceId;

    @Column(name = "servicio_nombre", nullable = false)
    private String serviceName;

    @Column(name = "centro_id")
    private String centerId;

    @Column(name = "box_id")
    private String boxId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private AppointmentStatus status;

    @Column(name = "motivo", length = 500)
    private String reason;

    @Column(name = "observaciones", length = 1000)
    private String notes;

    @Column(name = "creado_por")
    private String createdBy;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = AppointmentStatus.SOLICITADA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
