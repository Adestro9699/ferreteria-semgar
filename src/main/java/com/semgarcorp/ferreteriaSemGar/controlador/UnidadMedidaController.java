package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.UnidadMedida;
import com.semgarcorp.ferreteriaSemGar.servicio.UnidadMedidaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/unidades-medida")
public class UnidadMedidaController {

    private final UnidadMedidaService unidadMedidaService;

    public UnidadMedidaController(UnidadMedidaService unidadMedidaService) {
        this.unidadMedidaService = unidadMedidaService;
    }

    // Obtener la lista de todas las unidades de medida
    @GetMapping
    public List<UnidadMedida> listar() {
        return unidadMedidaService.listar();
    }

    // Obtener una unidad de medida por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedida> obtenerPorId(@PathVariable Integer id) {
        UnidadMedida unidadMedida = unidadMedidaService.obtenerPorId(id);
        if (unidadMedida != null) {
            return ResponseEntity.ok(unidadMedida); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva unidad de medida
    @PostMapping
    public ResponseEntity<UnidadMedida> guardar(@RequestBody UnidadMedida unidadMedida) {
        // Guardar la unidad de medida usando el servicio
        UnidadMedida nuevaUnidadMedida = unidadMedidaService.guardar(unidadMedida);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaUnidadMedida.getIdUnidadMedida()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevaUnidadMedida);
    }

    // Actualizar una unidad de medida existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedida> actualizar(@PathVariable Integer id, @RequestBody UnidadMedida unidadMedida) {
        // Obtener la unidad de medida existente
        UnidadMedida unidadMedidaExistente = unidadMedidaService.obtenerPorId(id);

        if (unidadMedidaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la unidad de medida
            unidadMedida.setIdUnidadMedida(id);

            // Aquí reemplazamos completamente la unidad de medida con la información que viene en el cuerpo
            UnidadMedida unidadMedidaActualizada = unidadMedidaService.actualizar(unidadMedida);

            return ResponseEntity.ok(unidadMedidaActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una unidad de medida por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        UnidadMedida unidadMedidaExistente = unidadMedidaService.obtenerPorId(id);
        if (unidadMedidaExistente != null) {
            unidadMedidaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}