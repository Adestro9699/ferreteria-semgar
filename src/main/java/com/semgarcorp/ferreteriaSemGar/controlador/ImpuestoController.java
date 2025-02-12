package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import com.semgarcorp.ferreteriaSemGar.servicio.ImpuestoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/impuestos")
public class ImpuestoController {
    private final ImpuestoService impuestoService;

    public ImpuestoController(ImpuestoService impuestoService) {
        this.impuestoService = impuestoService;
    }

    // Obtener la lista de todas los impuestos
    @GetMapping
    public List<Impuesto> listar() {
        return impuestoService.listar();
    }

    // Obtener un impuesto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Impuesto> obtenerPorId(@PathVariable Integer id) {
        Impuesto impuesto = impuestoService.obtenerPorId(id);
        if (impuesto != null) {
            return ResponseEntity.ok(impuesto); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo impuesto
    @PostMapping
    public ResponseEntity<Impuesto> guardar(@RequestBody Impuesto impuesto) {
        Impuesto nuevoImpuesto = impuestoService.guardar(impuesto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoImpuesto.getIdImpuesto()).toUri();

        return ResponseEntity.created(location).body(nuevoImpuesto);
    }

    // Actualizar un impuesto existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Impuesto> actualizar(@PathVariable Integer id, @RequestBody Impuesto impuesto) {
        // Obtener el impuesto existente
        Impuesto impuestoExistente = impuestoService.obtenerPorId(id);

        if (impuestoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar impuesto
            impuesto.setIdImpuesto(id);

            // Aquí reemplazas completamente el impuesto con la información que viene en el cuerpo
            Impuesto impuestoActualizada = impuestoService.actualizar(impuesto);

            return ResponseEntity.ok(impuestoActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un impuesto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Impuesto impuestoExistente = impuestoService.obtenerPorId(id);
        if (impuestoExistente != null) {
            impuestoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
