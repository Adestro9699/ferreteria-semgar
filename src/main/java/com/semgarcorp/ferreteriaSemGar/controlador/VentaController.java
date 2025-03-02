package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
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
    public List<Venta> listar() {
        return ventaService.listar();
    }

    // Obtener una venta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Integer id) {
        Venta venta = ventaService.obtenerPorId(id);
        if (venta != null) {
            return ResponseEntity.ok(venta); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
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

    // Actualizar una venta existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Venta> actualizar(@PathVariable Integer id, @RequestBody Venta venta) {
        // Obtener la venta existente
        Venta ventaExistente = ventaService.obtenerPorId(id);

        if (ventaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la venta
            venta.setIdVenta(id);

            // Aquí reemplazas completamente la venta con la información que viene en el cuerpo
            Venta ventaActualizada = ventaService.actualizar(venta);

            return ResponseEntity.ok(ventaActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una venta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Venta ventaExistente = ventaService.obtenerPorId(id);
        if (ventaExistente != null) {
            ventaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}