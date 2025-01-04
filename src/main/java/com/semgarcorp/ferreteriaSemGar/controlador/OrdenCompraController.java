package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.OrdenCompra;
import com.semgarcorp.ferreteriaSemGar.servicio.OrdenCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ordenCompras")
public class OrdenCompraController {
    private final OrdenCompraService ordenCompraService;

    public OrdenCompraController(OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }

    // Obtener la lista de todos los ordenCompra
    @GetMapping
    public List<OrdenCompra> listar() {
        return ordenCompraService.listar();
    }

    // Obtener un ordenCompra por su ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompra> obtenerPorId(@PathVariable Integer id) {
        OrdenCompra ordenCompra = ordenCompraService.obtenerPorId(id);
        if (ordenCompra != null) {
            return ResponseEntity.ok(ordenCompra); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo ordenCompra
    @PostMapping
    public ResponseEntity<OrdenCompra> guardar(@RequestBody OrdenCompra ordenCompra) {
        // Guardar el ordenCompra usando el servicio
        OrdenCompra nuevoOrdenCompra = ordenCompraService.guardar(ordenCompra);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoOrdenCompra.getIdOrdenCompra()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoOrdenCompra);
    }

    // Actualizar un ordenCompra existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompra> actualizar(@PathVariable Integer id, @RequestBody OrdenCompra ordenCompra) {
        // Obtener el ordenCompra existente
        OrdenCompra ordenCompraExistente = ordenCompraService.obtenerPorId(id);

        if (ordenCompraExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el ordenCompra
            ordenCompra.setIdOrdenCompra(id);

            // Aquí reemplazas completamente el ordenCompra con la información que viene en el cuerpo
            OrdenCompra ordenCompraActualizado = ordenCompraService.actualizar(ordenCompra);

            return ResponseEntity.ok(ordenCompraActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un ordenCompra por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        OrdenCompra ordenCompraExistente = ordenCompraService.obtenerPorId(id);
        if (ordenCompraExistente != null) {
            ordenCompraService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
