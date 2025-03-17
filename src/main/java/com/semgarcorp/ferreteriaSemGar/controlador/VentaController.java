package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
        VentaDTO ventaDTO = ventaService.obtenerPorId(id); // Obtener el DTO
        if (ventaDTO != null) {
            return ResponseEntity.ok(ventaDTO); // Devolver el DTO en la respuesta
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, devolver 404
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
    public ResponseEntity<VentaDTO> completarVenta(@PathVariable Integer idVenta) {
        VentaDTO ventaCompletada = ventaService.completarVenta(idVenta);
        return ResponseEntity.ok(ventaCompletada);
    }

    // Actualizar una venta existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Integer id, @RequestBody VentaDTO ventaDTO) {
        VentaDTO ventaExistenteDTO = ventaService.obtenerPorId(id);
        if (ventaExistenteDTO != null) {
            VentaDTO ventaActualizadaDTO = ventaService.actualizar(id, ventaDTO);
            if (ventaActualizadaDTO != null) {
                return ResponseEntity.ok(ventaActualizadaDTO);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar una venta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        VentaDTO ventaExistenteDTO = ventaService.obtenerPorId(id);
        if (ventaExistenteDTO != null) {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}