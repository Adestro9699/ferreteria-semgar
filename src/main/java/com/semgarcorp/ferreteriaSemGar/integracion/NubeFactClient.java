package com.semgarcorp.ferreteriaSemGar.integracion;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NubeFactClient {

    public String emitirComprobante(String jsonRequest) {
        RestTemplate restTemplate = new RestTemplate();

        // Configura headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String API_TOKEN = "d062baf9fd774cc2a8c113e7f81cc0c8d7f3d2c2cc034be0aad1632356b134b0";
        headers.set("Authorization", "Token " + API_TOKEN);

        // Crea la petición HTTP
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        // URL base de NubeFact
        String API_URL = "https://api.nubefact.com/api/v1/9c461e31-9c0c-4412-87b5-bd09c794cbb6";

        // Envía la petición
        ResponseEntity<String> response = restTemplate.postForEntity(
                API_URL,
                request,
                String.class
        );

        return response.getBody();
    }
}