package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.integracion.NubeFactService;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final NubeFactService nubeFactService;
    private final VentaService ventaServicio;

    public ComprobanteController(NubeFactService nubeFactService, VentaService ventaServicio) {
        this.nubeFactService = nubeFactService;
        this.ventaServicio = ventaServicio;
    }

    @PostMapping("/emitir/{ventaId}")
    public ResponseEntity<?> emitirComprobante(@PathVariable Integer ventaId) {
        return ventaServicio.obtenerPorId(ventaId)
                .map(venta -> {
                    String respuestaNubeFact = nubeFactService.emitirComprobante(venta);
                    return ResponseEntity.ok(Map.of(
                            "success", true,
                            "message", "Comprobante emitido correctamente",
                            "data", respuestaNubeFact
                    ));
                })
                .orElse(ResponseEntity.status(404).body(Map.of(
                        "success", false,
                        "message", "Venta no encontrada con ID: " + ventaId
                )));
    }


    @GetMapping("/estado/{ventaId}")
    public ResponseEntity<?> consultarEstadoComprobante(@PathVariable Integer ventaId) {
        try {
            // Cambio de Venta a VentaDTO
            VentaDTO venta = ventaServicio.obtenerPorId(ventaId)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));

            // Aquí puedes implementar lógica para consultar el estado en NubeFact
            // Por ahora devolvemos un mock
            return ResponseEntity.ok()
                    .body(Map.of(
                            "success", true,
                            "estado", "EMITIDO",
                            "numero_comprobante", venta.getNumeroComprobante(),
                            "serie", venta.getSerieComprobante()
                    ));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "message", "Error al consultar estado: " + e.getMessage()
                    ));
        }
    }
}