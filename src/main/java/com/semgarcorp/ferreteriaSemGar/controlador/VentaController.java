package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.VentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.EstadoVenta;
import com.semgarcorp.ferreteriaSemGar.servicio.VentaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // Obtener la lista de todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listar() {
        List<VentaDTO> ventasDTO = ventaService.listar(); // Obtener la lista de DTOs
        return ResponseEntity.ok(ventasDTO); // Devolver la lista en la respuesta
    }

    // Obtener una venta por su ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerPorId(@PathVariable Integer id) {
        return ventaService.obtenerPorId(id)
                .map(ResponseEntity::ok) // Si está presente, devuelve 200 OK con el DTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no está, devuelve 404 Not Found
    }

    // Crear una nueva venta
    @PostMapping
    public ResponseEntity<VentaDTO> guardarVenta(@RequestBody VentaDTO ventaDTO) {
        // 1. Guardar la venta usando el servicio
        VentaDTO nuevaVentaDTO = ventaService.guardarVenta(ventaDTO);

        // 2. Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaVentaDTO.getIdVenta()).toUri();

        // 3. Devolver la respuesta con la URI en la cabecera Location y el DTO en el
        // cuerpo
        return ResponseEntity.created(location).body(nuevaVentaDTO);
    }

    @PostMapping("/{idVenta}/completar")
    public ResponseEntity<?> completarVenta(
            @PathVariable Integer idVenta,
            @RequestBody Map<String, Integer> requestBody
    ) {
        System.out.println("==== LLEGÓ AL CONTROLLER ====");

        Integer idCaja = requestBody.get("idCaja");
        if (idCaja == null) {
            return ResponseEntity.badRequest().body("El idCaja es requerido para completar la venta.");
        }

        try {
            VentaDTO ventaCompletada = ventaService.completarVenta(idVenta, idCaja);
            return ResponseEntity.ok(ventaCompletada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Manejo específico para errores de stock y conexión
            if (e.getMessage().contains("Stock insuficiente")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // HTTP 409
            } else if (e.getMessage().contains("No hay conexión a Internet")) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage()); // HTTP 503
            }
            return ResponseEntity.badRequest().body(e.getMessage()); // Otros IllegalStateException
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al procesar la venta: " + e.getMessage());
        }
    }

    // Actualizar una venta existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Integer id, @RequestBody VentaDTO ventaDTO) {
        return ventaService.obtenerPorId(id)
                .map(ventaExistenteDTO -> {
                    VentaDTO ventaActualizadaDTO = ventaService.actualizar(id, ventaDTO);
                    return ResponseEntity.ok(ventaActualizadaDTO);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return ventaService.obtenerPorId(id)
                .map(venta -> {
                    ventaService.eliminar(id);
                    return ResponseEntity.noContent().<Void>build(); // Forzamos el tipo a Void
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //para traer el resumen básico de la venta en estado PENDIENTE
    @GetMapping("/pendientes")
    public ResponseEntity<Page<VentaResumenDTO>> listarPendientes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                ventaService.listarVentasPendientes(pagina, size));
    }

    //para traer el resumen básico de la venta en estado COMPLETADA + ANULADA
    @GetMapping("/completadas-anuladas")
    public ResponseEntity<Page<VentaResumenDTO>> listarCompletadasYAnuladas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                ventaService.listarVentasCompletadasYAnuladas(pagina, size));
    }

    // detalle completo de la venta
    @GetMapping("/{idVenta}/detalle")
    public ResponseEntity<VentaDetalleCompletoDTO> obtenerDetalleVenta(
            @PathVariable Integer idVenta) {
        return ResponseEntity.ok(ventaService.obtenerVentaDetalleCompleto(idVenta));
    }

    // pre cargar una venta desde una cotización
    @GetMapping("/precargar-venta/{codigoCotizacion}") // Cambiado a GET (es una consulta)
    public ResponseEntity<VentaDTO> precargarVentaDesdeCotizacion(
            @PathVariable String codigoCotizacion) {
        VentaDTO ventaDTO = ventaService.convertirCotizacionAVenta(codigoCotizacion);
        return ResponseEntity.ok(ventaDTO);
    }

    @GetMapping("/precargar-venta/por-id/{idCotizacion}")
    public ResponseEntity<VentaDTO> precargarVentaPorId(
            @PathVariable Integer idCotizacion) {
        VentaDTO ventaDTO = ventaService.convertirCotizacionAVentaPorId(idCotizacion);
        return ResponseEntity.ok(ventaDTO);
    }

    @PostMapping("/{idVenta}/anular")
    public ResponseEntity<?> anularVenta(@PathVariable Integer idVenta) {
        try {
            VentaDTO ventaAnulada = ventaService.anularVenta(idVenta);
            return ResponseEntity.ok(ventaAnulada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "error", "Venta no encontrada",
                    "mensaje", e.getMessage()
                ));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Operación no permitida",
                    "mensaje", e.getMessage()
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Error interno del servidor",
                    "mensaje", e.getMessage()
                ));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarVentas(
            @RequestParam String criterio,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<VentaResumenDTO> ventas = ventaService.buscarVentasCompletadasYAnuladas(criterio, pagina, size);
            
            return ResponseEntity.ok(Map.of(
                "mensaje", ventas.isEmpty() ? 
                    "No se encontraron ventas completadas o anuladas para el criterio: " + criterio : 
                    "Búsqueda exitosa",
                "ventas", ventas.getContent(),
                "totalElementos", ventas.getTotalElements(),
                "totalPaginas", ventas.getTotalPages(),
                "paginaActual", ventas.getNumber(),
                "tamanoPagina", ventas.getSize()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "error", "Criterio de búsqueda inválido",
                    "mensaje", e.getMessage()
                ));
        }
    }
}
