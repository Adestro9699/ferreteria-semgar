package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.TipoCambioDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.TipoCambio;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoCambioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class TipoCambioService {

    private final TipoCambioRepository tipoCambioRepositorio;
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.apis.net.pe/v2/sunat/tipo-cambio";
    private static final String TOKEN = "apis-token-13936.l9djKy9c0ENWbOu0D4HyuhTyCRMYk4Xr";

    public TipoCambioService(TipoCambioRepository tipoCambioRepositorio, RestTemplate restTemplate) {
        this.tipoCambioRepositorio = tipoCambioRepositorio;
        this.restTemplate = restTemplate;
    }

    public List<TipoCambio> listar() {
        return tipoCambioRepositorio.findAll();
    }

    public TipoCambio obtenerPorId(Integer id) {
        return tipoCambioRepositorio.findById(id).orElse(null);
    }

    public TipoCambio guardar(TipoCambio tipoCambio) {
        return tipoCambioRepositorio.save(tipoCambio);
    }

    public TipoCambio actualizar(TipoCambio tipoCambio) {
        return tipoCambioRepositorio.save(tipoCambio);
    }

    public void eliminar(Integer id) {
        tipoCambioRepositorio.deleteById(id);
    }

    public TipoCambio obtenerTipoCambioDelDia() {
        LocalDate hoy = LocalDate.now();
        return tipoCambioRepositorio.findByFecha(hoy)
                .orElseGet(() -> obtenerYGuardarTipoCambioDeApi(hoy));
    }

    private TipoCambio obtenerYGuardarTipoCambioDeApi(LocalDate fecha) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = API_URL + "?fecha=" + fecha.toString();

        ResponseEntity<TipoCambioDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TipoCambioDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            TipoCambioDTO apiResponse = response.getBody();

            // Verificar que los datos necesarios no sean nulos
            if (apiResponse.getFecha() == null || apiResponse.getVenta() == null) {
                throw new RuntimeException("La API retornó datos incompletos");
            }

            TipoCambio tipoCambio = new TipoCambio();
            tipoCambio.setFecha(apiResponse.getFecha());
            tipoCambio.setVenta(apiResponse.getVenta());
            tipoCambio.setCompra(apiResponse.getCompra()); // Este puede ser null según tu modelo

            return tipoCambioRepositorio.save(tipoCambio);
        } else {
            throw new RuntimeException("Error al obtener tipo de cambio. Código: " +
                    response.getStatusCode());
        }
    }
}
