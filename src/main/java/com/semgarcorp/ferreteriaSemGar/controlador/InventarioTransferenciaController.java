package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.InventarioTransferencia;
import com.semgarcorp.ferreteriaSemGar.servicio.InventarioTransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventariosTransferencias")
public class InventarioTransferenciaController {

    private final InventarioTransferenciaService inventarioTransferenciaService;

    public InventarioTransferenciaController(InventarioTransferenciaService inventarioTransferenciaService) {
        this.inventarioTransferenciaService = inventarioTransferenciaService;
    }

    // Obtener la lista de todos los inventarioTransferencias
    @GetMapping
    public List<InventarioTransferencia> listar() {
        return inventarioTransferenciaService.listar();
    }

    // Obtener un inventarioTransferencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<InventarioTransferencia> obtenerPorId(@PathVariable Integer id) {
        InventarioTransferencia inventarioTransferencia = inventarioTransferenciaService.obtenerPorId(id);
        if (inventarioTransferencia != null) {
            return ResponseEntity.ok(inventarioTransferencia); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo inventarioTransferencia
    @PostMapping
    public ResponseEntity<InventarioTransferencia> guardar(@RequestBody InventarioTransferencia inventarioTransferencia) {
        // Guardar el inventarioTransferencia usando el servicio
        InventarioTransferencia nuevoInventarioTransferencia = inventarioTransferenciaService.guardar(inventarioTransferencia);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoInventarioTransferencia.getIdInventarioTransferencia()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoInventarioTransferencia);
    }

    // Actualizar un inventarioTransferencia existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<InventarioTransferencia> actualizar(@PathVariable Integer id, @RequestBody InventarioTransferencia inventarioTransferencia) {
        // Obtener el inventarioTransferencia existente
        InventarioTransferencia inventarioTransferenciaExistente = inventarioTransferenciaService.obtenerPorId(id);

        if (inventarioTransferenciaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el inventarioTransferencia
            inventarioTransferencia.setIdInventarioTransferencia(id);

            // Aquí reemplazas completamente el inventarioTransferencia con la información que viene en el cuerpo
            InventarioTransferencia inventarioTransferenciaActualizado = inventarioTransferenciaService.actualizar(inventarioTransferencia);

            return ResponseEntity.ok(inventarioTransferenciaActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un inventarioTransferencia por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        InventarioTransferencia inventarioTransferenciaExistente = inventarioTransferenciaService.obtenerPorId(id);
        if (inventarioTransferenciaExistente != null) {
            inventarioTransferenciaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
