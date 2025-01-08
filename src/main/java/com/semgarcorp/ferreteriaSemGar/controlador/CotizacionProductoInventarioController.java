package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.CotizacionProductoInventario;
import com.semgarcorp.ferreteriaSemGar.servicio.CotizacionProductoInventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cotizaciones-producto-inventario")
public class CotizacionProductoInventarioController {

    private final CotizacionProductoInventarioService cotizacionProductoInventarioService;

    public CotizacionProductoInventarioController(CotizacionProductoInventarioService cotizacionProductoInventarioService) {
        this.cotizacionProductoInventarioService = cotizacionProductoInventarioService;
    }

    // Obtener la lista de todos los registros de CotizacionProductoInventario
    @GetMapping
    public List<CotizacionProductoInventario> listar() {
        return cotizacionProductoInventarioService.listar();
    }

    // Obtener un registro de CotizacionProductoInventario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<CotizacionProductoInventario> obtenerPorId(@PathVariable Integer id) {
        CotizacionProductoInventario cotizacionProductoInventario = cotizacionProductoInventarioService.obtenerPorId(id);
        if (cotizacionProductoInventario != null) {
            return ResponseEntity.ok(cotizacionProductoInventario); // Respuesta con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo registro de CotizacionProductoInventario
    @PostMapping
    public ResponseEntity<List<CotizacionProductoInventario>> guardar(@RequestBody List<CotizacionProductoInventario> productos) {
        // Guardar el registro usando el servicio
        List<CotizacionProductoInventario> nuevosProductos = cotizacionProductoInventarioService.guardar(productos);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevosProductos.get(0).getIdCotizacionProductoInventario()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevosProductos);
    }

    // Actualizar un registro de CotizacionProductoInventario existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<List<CotizacionProductoInventario>> actualizar(@PathVariable Integer id, @RequestBody List<CotizacionProductoInventario> productos) {
        // Verificamos si al menos uno de los productos en la lista tiene el ID correspondiente
        CotizacionProductoInventario cotizacionProductoInventarioExistente = cotizacionProductoInventarioService.obtenerPorId(id);

        if (cotizacionProductoInventarioExistente != null) {
            // Aseguramos que los productos a actualizar tengan el mismo ID (si es necesario)
            for (CotizacionProductoInventario producto : productos) {
                producto.setIdCotizacionProductoInventario(id); // Reemplazar el ID para la actualización
            }

            // Actualizar los productos con el servicio
            List<CotizacionProductoInventario> productosActualizados = cotizacionProductoInventarioService.actualizar(productos);

            return ResponseEntity.ok(productosActualizados); // Respuesta exitosa con los productos actualizados
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra el registro
        }
    }

    // Eliminar un registro de CotizacionProductoInventario por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        CotizacionProductoInventario cotizacionProductoInventarioExistente = cotizacionProductoInventarioService.obtenerPorId(id);
        if (cotizacionProductoInventarioExistente != null) {
            cotizacionProductoInventarioService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta 404 si no se encuentra el registro
    }
}
