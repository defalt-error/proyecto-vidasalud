package cl.duoc.vidasalud.catalog.repository;

import cl.duoc.vidasalud.catalog.model.MedicalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalServiceRepository extends JpaRepository<MedicalService, Long> {
    Optional<MedicalService> findByCode(String code);
    List<MedicalService> findByActiveTrue();
    List<MedicalService> findByCategoryAndActiveTrue(String category);
    boolean existsByCode(String code);
}
