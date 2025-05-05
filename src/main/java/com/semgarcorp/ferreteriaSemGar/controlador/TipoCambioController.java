package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoCambio;
import com.semgarcorp.ferreteriaSemGar.servicio.TipoCambioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipos-cambio")
public class TipoCambioController {

    private final TipoCambioService tipoCambioService;

    public TipoCambioController(TipoCambioService tipoCambioService) {
        this.tipoCambioService = tipoCambioService;
    }

    // Obtener la lista de todos los tipos de cambio
    @GetMapping
    public List<TipoCambio> listar() {
        return tipoCambioService.listar();
    }

    // Obtener un tipo de cambio por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoCambio> obtenerPorId(@PathVariable Integer id) {
        TipoCambio tipoCambio = tipoCambioService.obtenerPorId(id);
        if (tipoCambio != null) {
            return ResponseEntity.ok(tipoCambio); // Respuesta exitosa con el tipo de cambio
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra
    }

    // Crear un nuevo tipo de cambio
    @PostMapping
    public ResponseEntity<TipoCambio> guardar(@RequestBody TipoCambio tipoCambio) {
        // Guardar el nuevo tipo de cambio
        TipoCambio nuevoTipoCambio = tipoCambioService.guardar(tipoCambio);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoTipoCambio.getIdTipoCambio()).toUri();

        // Responder con la URI en la cabecera Location
        return ResponseEntity.created(location).body(nuevoTipoCambio);
    }

    // Actualizar un tipo de cambio existente
    @PutMapping("/{id}")
    public ResponseEntity<TipoCambio> actualizar(@PathVariable Integer id, @RequestBody TipoCambio tipoCambio) {
        TipoCambio tipoCambioExistente = tipoCambioService.obtenerPorId(id);
        if (tipoCambioExistente != null) {
            tipoCambio.setIdTipoCambio(id); // Mantener el ID para la actualización
            TipoCambio tipoCambioActualizado = tipoCambioService.actualizar(tipoCambio);
            return ResponseEntity.ok(tipoCambioActualizado); // Respuesta exitosa
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra
    }

    // Eliminar un tipo de cambio por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        TipoCambio tipoCambioExistente = tipoCambioService.obtenerPorId(id);
        if (tipoCambioExistente != null) {
            tipoCambioService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta 204 sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra
    }

    @GetMapping("/hoy")
    public ResponseEntity<TipoCambio> obtenerTipoCambioActual() {
        try {
            TipoCambio tipoCambio = tipoCambioService.obtenerTipoCambioDelDia();
            return ResponseEntity.ok(tipoCambio);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
