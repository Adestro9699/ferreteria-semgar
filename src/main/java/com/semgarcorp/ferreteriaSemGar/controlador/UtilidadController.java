package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.Utilidad;
import com.semgarcorp.ferreteriaSemGar.servicio.UtilidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/utilidades")
public class UtilidadController {

    private final UtilidadService utilidadService;

    public UtilidadController(UtilidadService utilidadService) {
        this.utilidadService = utilidadService;
    }

    // Obtener la lista de todas las utilidades
    @GetMapping
    public List<Utilidad> listar() {
        return utilidadService.listar();
    }

    // Obtener una utilidad por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Utilidad> obtenerPorId(@PathVariable Integer id) {
        Utilidad utilidad = utilidadService.obtenerPorId(id);
        if (utilidad != null) {
            return ResponseEntity.ok(utilidad); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva utilidad
    @PostMapping
    public ResponseEntity<Utilidad> guardar(@RequestBody Utilidad utilidad) {
        try {
            // Validar que tenga al menos categoría o producto
            if (utilidad.getCategoria() == null && utilidad.getProducto() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Si no viene porcentaje, asignar null
            if (utilidad.getPorcentajeUtilidad() != null) {
                // Validar rango si se proporciona
                if (utilidad.getPorcentajeUtilidad() < 0 || utilidad.getPorcentajeUtilidad() > 100) {
                    return ResponseEntity.badRequest().build();
                }
            }

            Utilidad guardada = utilidadService.guardar(utilidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Actualizar una utilidad existente
    @PutMapping("/{id}")
    public ResponseEntity<Utilidad> actualizar(@PathVariable Integer id, @RequestBody Utilidad utilidad) {
        // Obtener la utilidad existente
        Utilidad utilidadExistente = utilidadService.obtenerPorId(id);

        if (utilidadExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la utilidad
            utilidad.setIdUtilidad(id);

            // Aquí reemplazas completamente la utilidad con la información que viene en el cuerpo
            Utilidad utilidadActualizada = utilidadService.actualizar(utilidad);

            return ResponseEntity.ok(utilidadActualizada); // Usamos el método estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una utilidad por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Utilidad utilidadExistente = utilidadService.obtenerPorId(id);
        if (utilidadExistente != null) {
            utilidadService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    // Obtener utilidad por producto
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<BigDecimal> obtenerUtilidadPorProducto(@PathVariable Integer idProducto) {
        Producto producto = new Producto();
        producto.setIdProducto(idProducto);
        return utilidadService.obtenerPorcentajeUtilidadPorProducto(producto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener utilidad por categoría
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<BigDecimal> obtenerUtilidadPorCategoria(@PathVariable Integer idCategoria) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(idCategoria);
        return utilidadService.obtenerPorcentajeUtilidadPorCategoria(categoria)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Nuevo endpoint para obtener todas las utilidades de productos
    @GetMapping("/producto")
    public ResponseEntity<List<Utilidad>> obtenerUtilidadesProductos() {
        List<Utilidad> utilidades = utilidadService.obtenerUtilidadesProductos();
        return ResponseEntity.ok(utilidades);
    }

    // Nuevo endpoint para obtener todas las utilidades de categorías
    @GetMapping("/categoria")
    public ResponseEntity<List<Utilidad>> obtenerUtilidadesCategorias() {
        List<Utilidad> utilidades = utilidadService.obtenerUtilidadesCategorias();
        return ResponseEntity.ok(utilidades);
    }
}