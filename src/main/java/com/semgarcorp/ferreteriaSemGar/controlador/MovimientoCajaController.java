package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.MovimientoCaja;
import com.semgarcorp.ferreteriaSemGar.servicio.MovimientoCajaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movimientos-caja")
public class MovimientoCajaController {

    private final MovimientoCajaService movimientoCajaService;

    public MovimientoCajaController(MovimientoCajaService movimientoCajaService) {
        this.movimientoCajaService = movimientoCajaService;
    }

    // Obtener la lista de todos los movimientos de caja
    @GetMapping
    public List<MovimientoCaja> listar() {
        return movimientoCajaService.listar();
    }

    // Obtener un movimiento de caja por su ID
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoCaja> obtenerPorId(@PathVariable Integer id) {
        MovimientoCaja movimiento = movimientoCajaService.obtenerPorId(id);
        if (movimiento != null) {
            return ResponseEntity.ok(movimiento); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo movimiento de caja
    @PostMapping
    public ResponseEntity<MovimientoCaja> guardar(@RequestBody MovimientoCaja movimientoCaja) {
        // Guardar el movimiento usando el servicio
        MovimientoCaja nuevoMovimiento = movimientoCajaService.guardar(movimientoCaja);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoMovimiento.getIdMovimiento()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoMovimiento);
    }

    // Actualizar un movimiento de caja existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoCaja> actualizar(@PathVariable Integer id, @RequestBody MovimientoCaja movimientoCaja) {
        // Obtener el movimiento existente
        MovimientoCaja movimientoExistente = movimientoCajaService.obtenerPorId(id);

        if (movimientoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el movimiento
            movimientoCaja.setIdMovimiento(id);

            // Aquí reemplazamos completamente el movimiento con la información que viene en el cuerpo
            MovimientoCaja movimientoActualizado = movimientoCajaService.actualizar(movimientoCaja);

            return ResponseEntity.ok(movimientoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un movimiento de caja por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        MovimientoCaja movimientoExistente = movimientoCajaService.obtenerPorId(id);
        if (movimientoExistente != null) {
            movimientoCajaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}