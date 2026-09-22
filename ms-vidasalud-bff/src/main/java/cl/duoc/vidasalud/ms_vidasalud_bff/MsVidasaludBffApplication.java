package cl.duoc.vidasalud.ms_vidasalud_bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cl.duoc.vidasalud")
public class MsVidasaludBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsVidasaludBffApplication.class, args);
    }
}