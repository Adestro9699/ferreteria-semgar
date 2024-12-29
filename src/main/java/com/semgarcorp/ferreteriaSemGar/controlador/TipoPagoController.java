package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoPago;
import com.semgarcorp.ferreteriaSemGar.servicio.TipoPagoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tipopagos")
public class TipoPagoController {

    private final TipoPagoService tipoPagoService;

    // Constructor para inyectar el servicio
    public TipoPagoController(TipoPagoService tipoPagoService) {
        this.tipoPagoService = tipoPagoService;
    }

    // Obtener la lista de todos los tipos de pago
    @GetMapping
    public List<TipoPago> listar() {
        return tipoPagoService.listar();
    }

    // Obtener un tipo de pago por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoPago> obtenerPorId(@PathVariable Integer id) {
        TipoPago tipoPago = tipoPagoService.obtenerPorId(id);
        if (tipoPago != null) {
            return ResponseEntity.ok(tipoPago); // Uso de ResponseEntity.ok() para un retorno más limpio
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta clara con 404
    }

    // Crear un nuevo tipo de pago
    @PostMapping
    public ResponseEntity<TipoPago> guardar(@RequestBody TipoPago tipoPago) {
        // Guardar el tipo de pago usando el servicio
        TipoPago nuevoTipoPago = tipoPagoService.guardar(tipoPago);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoTipoPago.getIdTipoPago()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoTipoPago);
    }

    // Actualizar un tipo de pago existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TipoPago> actualizar(@PathVariable Integer id, @RequestBody TipoPago tipoPago) {
        TipoPago tipoPagoExistente = tipoPagoService.obtenerPorId(id);
        if (tipoPagoExistente != null) {
            tipoPago.setIdTipoPago(id); // Aseguramos que el ID se mantenga para la actualización
            TipoPago tipoPagoActualizado = tipoPagoService.actualizar(tipoPago);
            return ResponseEntity.ok(tipoPagoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
    }

    // Eliminar un tipo de pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        TipoPago tipoPagoExistente = tipoPagoService.obtenerPorId(id);
        if (tipoPagoExistente != null) {
            tipoPagoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Usamos el metodo estático "noContent"
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para devolver un 404
    }
}
