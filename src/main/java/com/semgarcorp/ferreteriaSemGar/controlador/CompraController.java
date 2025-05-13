package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Compra;
import com.semgarcorp.ferreteriaSemGar.servicio.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Obtener la lista de todos los compra
    @GetMapping
    public List<Compra> listar() {
        return compraService.listar();
    }

    // Obtener un compra por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Compra> obtenerPorId(@PathVariable Integer id) {
        Compra compra = compraService.obtenerPorId(id);
        if (compra != null) {
            return ResponseEntity.ok(compra); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo compra
    @PostMapping
    public ResponseEntity<Compra> guardar(@RequestBody Compra compra) {
        // Guardar el compra usando el servicio
        Compra nuevoCompra = compraService.guardar(compra);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoCompra.getIdCompra()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoCompra);
    }

    // Actualizar un compra existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Compra> actualizar(@PathVariable Integer id, @RequestBody Compra compra) {
        // Obtener la compra existente
        Compra compraExistente = compraService.obtenerPorId(id);

        if (compraExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la compra
            compra.setIdCompra(id);

            // Aquí reemplazas completamente la compra con la información que viene en el cuerpo
            Compra compraActualizado = compraService.actualizar(compra);

            return ResponseEntity.ok(compraActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una compra por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Compra compraExistente = compraService.obtenerPorId(id);
        if (compraExistente != null) {
            compraService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    // Eliminar múltiples compras por sus IDs
    @DeleteMapping("/eliminar-multiples")
    public ResponseEntity<Void> eliminarMultiples(@RequestBody List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Integer id : ids) {
                Compra compraExistente = compraService.obtenerPorId(id);
                if (compraExistente != null) {
                    compraService.eliminar(id);
                }
            }
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
