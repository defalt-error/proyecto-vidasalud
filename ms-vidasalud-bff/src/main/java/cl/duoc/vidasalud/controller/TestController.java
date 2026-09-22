package cl.duoc.vidasalud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public/test")
    public Map<String, String> getPublicTest() {
        return Map.of("message", "Conexión exitosa PÚBLICA con Spring Boot BFF (sin necesidad de JWT)");
    }

    @GetMapping("/test")
    public Map<String, String> getTest() {
        return Map.of("message", "Conexión exitosa AUTENTICADA entre Angular y Spring Boot BFF (JWT de Azure AD verificado)");
    }
}