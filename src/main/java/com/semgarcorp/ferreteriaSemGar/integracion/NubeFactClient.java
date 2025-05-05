package com.semgarcorp.ferreteriaSemGar.integracion;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NubeFactClient {
    private static final String API_TOKEN = "d062baf9fd774cc2a8c113e7f81cc0c8d7f3d2c2cc034be0aad1632356b134b0";
    private static final String API_URL = "https://api.nubefact.com/api/v1/9c461e31-9c0c-4412-87b5-bd09c794cbb6";
    private final RestTemplate restTemplate = new RestTemplate();

    public String emitirComprobante(String jsonRequest) {
        HttpHeaders headers = crearHeaders();
        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                API_URL,
                request,
                String.class
        );
        return response.getBody();
    }

    public String anularComprobante(String serie, String numero, String motivo, String codigoUnico) {
        try {
            // Construir el JSON de anulación
            String jsonRequest = String.format(
                    "{\"operacion\":\"generar_anulacion\",\"tipo_de_comprobante\":1,\"serie\":\"%s\",\"numero\":%s,\"motivo\":\"%s\",\"codigo_unico\":\"%s\"}",
                    serie, numero, motivo, codigoUnico);

            HttpHeaders headers = crearHeaders();
            HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    API_URL,
                    request,
                    String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Error al anular comprobante en NubeFact", e);
        }
    }

    private HttpHeaders crearHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + API_TOKEN);
        return headers;
    }

    public String consultarComprobantes(int tipoComprobante, String serie) {
        HttpHeaders headers = crearHeaders();
        String url = String.format("%s?tipo=%d&serie=%s", API_URL, tipoComprobante, serie);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        return response.getBody();
    }
}