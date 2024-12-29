package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.servicio.CajaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cajas")
public class CajaController {

    private final CajaService cajaService;

    // Constructor con inyección del servicio
    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    // Endpoint GET para listar todos los registros de caja
    @GetMapping
    public ResponseEntity<List<Caja>> listar() {
        List<Caja> cajas = cajaService.listar();
        return ResponseEntity.ok(cajas);
    }

    // Endpoint GET por ID para obtener un registro de caja específico
    @GetMapping("/{id}")
    public ResponseEntity<Caja> obtenerPorId(@PathVariable Integer id) {
        Caja caja = cajaService.obtenerPorId(id);
        if (caja != null) {
            return ResponseEntity.ok(caja);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint POST para guardar un nuevo registro de caja
    @PostMapping
    public ResponseEntity<Caja> guardar(@RequestBody Caja caja) {
        Caja nuevaCaja = cajaService.guardar(caja);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaCaja.getIdCaja())
                .toUri();
        return ResponseEntity.created(location).body(nuevaCaja);
    }

    // Endpoint PUT para actualizar un registro completo de caja
    @PutMapping("/{id}")
    public ResponseEntity<Caja> actualizar(@PathVariable Integer id, @RequestBody Caja caja) {
        if (cajaService.obtenerPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        caja.setIdCaja(id);
        Caja cajaActualizada = cajaService.guardar(caja);
        return ResponseEntity.ok(cajaActualizada);
    }

    // Endpoint DELETE para eliminar un registro de caja por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        if (cajaService.obtenerPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        cajaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
