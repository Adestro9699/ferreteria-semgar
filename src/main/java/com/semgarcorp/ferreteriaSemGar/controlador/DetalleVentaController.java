package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleVenta;
import com.semgarcorp.ferreteriaSemGar.servicio.DetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/detalles-venta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    // Inyectar el servicio a través del constructor
    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    // Obtener la lista de todos los detalles de venta
    @GetMapping
    public List<DetalleVenta> listar() {
        return detalleVentaService.listar();
    }

    // Obtener un detalle de venta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerPorId(@PathVariable Integer id) {
        DetalleVenta detalleVenta = detalleVentaService.obtenerPorId(id);
        if (detalleVenta != null) {
            return ResponseEntity.ok(detalleVenta); // Respuesta 200 OK con el detalle de venta
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 Not Found si no se encuentra
    }

    // Crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<DetalleVenta> guardar(@RequestBody DetalleVenta detalleVenta) {
        // Guardar el detalle de venta usando el servicio
        DetalleVenta nuevoDetalleVenta = detalleVentaService.guardar(detalleVenta);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoDetalleVenta.getIdDetalleVenta())
                .toUri();

        // Devolver la respuesta con la URI en la cabecera Location
        return ResponseEntity.created(location).body(nuevoDetalleVenta);
    }

    // Actualizar un detalle de venta existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizar(@PathVariable Integer id, @RequestBody DetalleVenta detalleVenta) {
        DetalleVenta detalleVentaExistente = detalleVentaService.obtenerPorId(id);
        if (detalleVentaExistente != null) {
            detalleVenta.setIdDetalleVenta(id); // Asegurar que el ID se mantenga para la actualización
            DetalleVenta detalleVentaActualizada = detalleVentaService.actualizar(detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizada); // Respuesta 200 OK con el detalle de venta actualizado
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 Not Found si no se encuentra
    }

    // Eliminar un detalle de venta por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        DetalleVenta detalleVentaExistente = detalleVentaService.obtenerPorId(id);
        if (detalleVentaExistente != null) {
            detalleVentaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 Not Found si no se encuentra
    }
}