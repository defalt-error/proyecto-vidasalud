package cl.duoc.vidasalud.appointments.repository;

import cl.duoc.vidasalud.appointments.model.Appointment;
import cl.duoc.vidasalud.appointments.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientId(String patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:patientId IS NULL OR a.patientId = :patientId) AND " +
           "(:from IS NULL OR a.appointmentDate >= :from) AND " +
           "(:to IS NULL OR a.appointmentDate <= :to) " +
           "ORDER BY a.appointmentDate ASC")
    List<Appointment> findWithFilters(
            @Param("status") AppointmentStatus status,
            @Param("patientId") String patientId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
