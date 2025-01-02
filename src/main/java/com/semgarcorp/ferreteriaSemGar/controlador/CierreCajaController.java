package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.CierreCaja;
import com.semgarcorp.ferreteriaSemGar.servicio.CierreCajaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cierrescajas")
public class CierreCajaController {

    private final CierreCajaService cierreCajaService;

    public CierreCajaController(CierreCajaService cierreCajaService) {
        this.cierreCajaService = cierreCajaService;
    }

    // Obtener la lista de todos los cierres de caja
    @GetMapping
    public List<CierreCaja> listar() {
        return cierreCajaService.listar();
    }

    // Obtener un cierre de caja por su ID
    @GetMapping("/{id}")
    public ResponseEntity<CierreCaja> obtenerPorId(@PathVariable Integer id) {
        CierreCaja cierreCaja = cierreCajaService.obtenerPorId(id);
        if (cierreCaja != null) {
            return ResponseEntity.ok(cierreCaja); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo cierre de caja
    @PostMapping
    public ResponseEntity<CierreCaja> guardar(@RequestBody CierreCaja cierreCaja) {
        // Guardar el cierre de caja usando el servicio
        CierreCaja nuevoCierreCaja = cierreCajaService.guardar(cierreCaja);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoCierreCaja.getIdCierreCaja()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoCierreCaja);
    }

    // Actualizar un cierre de caja existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<CierreCaja> actualizar(@PathVariable Integer id, @RequestBody CierreCaja cierreCaja) {
        // Obtener el cierre de caja existente
        CierreCaja cierreCajaExistente = cierreCajaService.obtenerPorId(id);

        if (cierreCajaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el cierre de caja
            cierreCaja.setIdCierreCaja(id);

            // Aquí reemplazas completamente el cierre de caja con la información que viene en el cuerpo
            CierreCaja cierreCajaActualizado = cierreCajaService.actualizar(cierreCaja);

            return ResponseEntity.ok(cierreCajaActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un cierre de caja por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        CierreCaja cierreCajaExistente = cierreCajaService.obtenerPorId(id);
        if (cierreCajaExistente != null) {
            cierreCajaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
