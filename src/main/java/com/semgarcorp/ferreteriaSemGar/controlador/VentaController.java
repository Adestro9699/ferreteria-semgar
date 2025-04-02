package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Obtener la lista de todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listar() {
        List<VentaDTO> ventasDTO = ventaService.listar(); // Obtener la lista de DTOs
        return ResponseEntity.ok(ventasDTO); // Devolver la lista en la respuesta
    }

    // Obtener una venta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Integer id) {
        return ventaService.obtenerPorId(id)
                .map(ResponseEntity::ok)                      // Si está presente, devuelve 200 OK con el DTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no está, devuelve 404 Not Found
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<VentaDTO> guardarVenta(@RequestBody VentaDTO ventaDTO) {
        // 1. Guardar la venta usando el servicio
        VentaDTO nuevaVentaDTO = ventaService.guardarVenta(ventaDTO);

        // 2. Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaVentaDTO.getIdVenta()).toUri();

        // 3. Devolver la respuesta con la URI en la cabecera Location y el DTO en el cuerpo
        return ResponseEntity.created(location).body(nuevaVentaDTO);
    }

    @PostMapping("/{idVenta}/completar")
    public ResponseEntity<VentaDTO> completarVenta(
            @PathVariable Integer idVenta,
            @RequestBody Map<String, Integer> requestBody // Recibir el idCaja en el cuerpo de la solicitud
    )
    {
        System.out.println("==== LLEGÓ AL CONTROLLER ====");

        Integer idCaja = requestBody.get("idCaja"); // Obtener el idCaja del JSON
        if (idCaja == null) {
            throw new IllegalArgumentException("El idCaja es requerido para completar la venta.");
        }

        VentaDTO ventaCompletada = ventaService.completarVenta(idVenta, idCaja); // Pasar el idCaja al servicio
        return ResponseEntity.ok(ventaCompletada);
    }

    // Actualizar una venta existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Integer id, @RequestBody VentaDTO ventaDTO) {
        return ventaService.obtenerPorId(id)
                .map(ventaExistenteDTO -> {
                    VentaDTO ventaActualizadaDTO = ventaService.actualizar(id, ventaDTO);
                    return ResponseEntity.ok(ventaActualizadaDTO);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return ventaService.obtenerPorId(id)
                .map(venta -> {
                    ventaService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build(); // Forzamos el tipo a Void
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}