package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.ComprobanteNubeFactDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.ComprobanteNubeFactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteNubeFactController {

    private final ComprobanteNubeFactService comprobanteNubeFactService;

    public ComprobanteNubeFactController(ComprobanteNubeFactService comprobanteNubeFactService) {
        this.comprobanteNubeFactService = comprobanteNubeFactService;
    }

    //para poder consultar PDF, XML Y QR de una venta a través de ID de la venta
    //http://localhost:8080/fs/comprobantes/venta/idVenta
    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<ComprobanteNubeFactDTO> obtenerComprobantePorVenta(@PathVariable Integer idVenta) {
        return ResponseEntity.ok(comprobanteNubeFactService.obtenerComprobantePorVenta(idVenta));
    }
} 