package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import com.semgarcorp.ferreteriaSemGar.servicio.TransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {
    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    // Obtener la lista de todos las transferencias
    @GetMapping
    public List<Transferencia> listar() {
        return transferenciaService.listar();
    }

    // Obtener un transferencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> obtenerPorId(@PathVariable Integer id) {
        Transferencia transferencia = transferenciaService.obtenerPorId(id);
        if (transferencia != null) {
            return ResponseEntity.ok(transferencia); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo transferencia
    @PostMapping
    public ResponseEntity<Transferencia> guardar(@RequestBody Transferencia transferencia) {
        // Guardar el transferencia usando el servicio
        Transferencia nuevaTransferencia = transferenciaService.guardar(transferencia);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaTransferencia.getIdTransferencia()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevaTransferencia);
    }

    // Actualizar un transferencia existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Transferencia> actualizar(@PathVariable Integer id, @RequestBody Transferencia transferencia) {
        // Obtener el transferencia existente
        Transferencia transferenciaExistente = transferenciaService.obtenerPorId(id);

        if (transferenciaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el transferencia
            transferencia.setIdTransferencia(id);

            // Aquí reemplazas completamente el transferencia con la información que viene en el cuerpo
            Transferencia transferenciaActualizado = transferenciaService.actualizar(transferencia);

            return ResponseEntity.ok(transferenciaActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un transferencia por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Transferencia transferenciaExistente = transferenciaService.obtenerPorId(id);
        if (transferenciaExistente != null) {
            transferenciaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
