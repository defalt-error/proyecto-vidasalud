package cl.duoc.vidasalud.catalog.repository;

import cl.duoc.vidasalud.catalog.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoxRepository extends JpaRepository<Box, Long> {
    Optional<Box> findByCode(String code);
    List<Box> findByActiveTrue();
    boolean existsByCode(String code);
}
