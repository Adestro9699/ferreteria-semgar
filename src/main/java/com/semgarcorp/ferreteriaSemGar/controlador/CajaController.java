package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.CajaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.MovimientoCajaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.servicio.CajaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.Map; // Para crear mapas (usado en las respuestas de error)

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<CajaDTO>> listar() {
        List<Caja> cajas = cajaService.listar();
        List<CajaDTO> cajasDTO = cajas.stream()
                .map(cajaService::convertirACajaDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cajasDTO);
    }

    // Endpoint GET por ID para obtener un registro de caja específico
    @GetMapping("/{id}")
    public ResponseEntity<CajaDTO> obtenerPorId(@PathVariable Integer id) {
        Caja caja = cajaService.obtenerPorId(id);
        if (caja != null) {
            CajaDTO cajaDTO = cajaService.convertirACajaDTO(caja);
            return ResponseEntity.ok(cajaDTO);
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

    @PutMapping("/{id}")
    public ResponseEntity<CajaDTO> actualizar(@PathVariable Integer id, @RequestBody CajaDTO cajaDTO) {
        try {
            // Verificar si la caja existe
            Caja cajaExistente = cajaService.obtenerPorId(id);
            if (cajaExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Validar que el DTO no sea nulo y que el nombreCaja no esté vacío
            if (cajaDTO == null || cajaDTO.getNombreCaja() == null || cajaDTO.getNombreCaja().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // Actualizar solo el campo nombreCaja
            cajaExistente.setNombreCaja(cajaDTO.getNombreCaja());

            // Guardar los cambios en la base de datos
            Caja cajaActualizada = cajaService.actualizar(cajaExistente);

            // Convertir la entidad actualizada a DTO
            CajaDTO cajaActualizadaDTO = cajaService.convertirACajaDTO(cajaActualizada);

            return ResponseEntity.ok(cajaActualizadaDTO);
        } catch (Exception e) {
            // Manejar excepciones y devolver un error 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint DELETE para eliminar un registro de caja por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCaja(@PathVariable Integer id) {
        try {
            cajaService.eliminar(id);
            return ResponseEntity.ok("Caja eliminada exitosamente.");
        } catch (RuntimeException e) {
            // Captura la excepción lanzada por el servicio
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la caja.");
        }
    }

    @PostMapping("/abrir")
    public ResponseEntity<?> abrirCaja(@RequestBody CajaDTO cajaDTO) {
        try {
            // Llamar al servicio para abrir la caja
            Caja cajaAbierta = cajaService.abrirCaja(cajaDTO);

            // Convertir la entidad actualizada a DTO
            CajaDTO cajaAbiertaDTO = cajaService.convertirACajaDTO(cajaAbierta);

            // Retornar la respuesta con la caja actualizada
            return ResponseEntity.ok(cajaAbiertaDTO);
        } catch (IllegalArgumentException ex) {
            // Capturar excepciones de validación (por ejemplo, datos inválidos o faltantes)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", "Bad Request",
                    "message", ex.getMessage()
            ));
        } catch (IllegalStateException ex) {
            // Capturar excepciones de estado (por ejemplo, el usuario ya tiene una caja abierta o la caja no está cerrada)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", "Conflict",
                    "message", ex.getMessage()
            ));
        } catch (EntityNotFoundException ex) {
            // Capturar excepciones de recurso no encontrado (por ejemplo, caja o usuario no encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "Not Found",
                    "message", ex.getMessage()
            ));
        } catch (Exception ex) {
            // Capturar cualquier otra excepción no esperada
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Internal Server Error",
                    "message", "Ocurrió un error inesperado al abrir la caja."
            ));
        }
    }

    @PostMapping("/cerrar")
    public ResponseEntity<CajaDTO> cerrarCaja(@RequestBody CajaDTO cajaDTO) {
        try {
            // Llamar al servicio para cerrar la caja
            Caja cajaCerrada = cajaService.cerrarCaja(cajaDTO);

            // Convertir la entidad actualizada a DTO
            CajaDTO cajaCerradaDTO = cajaService.convertirACajaDTO(cajaCerrada);

            // Retornar la respuesta con la caja actualizada
            return ResponseEntity.ok(cajaCerradaDTO);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Maneja tanto IllegalArgumentException como IllegalStateException
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idCaja}/entrada-manual")
    public ResponseEntity<CajaDTO> registrarEntradaManual(
            @PathVariable Integer idCaja,
            @RequestBody MovimientoCajaDTO movimientoDTO) {
        try {
            // Llamar al servicio para registrar la entrada manual
            Caja caja = cajaService.registrarEntradaManual(idCaja, movimientoDTO);

            // Convertir la entidad actualizada a DTO
            CajaDTO cajaDTO = cajaService.convertirACajaDTO(caja);

            // Retornar la respuesta con la caja actualizada
            return ResponseEntity.ok(cajaDTO);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Maneja tanto IllegalArgumentException como IllegalStateException
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{idCaja}/salida-manual")
    public ResponseEntity<CajaDTO> registrarSalidaManual(
            @PathVariable Integer idCaja,
            @RequestBody MovimientoCajaDTO movimientoDTO) {
        try {
            // Llamar al servicio para registrar la salida manual
            Caja caja = cajaService.registrarSalidaManual(idCaja, movimientoDTO);

            // Convertir la entidad actualizada a DTO
            CajaDTO cajaDTO = cajaService.convertirACajaDTO(caja);

            // Retornar la respuesta con la caja actualizada
            return ResponseEntity.ok(cajaDTO);
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Maneja tanto IllegalArgumentException como IllegalStateException
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
