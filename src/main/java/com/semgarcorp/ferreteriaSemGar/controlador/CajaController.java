package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.CajaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.servicio.CajaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
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
    public ResponseEntity<Caja> guardar(@RequestBody CajaDTO cajaDTO) {
        // Llamar al servicio para crear la caja
        Caja nuevaCaja = cajaService.crearCaja(cajaDTO);

        // Construir la URI de la nueva caja
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaCaja.getIdCaja())
                .toUri();

        // Retornar la respuesta con la nueva caja y la URI de ubicación
        return ResponseEntity.created(location).body(nuevaCaja);
    }

    // Endpoint PUT para actualizar un registro completo de caja
    @PutMapping("/{id}")
    public ResponseEntity<Caja> actualizar(@PathVariable Integer id, @RequestBody Caja caja) {
        if (cajaService.obtenerPorId(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        caja.setIdCaja(id);
        Caja cajaActualizada = cajaService.actualizar(caja);
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

    @PostMapping("/abrir")
    public ResponseEntity<Caja> abrirCaja(@RequestBody CajaDTO cajaDTO) {
        // Llamar al servicio para abrir la caja
        Caja cajaAbierta = cajaService.abrirCaja(cajaDTO);

        // Retornar la respuesta con la caja actualizada
        return ResponseEntity.ok(cajaAbierta);
    }

    @PostMapping("/cerrar")
    public ResponseEntity<Caja> cerrarCaja(@RequestBody CajaDTO cajaDTO) {
        try {
            Caja cajaCerrada = cajaService.cerrarCaja(cajaDTO);
            return ResponseEntity.ok(cajaCerrada);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Maneja tanto IllegalArgumentException como IllegalStateException
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idCaja}/entrada-manual")
    public ResponseEntity<Caja> registrarEntradaManual(
            @PathVariable Integer idCaja,
            @RequestParam BigDecimal monto,
            @RequestParam(required = false) String observaciones) {
        Caja caja = cajaService.registrarEntradaManual(idCaja, monto, observaciones);
        return ResponseEntity.ok(caja);
    }

    @PostMapping("/{idCaja}/salida-manual")
    public ResponseEntity<Caja> registrarSalidaManual(
            @PathVariable Integer idCaja,
            @RequestParam BigDecimal monto,
            @RequestParam(required = false) String observaciones) {
        Caja caja = cajaService.registrarSalidaManual(idCaja, monto, observaciones);
        return ResponseEntity.ok(caja);
    }
}
