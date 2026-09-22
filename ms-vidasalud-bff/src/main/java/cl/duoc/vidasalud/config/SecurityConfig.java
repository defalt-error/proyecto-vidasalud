package cl.duoc.vidasalud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/public/**", "/api/public/**").permitAll()
                // Autorización basada en roles (Admin, Operador/Recepcionista, Cliente/Paciente, Auditor)
                .requestMatchers("/api/admin/**").hasAnyRole("Admin", "ADMIN")
                .requestMatchers("/api/operator/**").hasAnyRole("Operador", "OPERADOR", "Recepcionista", "RECEPCIONISTA", "Admin", "ADMIN")
                .requestMatchers("/api/client/**").hasAnyRole("Cliente", "CLIENTE", "Paciente", "PACIENTE", "Admin", "ADMIN")
                .requestMatchers("/api/audit/**").hasAnyRole("Auditor", "AUDITOR", "Admin", "ADMIN")
                
                // Endpoints de Dominio Catálogo (/api/catalog/**)
                .requestMatchers(HttpMethod.GET, "/api/catalog", "/api/catalog/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/catalog", "/api/catalog/**").hasAnyRole("Admin", "ADMIN", "Operador", "OPERADOR", "Recepcionista", "RECEPCIONISTA")
                .requestMatchers(HttpMethod.PUT, "/api/catalog", "/api/catalog/**").hasAnyRole("Admin", "ADMIN", "Operador", "OPERADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/catalog", "/api/catalog/**").hasAnyRole("Admin", "ADMIN")

                // Endpoints de Dominio Atenciones (/api/appointments/**)
                .requestMatchers(HttpMethod.PUT, "/api/appointments/*/status", "/api/appointments/**").hasAnyRole("Operador", "OPERADOR", "Recepcionista", "RECEPCIONISTA", "Admin", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/appointments", "/api/appointments/**").hasAnyRole("Cliente", "CLIENTE", "Paciente", "PACIENTE", "Operador", "OPERADOR", "Recepcionista", "RECEPCIONISTA", "Admin", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/appointments", "/api/appointments/**").authenticated()
                
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With", "Origin"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Extrae las autoridades, scopes y roles ('roles' claim) desde el JWT de Azure AD
     */
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>(defaultGrantedAuthoritiesConverter.convert(jwt));

            // Extracción de roles desde la claim 'roles' otorgada por Azure AD
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            }

            // Extracción de grupos desde la claim 'groups'
            List<String> groups = jwt.getClaimAsStringList("groups");
            if (groups != null) {
                for (String group : groups) {
                    authorities.add(new SimpleGrantedAuthority("GROUP_" + group));
                }
            }

            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String tenantId = issuerUri.replace("https://login.microsoftonline.com/", "")
                                   .replace("/v2.0", "")
                                   .replace("/", "");

        String jwkSetUri = "https://login.microsoftonline.com/" + tenantId + "/discovery/v2.0/keys";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        String v2Issuer = "https://login.microsoftonline.com/" + tenantId + "/v2.0";
        String v1Issuer = "https://sts.windows.net/" + tenantId + "/";

        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();

        // Validaciones de emisor (issuer)
        OAuth2TokenValidator<Jwt> issuerValidator = jwt -> {
            String iss = jwt.getIssuer() != null ? jwt.getIssuer().toString() : "";
            if (iss.equals(v2Issuer) || iss.equals(v1Issuer)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(
                new OAuth2Error("invalid_issuer", "El emisor del token (" + iss + ") no coincide con " + v2Issuer + " ni " + v1Issuer, null)
            );
        };

        // Validaciones de audiencia (audience)
        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> {
            List<String> aud = jwt.getAudience();
            if (aud == null || aud.isEmpty()) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.success();
        };

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(timestampValidator, issuerValidator, audienceValidator));
        return jwtDecoder;
    }
}