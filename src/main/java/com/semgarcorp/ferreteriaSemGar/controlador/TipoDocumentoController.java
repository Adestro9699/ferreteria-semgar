package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoDocumento;
import com.semgarcorp.ferreteriaSemGar.servicio.TipoDocumentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipos-documento")
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    // Obtener la lista de todos los tipos de documento
    @GetMapping
    public List<TipoDocumento> listar() {
        return tipoDocumentoService.listar();
    }

    // Obtener un tipo de documento por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoDocumento> obtenerPorId(@PathVariable Integer id) {
        TipoDocumento tipoDocumento = tipoDocumentoService.obtenerPorId(id);
        if (tipoDocumento != null) {
            return ResponseEntity.ok(tipoDocumento); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo tipo de documento
    @PostMapping
    public ResponseEntity<TipoDocumento> guardar(@RequestBody TipoDocumento tipoDocumento) {
        // Guardar el tipo de documento usando el servicio
        TipoDocumento nuevoTipoDocumento = tipoDocumentoService.guardar(tipoDocumento);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoTipoDocumento.getIdTipoDocumento()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoTipoDocumento);
    }

    // Actualizar un tipo de documento existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TipoDocumento> actualizar(@PathVariable Integer id, @RequestBody TipoDocumento tipoDocumento) {
        // Obtener el tipo de documento existente
        TipoDocumento tipoDocumentoExistente = tipoDocumentoService.obtenerPorId(id);

        if (tipoDocumentoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el tipo de documento
            tipoDocumento.setIdTipoDocumento(id);

            // Aquí reemplazamos completamente el tipo de documento con la información que viene en el cuerpo
            TipoDocumento tipoDocumentoActualizado = tipoDocumentoService.actualizar(tipoDocumento);

            return ResponseEntity.ok(tipoDocumentoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un tipo de documento por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        TipoDocumento tipoDocumentoExistente = tipoDocumentoService.obtenerPorId(id);
        if (tipoDocumentoExistente != null) {
            tipoDocumentoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}