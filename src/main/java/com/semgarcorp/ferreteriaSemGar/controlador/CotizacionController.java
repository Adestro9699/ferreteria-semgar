package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.CotizacionDTO;
import com.semgarcorp.ferreteriaSemGar.dto.CotizacionDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.CotizacionResumenDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Cotizacion;
import com.semgarcorp.ferreteriaSemGar.servicio.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;

    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    // Obtener la lista de todas las cotizaciones
    @GetMapping
    public List<Cotizacion> listar() {
        return cotizacionService.listar();
    }

    // Obtener una cotización por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cotizacion> obtenerPorId(@PathVariable Integer id) {
        Cotizacion cotizacion = cotizacionService.obtenerPorId(id);
        if (cotizacion != null) {
            return ResponseEntity.ok(cotizacion); // Uso de ResponseEntity.ok() para simplificar la respuesta con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Devuelve 404 Not Found si no se encuentra el recurso
    }

    // Crear una nueva cotización
    @PostMapping
    public ResponseEntity<Cotizacion> guardar(@RequestBody Cotizacion cotizacion) {
        // Guardar la cotización usando el servicio
        Cotizacion nuevaCotizacion = cotizacionService.guardar(cotizacion);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaCotizacion.getIdCotizacion()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location
        return ResponseEntity.created(location).body(nuevaCotizacion);
    }

    // Actualizar una cotización existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<CotizacionDTO> actualizar(
            @PathVariable Integer id,
            @RequestBody CotizacionDTO cotizacionDTO) {

        CotizacionDTO cotizacionActualizada = cotizacionService.actualizar(id, cotizacionDTO);
        return ResponseEntity.ok(cotizacionActualizada);
    }

    // Eliminar una cotización por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Cotizacion cotizacionExistente = cotizacionService.obtenerPorId(id);
        if (cotizacionExistente != null) {
            cotizacionService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    // Convertir una venta en cotización
    @PostMapping("/convertir-venta")
    public ResponseEntity<CotizacionDTO> convertirVentaACotizacion(@RequestBody VentaDTO ventaDTO) {
        CotizacionDTO cotizacionDTO = cotizacionService.convertirVentaACotizacion(ventaDTO);
        return ResponseEntity.ok(cotizacionDTO);
    }

    @GetMapping("/resumen")
    public List<CotizacionResumenDTO> obtenerTodasCotizacionesResumen() {
        return cotizacionService.obtenerTodasCotizacionesResumen();
    }

    @GetMapping("/{idCotizacion}/detalle")
    public ResponseEntity<CotizacionDetalleCompletoDTO> obtenerDetalleCotizacion(
            @PathVariable Integer idCotizacion) {
        return ResponseEntity.ok(cotizacionService.obtenerCotizacionDetalleCompleto(idCotizacion));
    }
}
