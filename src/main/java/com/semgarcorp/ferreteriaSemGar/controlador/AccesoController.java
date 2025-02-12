package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.servicio.AccesoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accesos")
public class AccesoController {

    private final AccesoService accesoService;

    public AccesoController(AccesoService accesoService) {
        this.accesoService = accesoService;
    }

    // Obtener la lista de todos los accesos
    @GetMapping
    public List<Acceso> listar() {
        return accesoService.listar();
    }

    // Obtener un acceso por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Acceso> obtenerPorId(@PathVariable Integer id) {
        Acceso acceso = accesoService.obtenerPorId(id);
        if (acceso != null) {
            return ResponseEntity.ok(acceso); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo acceso
    @PostMapping
    public ResponseEntity<Acceso> guardar(@RequestBody Acceso acceso) {
        // Guardar el acceso usando el servicio
        Acceso nuevoAcceso = accesoService.guardar(acceso);
        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoAcceso.getIdAcceso()).toUri();
        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoAcceso);
    }

    // Actualizar un acceso existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Acceso> actualizar(@PathVariable Integer id, @RequestBody Acceso acceso) {
        // Obtener el acceso existente
        Acceso accesoExistente = accesoService.obtenerPorId(id);
        if (accesoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el acceso
            acceso.setIdAcceso(id);
            // Aquí reemplazas completamente el acceso con la información que viene en el cuerpo
            Acceso accesoActualizado = accesoService.actualizar(acceso);
            return ResponseEntity.ok(accesoActualizado); // Usamos el método estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un acceso por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Acceso accesoExistente = accesoService.obtenerPorId(id);
        if (accesoExistente != null) {
            accesoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    // Nuevo endpoint para actualizar los permisos de un acceso
    @PutMapping("/{id}/permisos")
    public ResponseEntity<Acceso> actualizarPermisos(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> requestBody) {
        // Obtener los nuevos permisos del cuerpo de la solicitud
        Map<String, Boolean> nuevosPermisos = (Map<String, Boolean>) requestBody.get("permisos");

        // Validar que los permisos no sean nulos
        if (nuevosPermisos == null || nuevosPermisos.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Respuesta 400 Bad Request si no hay permisos
        }

        // Llamar al servicio para actualizar los permisos
        try {
            Acceso accesoActualizado = accesoService.actualizarPermisos(id, nuevosPermisos);
            return ResponseEntity.ok(accesoActualizado); // Respuesta 200 OK con el acceso actualizado
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Respuesta 500 en caso de error
        }
    }
}