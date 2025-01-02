package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.servicio.AlmacenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    // Obtener la lista de todos los almacenes
    @GetMapping
    public List<Almacen> listar() {
        return almacenService.listar();
    }

    // Obtener un almacen por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Almacen> obtenerPorId(@PathVariable Integer id) {
        Almacen almacen = almacenService.obtenerPorId(id);
        if (almacen != null) {
            return ResponseEntity.ok(almacen); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo almacen
    @PostMapping
    public ResponseEntity<Almacen> guardar(@RequestBody Almacen almacen) {
        // Guardar el almacen usando el servicio
        Almacen nuevoAlmacen = almacenService.guardar(almacen);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoAlmacen.getIdAlmacen()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoAlmacen);
    }

    // Actualizar un almacen existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Almacen> actualizar(@PathVariable Integer id, @RequestBody Almacen almacen) {
        // Obtener el almacen existente
        Almacen almacenExistente = almacenService.obtenerPorId(id);

        if (almacenExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el almacen
            almacen.setIdAlmacen(id);

            // Aquí reemplazas completamente el almacen con la información que viene en el cuerpo
            Almacen almacenActualizado = almacenService.actualizar(almacen);

            return ResponseEntity.ok(almacenActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un almacen por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Almacen almacenExistente = almacenService.obtenerPorId(id);
        if (almacenExistente != null) {
            almacenService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
