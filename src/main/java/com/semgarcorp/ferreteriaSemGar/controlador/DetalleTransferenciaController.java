package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;
import com.semgarcorp.ferreteriaSemGar.servicio.DetalleTransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/detalles-transferencia")
public class DetalleTransferenciaController {
    
    private final DetalleTransferenciaService detalleTransferenciaService;
    
    public DetalleTransferenciaController(DetalleTransferenciaService detalleTransferenciaService) {
        this.detalleTransferenciaService = detalleTransferenciaService;
    }
    
    // Obtener la lista de todos los detalles de transferencia
    @GetMapping
    public List<DetalleTransferencia> listar() {
        return detalleTransferenciaService.listar();
    }
    
    // Obtener un detalle por su ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleTransferencia> obtenerPorId(@PathVariable Integer id) {
        DetalleTransferencia detalle = detalleTransferenciaService.obtenerPorId(id);
        if (detalle != null) {
            return ResponseEntity.ok(detalle);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    // Crear un nuevo detalle de transferencia
    @PostMapping
    public ResponseEntity<DetalleTransferencia> guardar(@RequestBody DetalleTransferencia detalleTransferencia) {
        DetalleTransferencia nuevoDetalle = detalleTransferenciaService.guardar(detalleTransferencia);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoDetalle.getIdDetalleTransferencia()).toUri();
        
        return ResponseEntity.created(location).body(nuevoDetalle);
    }
    
    // Actualizar un detalle existente
    @PutMapping("/{id}")
    public ResponseEntity<DetalleTransferencia> actualizar(@PathVariable Integer id, @RequestBody DetalleTransferencia detalleTransferencia) {
        DetalleTransferencia detalleExistente = detalleTransferenciaService.obtenerPorId(id);
        
        if (detalleExistente != null) {
            detalleTransferencia.setIdDetalleTransferencia(id);
            DetalleTransferencia detalleActualizado = detalleTransferenciaService.actualizar(detalleTransferencia);
            return ResponseEntity.ok(detalleActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // Eliminar un detalle por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        DetalleTransferencia detalleExistente = detalleTransferenciaService.obtenerPorId(id);
        if (detalleExistente != null) {
            detalleTransferenciaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    // Buscar detalles por transferencia
    @GetMapping("/transferencia/{idTransferencia}")
    public ResponseEntity<List<DetalleTransferencia>> buscarPorTransferencia(@PathVariable Integer idTransferencia) {
        List<DetalleTransferencia> detalles = detalleTransferenciaService.buscarPorTransferencia(idTransferencia);
        return ResponseEntity.ok(detalles);
    }
    
    // Buscar detalles por producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<DetalleTransferencia>> buscarPorProducto(@PathVariable Integer idProducto) {
        List<DetalleTransferencia> detalles = detalleTransferenciaService.buscarPorProducto(idProducto);
        return ResponseEntity.ok(detalles);
    }
} 