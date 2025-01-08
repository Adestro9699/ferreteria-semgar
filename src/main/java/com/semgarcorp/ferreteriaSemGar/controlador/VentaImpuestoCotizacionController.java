package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.VentaImpuestoCotizacion;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaImpuestoCotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ventaImpuestoCotizacion")
public class VentaImpuestoCotizacionController {

    private final VentaImpuestoCotizacionService ventaImpuestoCotizacionService;

    public VentaImpuestoCotizacionController(VentaImpuestoCotizacionService ventaImpuestoCotizacionService) {
        this.ventaImpuestoCotizacionService = ventaImpuestoCotizacionService;
    }

    // Obtener la lista de todas las ventaImpuestoCotizacions
    @GetMapping
    public List<VentaImpuestoCotizacion> listar() {
        return ventaImpuestoCotizacionService.listar();
    }

    // Obtener una ventaImpuestoCotizacion por su ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaImpuestoCotizacion> obtenerPorId(@PathVariable Integer id) {
        VentaImpuestoCotizacion ventaImpuestoCotizacion = ventaImpuestoCotizacionService.obtenerPorId(id);
        if (ventaImpuestoCotizacion != null) {
            return ResponseEntity.ok(ventaImpuestoCotizacion); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva ventaImpuestoCotizacion
    @PostMapping
    public ResponseEntity<VentaImpuestoCotizacion> guardar(@RequestBody VentaImpuestoCotizacion ventaImpuestoCotizacion) {
        VentaImpuestoCotizacion nuevaVentaImpuestoCotizacion = ventaImpuestoCotizacionService.guardar(ventaImpuestoCotizacion);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaVentaImpuestoCotizacion.getIdVentaImpuestoCotizacion()).toUri();

        return ResponseEntity.created(location).body(nuevaVentaImpuestoCotizacion);
    }

    // Actualizar una ventaImpuestoCotizacion existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<VentaImpuestoCotizacion> actualizar(@PathVariable Integer id, @RequestBody VentaImpuestoCotizacion ventaImpuestoCotizacion) {
        // Obtener la ventaImpuestoCotizacion existente
        VentaImpuestoCotizacion ventaImpuestoCotizacionExistente = ventaImpuestoCotizacionService.obtenerPorId(id);

        if (ventaImpuestoCotizacionExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la ventaImpuestoCotizacion
            ventaImpuestoCotizacion.setIdVentaImpuestoCotizacion(id);

            // Aquí reemplazas completamente la ventaImpuestoCotizacion con la información que viene en el cuerpo
            VentaImpuestoCotizacion ventaImpuestoCotizacionActualizada = ventaImpuestoCotizacionService.actualizar(ventaImpuestoCotizacion);

            return ResponseEntity.ok(ventaImpuestoCotizacionActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una ventaImpuestoCotizacion por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        VentaImpuestoCotizacion ventaImpuestoCotizacionExistente = ventaImpuestoCotizacionService.obtenerPorId(id);
        if (ventaImpuestoCotizacionExistente != null) {
            ventaImpuestoCotizacionService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
