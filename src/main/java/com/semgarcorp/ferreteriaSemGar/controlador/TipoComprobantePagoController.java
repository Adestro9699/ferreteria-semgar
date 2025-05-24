package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoComprobantePago;
import com.semgarcorp.ferreteriaSemGar.servicio.TipoComprobantePagoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipo-comprobantes-pago")
public class TipoComprobantePagoController {

    private final TipoComprobantePagoService tipoComprobantePagoService;

    public TipoComprobantePagoController(TipoComprobantePagoService tipoComprobantePagoService) {
        this.tipoComprobantePagoService = tipoComprobantePagoService;
    }

    @GetMapping
    public List<TipoComprobantePago> listar() {
        return tipoComprobantePagoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoComprobantePago> obtenerPorId(@PathVariable Integer id) {
        TipoComprobantePago tipoComprobantePago = tipoComprobantePagoService.obtenerPorId(id);
        if (tipoComprobantePago != null) {
            return ResponseEntity.ok(tipoComprobantePago); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    @PostMapping
    public ResponseEntity<TipoComprobantePago> guardar(@RequestBody TipoComprobantePago tipoComprobantePago) {
        TipoComprobantePago nuevoTipoComprobantePago = tipoComprobantePagoService.guardar(tipoComprobantePago);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoTipoComprobantePago.getIdTipoComprobantePago()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoTipoComprobantePago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoComprobantePago> actualizar(@PathVariable Integer id, @RequestBody TipoComprobantePago tipoComprobantePago) {
        TipoComprobantePago tipoComprobantePagoExistente = tipoComprobantePagoService.obtenerPorId(id);

        if (tipoComprobantePagoExistente != null) {
            tipoComprobantePago.setIdTipoComprobantePago(id);

            TipoComprobantePago tipoComprobantePagoActualizado = tipoComprobantePagoService.actualizar(tipoComprobantePago);

            return ResponseEntity.ok(tipoComprobantePagoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Integer id) {
        try {
            TipoComprobantePago tipoExistente = tipoComprobantePagoService.obtenerPorId(id);
            if (tipoExistente == null) {
                return ResponseEntity.notFound().build();
            }

            tipoComprobantePagoService.eliminar(id);
            return ResponseEntity.noContent().build(); // 204

        } catch (DataIntegrityViolationException e) {
            // Error de FK (ventas asociadas)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar: existen ventas asociadas a este comprobante.");

        } catch (TransactionSystemException e) {
            // Maneja errores transaccionales (timeout, etc.)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error transaccional: la operación fue revertida. Contacte al administrador.");

        } catch (Exception e) {
            // Otros errores
            return ResponseEntity.internalServerError()
                    .body("Error interno: " + e.getMessage());
        }
    }
}