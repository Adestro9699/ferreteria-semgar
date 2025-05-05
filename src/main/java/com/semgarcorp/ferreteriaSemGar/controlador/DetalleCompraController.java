package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleCompra;
import com.semgarcorp.ferreteriaSemGar.servicio.DetalleCompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/detalles-compras")
public class DetalleCompraController {
    private final DetalleCompraService detalleCompraService;

    public DetalleCompraController(DetalleCompraService detalleCompraService) {
        this.detalleCompraService = detalleCompraService;
    }

    // Obtener la lista de todos los detallecompra
    @GetMapping
    public List<DetalleCompra> listar() {
        return detalleCompraService.listar();
    }

    // Obtener un detallecompra por su ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleCompra> obtenerPorId(@PathVariable Integer id) {
        DetalleCompra detallecompra = detalleCompraService.obtenerPorId(id);
        if (detallecompra != null) {
            return ResponseEntity.ok(detallecompra); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo detallecompra
    @PostMapping
    public ResponseEntity<DetalleCompra> guardar(@RequestBody DetalleCompra detallecompra) {
        // Guardar el detallecompra usando el servicio
        DetalleCompra nuevoDetalleCompra = detalleCompraService.guardar(detallecompra);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoDetalleCompra.getIdDetalleCompra()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoDetalleCompra);
    }

    @GetMapping("/compra/{idCompra}")
    public ResponseEntity<List<DetalleCompra>> obtenerDetallesPorCompra(@PathVariable Integer idCompra) {
        List<DetalleCompra> detallesCompra = detalleCompraService.obtenerDetallesPorCompra(idCompra);
        if (!detallesCompra.isEmpty()) {
            return ResponseEntity.ok(detallesCompra);
        }
        return ResponseEntity.noContent().build();
    }

    // Actualizar un detallecompra existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<DetalleCompra> actualizar(@PathVariable Integer id, @RequestBody DetalleCompra detallecompra) {
        // Obtener la detallecompra existente
        DetalleCompra detalleCompraExistente = detalleCompraService.obtenerPorId(id);

        if (detalleCompraExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la detallecompra
            detallecompra.setIdDetalleCompra(id);

            // Aquí reemplazas completamente la detallecompra con la información que viene en el cuerpo
            DetalleCompra detalleCompraActualizado = detalleCompraService.actualizar(detallecompra);

            return ResponseEntity.ok(detalleCompraActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una detallecompra por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        DetalleCompra detalleCompraExistente = detalleCompraService.obtenerPorId(id);
        if (detalleCompraExistente != null) {
            detalleCompraService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    @PostMapping("/con-utilidad-manual")
    public ResponseEntity<DetalleCompra> guardarConUtilidadManual(
            @RequestBody DetalleCompra detalleCompra,
            @RequestParam(required = false) BigDecimal utilidadManual) {

        if (utilidadManual != null) {
            detalleCompra.setPorcentajeUtilidadManual(utilidadManual);
        }

        DetalleCompra nuevoDetalle = detalleCompraService.guardar(detalleCompra);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoDetalle.getIdDetalleCompra()).toUri();

        return ResponseEntity.created(location).body(nuevoDetalle);
    }
}
