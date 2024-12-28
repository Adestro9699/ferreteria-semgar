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
    public ResponseEntity<TipoPago> obtenerPorId(@PathVariable Long id) {
        TipoPago tipoPago = tipoPagoService.obtenerPorId(id);
        if (tipoPago != null) {
            return new ResponseEntity<>(tipoPago, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<TipoPago> actualizar(@PathVariable Long id, @RequestBody TipoPago tipoPago) {
        TipoPago tipoPagoExistente = tipoPagoService.obtenerPorId(id);
        if (tipoPagoExistente != null) {
            tipoPago.setIdTipoPago(id); // Aseguramos que el ID se mantenga para la actualización
            return new ResponseEntity<>(tipoPagoService.actualizar(tipoPago), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar un tipo de pago por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        TipoPago tipoPagoExistente = tipoPagoService.obtenerPorId(id);
        if (tipoPagoExistente != null) {
            tipoPagoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
