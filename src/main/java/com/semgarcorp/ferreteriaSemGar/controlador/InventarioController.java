package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Inventario;
import com.semgarcorp.ferreteriaSemGar.servicio.InventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    // Obtener la lista de todos los inventarios
    @GetMapping
    public List<Inventario> listar() {
        return inventarioService.listar();
    }

    // Obtener un inventario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Integer id) {
        Inventario inventario = inventarioService.obtenerPorId(id);
        if (inventario != null) {
            return ResponseEntity.ok(inventario); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo inventario
    @PostMapping
    public ResponseEntity<Inventario> guardar(@RequestBody Inventario inventario) {
        // Guardar el inventario usando el servicio
        Inventario nuevoInventario = inventarioService.guardar(inventario);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoInventario.getIdInventario()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoInventario);
    }

    // Actualizar un inventario existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Integer id, @RequestBody Inventario inventario) {
        // Obtener el inventario existente
        Inventario inventarioExistente = inventarioService.obtenerPorId(id);

        if (inventarioExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el inventario
            inventario.setIdInventario(id);

            // Aquí reemplazas completamente el inventario con la información que viene en el cuerpo
            Inventario inventarioActualizado = inventarioService.actualizar(inventario);

            return ResponseEntity.ok(inventarioActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un inventario por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Inventario inventarioExistente = inventarioService.obtenerPorId(id);
        if (inventarioExistente != null) {
            inventarioService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
