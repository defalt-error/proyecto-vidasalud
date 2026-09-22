package cl.duoc.vidasalud.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {
    // La configuración de CORS está administrada centralizadamente en SecurityConfig 
    // a través del bean CorsConfigurationSource para garantizar que se aplique 
    // a todas las peticiones (incluyendo peticiones fallidas 401/403 y OPTIONS preflight).
}