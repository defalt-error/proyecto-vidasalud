package cl.duoc.vidasalud.catalog.config;

import cl.duoc.vidasalud.catalog.model.Box;
import cl.duoc.vidasalud.catalog.model.MedicalService;
import cl.duoc.vidasalud.catalog.repository.BoxRepository;
import cl.duoc.vidasalud.catalog.repository.MedicalServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MedicalServiceRepository serviceRepository;
    private final BoxRepository boxRepository;

    @Override
    public void run(String... args) {
        if (serviceRepository.count() == 0) {
            log.info("Inicializando datos semilla en el catálogo de prestaciones...");
            
            serviceRepository.save(MedicalService.builder()
                    .code("MED-GEN-01")
                    .name("Consulta Medicina General")
                    .description("Atención médica preventiva y diagnóstico general para adultos y niños.")
                    .category("Medicina General")
                    .price(new BigDecimal("25000.00"))
                    .availableQuota(15)
                    .active(true)
                    .build());

            serviceRepository.save(MedicalService.builder()
                    .code("DEN-PREV-01")
                    .name("Atención Dental Preventiva")
                    .description("Limpieza dental profunda, evaluación de caries y aplicación de flúor.")
                    .category("Odontología")
                    .price(new BigDecimal("45000.00"))
                    .availableQuota(8)
                    .active(true)
                    .build());

            serviceRepository.save(MedicalService.builder()
                    .code("TELE-MED-01")
                    .name("Telemedicina Urgencia Leve")
                    .description("Consulta remota por videoconferencia para diagnósticos de baja complejidad.")
                    .category("Telemedicina")
                    .price(new BigDecimal("18000.00"))
                    .availableQuota(25)
                    .active(true)
                    .build());

            serviceRepository.save(MedicalService.builder()
                    .code("PED-GEN-01")
                    .name("Consulta Pediatría")
                    .description("Control del niño sano y atención médica infantil especializada.")
                    .category("Pediatría")
                    .price(new BigDecimal("32000.00"))
                    .availableQuota(10)
                    .active(true)
                    .build());
        }

        if (boxRepository.count() == 0) {
            log.info("Inicializando boxes clínicos en el catálogo...");

            boxRepository.save(Box.builder()
                    .code("BOX-101")
                    .name("Box 101 - Consulta General")
                    .centerId("CENTRO-SANTIAGO-CENTRO")
                    .specialty("Medicina General")
                    .active(true)
                    .build());

            boxRepository.save(Box.builder()
                    .code("BOX-102")
                    .name("Box 102 - Pediatría")
                    .centerId("CENTRO-SANTIAGO-CENTRO")
                    .specialty("Pediatría")
                    .active(true)
                    .build());

            boxRepository.save(Box.builder()
                    .code("BOX-DENTAL-01")
                    .name("Box Dental 01 - Sillón Ergonómico")
                    .centerId("CENTRO-PROVIDENCIA")
                    .specialty("Odontología")
                    .active(true)
                    .build());
        }
    }
}
