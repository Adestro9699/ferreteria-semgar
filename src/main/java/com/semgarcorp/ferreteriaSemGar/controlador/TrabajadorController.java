package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.servicio.TrabajadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    // Obtener la lista de todos los trabajadores
    @GetMapping
    public List<Trabajador> listar() {
        return trabajadorService.listar();
    }

    // Obtener un trabajador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Trabajador> obtenerPorId(@PathVariable Integer id) {
        Trabajador trabajador = trabajadorService.obtenerPorId(id);
        if (trabajador != null) {
            return ResponseEntity.ok(trabajador); // Simplificado con "ok()"
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta más clara y consistente
    }

    // Crear un nuevo trabajador
    @PostMapping
    public ResponseEntity<Trabajador> guardar(@RequestBody Trabajador trabajador) {
        // Guardar el trabajador usando el servicio
        Trabajador nuevoTrabajador = trabajadorService.guardar(trabajador);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoTrabajador.getIdTrabajador()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoTrabajador);
    }

    // Actualizar un trabajador existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Trabajador> actualizar(@PathVariable Integer id, @RequestBody Trabajador trabajador) {
        Trabajador trabajadorExistente = trabajadorService.obtenerPorId(id);
        if (trabajadorExistente != null) {
            trabajador.setIdTrabajador(id); // Aseguramos que el ID se mantenga para la actualización
            Trabajador trabajadorActualizado = trabajadorService.actualizar(trabajador);
            return ResponseEntity.ok(trabajadorActualizado); // Usamos el metodo estático "ok"
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos el builder
    }

    // Eliminar un trabajador por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Trabajador trabajadorExistente = trabajadorService.obtenerPorId(id);
        if (trabajadorExistente != null) {
            trabajadorService.eliminar(id);
            return ResponseEntity.noContent().build(); // Usamos el metodo estático "noContent" para devolver un 204
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para devolver un 404
    }
}
