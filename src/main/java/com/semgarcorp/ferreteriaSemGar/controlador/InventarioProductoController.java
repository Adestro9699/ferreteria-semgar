package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.InventarioProducto;
import com.semgarcorp.ferreteriaSemGar.servicio.InventarioProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/inventariosProductos")
public class InventarioProductoController {
    private final InventarioProductoService inventarioProductoService;

    public InventarioProductoController(InventarioProductoService inventarioProductoService) {
        this.inventarioProductoService = inventarioProductoService;
    }

    // Obtener la lista de todas las inventarioProductos
    @GetMapping
    public List<InventarioProducto> listar() {
        return inventarioProductoService.listar();
    }

    // Obtener una inventarioProducto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<InventarioProducto> obtenerPorId(@PathVariable Integer id) {
        InventarioProducto inventarioProducto = inventarioProductoService.obtenerPorId(id);
        if (inventarioProducto != null) {
            return ResponseEntity.ok(inventarioProducto); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva inventarioProducto
    @PostMapping
    public ResponseEntity<InventarioProducto> guardar(@RequestBody InventarioProducto inventarioProducto) {
        InventarioProducto nuevoInventarioProducto = inventarioProductoService.guardar(inventarioProducto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoInventarioProducto.getIdInventarioProducto()).toUri();

        return ResponseEntity.created(location).body(nuevoInventarioProducto);
    }

    // Actualizar una inventarioProducto existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<InventarioProducto> actualizar(@PathVariable Integer id, @RequestBody InventarioProducto inventarioProducto) {
        // Obtener la inventarioProducto existente
        InventarioProducto inventarioProductoExistente = inventarioProductoService.obtenerPorId(id);

        if (inventarioProductoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la inventarioProducto
            inventarioProducto.setIdInventarioProducto(id);

            // Aquí reemplazas completamente la inventarioProducto con la información que viene en el cuerpo
            InventarioProducto inventarioProductoActualizado = inventarioProductoService.actualizar(inventarioProducto);

            return ResponseEntity.ok(inventarioProductoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una inventarioProducto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        InventarioProducto inventarioProductoExistente = inventarioProductoService.obtenerPorId(id);
        if (inventarioProductoExistente != null) {
            inventarioProductoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
