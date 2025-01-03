package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Proveedor;
import com.semgarcorp.ferreteriaSemGar.servicio.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    // Obtener la lista de todos los proveedores
    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    // Obtener un proveedor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer id) {
        Proveedor proveedor = proveedorService.obtenerPorId(id);
        if (proveedor != null) {
            return ResponseEntity.ok(proveedor); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo proveedor
    @PostMapping
    public ResponseEntity<Proveedor> guardar(@RequestBody Proveedor proveedor) {
        // Guardar el proveedor usando el servicio
        Proveedor nuevoProveedor = proveedorService.guardar(proveedor);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoProveedor.getIdProveedor()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoProveedor);
    }

    // Actualizar un proveedor existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        // Obtener el proveedor existente
        Proveedor proveedorExistente = proveedorService.obtenerPorId(id);

        if (proveedorExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el proveedor
            proveedor.setIdProveedor(id);

            // Aquí reemplazas completamente el proveedor con la información que viene en el cuerpo
            Proveedor proveedorActualizado = proveedorService.actualizar(proveedor);

            return ResponseEntity.ok(proveedorActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un proveedor por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Proveedor proveedorExistente = proveedorService.obtenerPorId(id);
        if (proveedorExistente != null) {
            proveedorService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
