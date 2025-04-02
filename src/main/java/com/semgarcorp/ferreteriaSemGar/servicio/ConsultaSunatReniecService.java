package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import com.semgarcorp.ferreteriaSemGar.modelo.TipoDocumento;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ConsultaSunatReniecService {

    @Value("${sunat.api.token}")
    private String token;

    @Value("${sunat.api.ruc.url}")
    private String apiRucUrl;

    @Value("${sunat.api.dni.url}")
    private String apiDniUrl;

    private final RestTemplate restTemplate;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public ConsultaSunatReniecService(RestTemplate restTemplate,
                                      TipoDocumentoRepository tipoDocumentoRepository) {
        this.restTemplate = restTemplate;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public Cliente consultarDocumento(String numeroDocumento, String tipoDocumento) {
        if (tipoDocumento.equals("RUC")) {
            return consultarRuc(numeroDocumento);
        } else {
            return consultarDni(numeroDocumento);
        }
    }

    private Cliente consultarRuc(String ruc) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    apiRucUrl + ruc,
                    HttpMethod.GET,
                    entity,
                    Map.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Error al consultar SUNAT: " + response.getStatusCode());
            }

            return mapearRespuesta(response.getBody(), 6);
        } catch (RestClientException e) {
            throw new RuntimeException("Error al conectar con SUNAT: " + e.getMessage());
        }
    }

    private Cliente consultarDni(String dni) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                apiDniUrl + dni,
                HttpMethod.GET,
                entity,
                Map.class
        );

        return mapearRespuesta(response.getBody(), 1); // "1" = DNI
    }

    private Cliente mapearRespuesta(Map<String, Object> data, Integer codigoNubeFact) {
        Cliente cliente = new Cliente();
        TipoDocumento tipoDoc = tipoDocumentoRepository.findByCodigoNubeFact(codigoNubeFact)
                .orElseThrow(() -> new RuntimeException("Tipo documento no encontrado"));

        cliente.setTipoDocumento(tipoDoc);
        cliente.setNumeroDocumento(data.get("numeroDocumento").toString());

        if (codigoNubeFact.equals(6)) { // RUC
            cliente.setRazonSocial(data.get("razonSocial").toString());
            cliente.setDireccion(data.get("direccion").toString());
            cliente.setEstado(data.get("estado").toString());
        } else { // DNI
            cliente.setNombres(data.get("nombres").toString());
            cliente.setApellidos(
                    data.get("apellidoPaterno").toString() + " " +
                            data.get("apellidoMaterno").toString()
            );
        }

        return cliente;
    }
}