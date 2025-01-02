package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.servicio.AccesoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/accesos")
public class AccesoController {

    private final AccesoService accesoService;

    public AccesoController(AccesoService accesoService) {
        this.accesoService = accesoService;
    }

    // Obtener la lista de todos los usuarios
    @GetMapping
    public List<Acceso> listar() {
        return accesoService.listar();
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Acceso> obtenerPorId(@PathVariable Integer id) {
        Acceso acceso = accesoService.obtenerPorId(id);
        if (acceso != null) {
            return ResponseEntity.ok(acceso); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<Acceso> guardar(@RequestBody Acceso acceso) {
        // Guardar el usuario usando el servicio
        Acceso nuevoAcceso = accesoService.guardar(acceso);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoAcceso.getIdAcceso()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoAcceso);
    }

    // Actualizar un usuario existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Acceso> actualizar(@PathVariable Integer id, @RequestBody Acceso acceso) {
        // Obtener el usuario existente
        Acceso accesoExistente = accesoService.obtenerPorId(id);

        if (accesoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el usuario
            acceso.setIdAcceso(id);

            // Aquí reemplazas completamente el usuario con la información que viene en el cuerpo
            Acceso accesoActualizado = accesoService.actualizar(acceso);

            return ResponseEntity.ok(accesoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un usuario por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Acceso accesoExistente = accesoService.obtenerPorId(id);
        if (accesoExistente != null) {
            accesoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
