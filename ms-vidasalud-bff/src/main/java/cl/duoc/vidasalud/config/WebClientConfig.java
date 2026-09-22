package cl.duoc.vidasalud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig {

    @Value("${services.appointments.url:http://localhost:8081}")
    private String appointmentsBaseUrl;

    @Value("${services.catalog.url:http://localhost:8083}")
    private String catalogBaseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestClient appointmentsRestClient() {
        return RestClient.builder()
                .baseUrl(appointmentsBaseUrl)
                .build();
    }

    @Bean
    public RestClient catalogRestClient() {
        return RestClient.builder()
                .baseUrl(catalogBaseUrl)
                .build();
    }
}