package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import com.semgarcorp.ferreteriaSemGar.dto.TransferenciaDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.TransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transferencias")
public class TransferenciaController {
    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    // Obtener la lista de todas las transferencias
    @GetMapping
    public List<TransferenciaDTO> listar() {
        List<Transferencia> transferencias = transferenciaService.listar();
        return transferencias.stream()
                .map(TransferenciaDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener una transferencia por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> obtenerPorId(@PathVariable Integer id) {
        Transferencia transferencia = transferenciaService.obtenerPorId(id);
        if (transferencia != null) {
            return ResponseEntity.ok(new TransferenciaDTO(transferencia));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Crear una nueva transferencia
    @PostMapping
    public ResponseEntity<TransferenciaDTO> guardar(@RequestBody TransferenciaDTO transferenciaDTO) {
        Transferencia transferencia = transferenciaDTO.toEntity();
        Transferencia nuevaTransferencia = transferenciaService.guardar(transferencia);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaTransferencia.getIdTransferencia()).toUri();

        return ResponseEntity.created(location).body(new TransferenciaDTO(nuevaTransferencia));
    }

    /**
     * Crear una nueva transferencia con almacén origen automático del usuario autenticado
     * El sistema automáticamente establece el almacén origen y trabajador origen
     */
    @PostMapping("/crear-con-origen-automatico")
    public ResponseEntity<TransferenciaDTO> crearConOrigenAutomatico(@RequestBody TransferenciaDTO transferenciaDTO) {
        try {
            Transferencia transferencia = transferenciaDTO.toEntity();
            Transferencia nuevaTransferencia = transferenciaService.crearTransferenciaConOrigenAutomatico(transferencia);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevaTransferencia.getIdTransferencia()).toUri();

            return ResponseEntity.created(location).body(new TransferenciaDTO(nuevaTransferencia));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Obtener transferencias emitidas por el usuario autenticado
     * Estas son las transferencias que el usuario ha enviado a otros almacenes
     */
    @GetMapping("/emitidas")
    public ResponseEntity<List<TransferenciaDTO>> obtenerTransferenciasEmitidas() {
        try {
            List<Transferencia> transferencias = transferenciaService.obtenerTransferenciasEmitidasPorUsuario();
            List<TransferenciaDTO> transferenciasDTO = transferencias.stream()
                    .map(TransferenciaDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(transferenciasDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener transferencias recibidas por el usuario autenticado
     * Estas son las transferencias que el usuario ha recibido de otros almacenes
     */
    @GetMapping("/recibidas")
    public ResponseEntity<List<TransferenciaDTO>> obtenerTransferenciasRecibidas() {
        try {
            List<Transferencia> transferencias = transferenciaService.obtenerTransferenciasRecibidasPorUsuario();
            List<TransferenciaDTO> transferenciasDTO = transferencias.stream()
                    .map(TransferenciaDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(transferenciasDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Aceptar una transferencia (establecer trabajador destino y cambiar estado)
     * Solo el almacén destino puede aceptar la transferencia
     */
    @PostMapping("/{id}/aceptar")
    public ResponseEntity<TransferenciaDTO> aceptarTransferencia(@PathVariable Integer id) {
        try {
            Transferencia transferenciaAceptada = transferenciaService.aceptarTransferencia(id);
            return ResponseEntity.ok(new TransferenciaDTO(transferenciaAceptada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Actualizar una transferencia existente
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> actualizar(@PathVariable Integer id, @RequestBody TransferenciaDTO transferenciaDTO) {
        Transferencia transferenciaExistente = transferenciaService.obtenerPorId(id);

        if (transferenciaExistente != null) {
            transferenciaDTO.setIdTransferencia(id);
            Transferencia transferencia = transferenciaDTO.toEntity();
            Transferencia transferenciaActualizado = transferenciaService.actualizar(transferencia);
            return ResponseEntity.ok(new TransferenciaDTO(transferenciaActualizado));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar una transferencia por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Transferencia transferenciaExistente = transferenciaService.obtenerPorId(id);
        if (transferenciaExistente != null) {
            transferenciaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    // Ejecutar una transferencia (mover stock)
    @PostMapping("/{id}/ejecutar")
    public ResponseEntity<?> ejecutarTransferencia(@PathVariable Integer id) {
        try {
            Transferencia transferenciaEjecutada = transferenciaService.ejecutarTransferencia(id);
            return ResponseEntity.ok(new TransferenciaDTO(transferenciaEjecutada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Transferencia no encontrada", "mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Operación no permitida", "mensaje", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error procesando transferencia", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }
    
    // Cancelar una transferencia
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarTransferencia(@PathVariable Integer id) {
        try {
            Transferencia transferenciaCancelada = transferenciaService.cancelarTransferencia(id);
            return ResponseEntity.ok(new TransferenciaDTO(transferenciaCancelada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Transferencia no encontrada", "mensaje", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Operación no permitida", "mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor", "mensaje", e.getMessage()));
        }
    }
    
    // Buscar transferencias por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<TransferenciaDTO>> buscarPorEstado(@PathVariable String estado) {
        try {
            Transferencia.EstadoTransferencia estadoEnum = Transferencia.EstadoTransferencia.valueOf(estado.toUpperCase());
            List<Transferencia> transferencias = transferenciaService.buscarPorEstado(estadoEnum);
            List<TransferenciaDTO> transferenciasDTO = transferencias.stream()
                    .map(TransferenciaDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(transferenciasDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Buscar transferencias por almacén origen
    @GetMapping("/almacen-origen/{idAlmacen}")
    public ResponseEntity<List<TransferenciaDTO>> buscarPorAlmacenOrigen(@PathVariable Integer idAlmacen) {
        List<Transferencia> transferencias = transferenciaService.buscarPorAlmacenOrigen(idAlmacen);
        List<TransferenciaDTO> transferenciasDTO = transferencias.stream()
                .map(TransferenciaDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transferenciasDTO);
    }
    
    // Buscar transferencias por almacén destino
    @GetMapping("/almacen-destino/{idAlmacen}")
    public ResponseEntity<List<TransferenciaDTO>> buscarPorAlmacenDestino(@PathVariable Integer idAlmacen) {
        List<Transferencia> transferencias = transferenciaService.buscarPorAlmacenDestino(idAlmacen);
        List<TransferenciaDTO> transferenciasDTO = transferencias.stream()
                .map(TransferenciaDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transferenciasDTO);
    }
}
