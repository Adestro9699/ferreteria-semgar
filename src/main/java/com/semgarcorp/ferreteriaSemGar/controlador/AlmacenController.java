package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.dto.AlmacenDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.AlmacenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    /**
     * Obtener la lista de todos los almacenes como DTOs
     */
    @GetMapping
    public List<AlmacenDTO> listar() {
        List<Almacen> almacenes = almacenService.listar();
        return almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtener un almacén por su ID como DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerPorId(@PathVariable Integer id) {
        Almacen almacen = almacenService.obtenerPorId(id);
        if (almacen != null) {
            return ResponseEntity.ok(new AlmacenDTO(almacen));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crear un nuevo almacén
     */
    @PostMapping
    public ResponseEntity<AlmacenDTO> guardar(@RequestBody AlmacenDTO almacenDTO) {
        Almacen almacen = almacenDTO.toEntity();
        Almacen nuevoAlmacen = almacenService.guardar(almacen);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/almacenes/" + nuevoAlmacen.getIdAlmacen())
                .body(new AlmacenDTO(nuevoAlmacen));
    }

    /**
     * Actualizar un almacén existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDTO> actualizar(@PathVariable Integer id, @RequestBody AlmacenDTO almacenDTO) {
        Almacen almacenExistente = almacenService.obtenerPorId(id);

        if (almacenExistente != null) {
            almacenDTO.setIdAlmacen(id);
            almacenDTO.updateEntity(almacenExistente);
            Almacen almacenActualizado = almacenService.actualizar(almacenExistente);
            return ResponseEntity.ok(new AlmacenDTO(almacenActualizado));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Eliminar un almacén por su ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Almacen almacenExistente = almacenService.obtenerPorId(id);
        if (almacenExistente != null) {
            almacenService.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar almacenes por tipo (principal o de sucursal) como DTOs
     */
    @GetMapping("/tipo/{esPrincipal}")
    public ResponseEntity<List<AlmacenDTO>> buscarPorTipo(@PathVariable Boolean esPrincipal) {
        List<Almacen> almacenes = almacenService.buscarPorTipo(esPrincipal);
        List<AlmacenDTO> almacenesDTO = almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(almacenesDTO);
    }

    /**
     * Buscar almacenes por estado como DTOs
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AlmacenDTO>> buscarPorEstado(@PathVariable String estado) {
        try {
            Almacen.EstadoAlmacen estadoAlmacen = Almacen.EstadoAlmacen.valueOf(estado.toUpperCase());
            List<Almacen> almacenes = almacenService.buscarPorEstado(estadoAlmacen);
            List<AlmacenDTO> almacenesDTO = almacenes.stream()
                    .map(AlmacenDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(almacenesDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Buscar almacenes por sucursal como DTOs
     */
    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<AlmacenDTO>> buscarPorSucursal(@PathVariable Integer idSucursal) {
        List<Almacen> almacenes = almacenService.buscarPorSucursal(idSucursal);
        List<AlmacenDTO> almacenesDTO = almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(almacenesDTO);
    }

    /**
     * Buscar almacenes principales (esPrincipal = true) como DTOs
     */
    @GetMapping("/principales")
    public ResponseEntity<List<AlmacenDTO>> buscarAlmacenesPrincipales() {
        List<Almacen> almacenes = almacenService.buscarAlmacenesPrincipales();
        List<AlmacenDTO> almacenesDTO = almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(almacenesDTO);
    }

    /**
     * Buscar almacenes de sucursal (esPrincipal = false) como DTOs
     */
    @GetMapping("/sucursal")
    public ResponseEntity<List<AlmacenDTO>> buscarAlmacenesSucursal() {
        List<Almacen> almacenes = almacenService.buscarAlmacenesSucursal();
        List<AlmacenDTO> almacenesDTO = almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(almacenesDTO);
    }

    /**
     * Buscar almacenes por nombre como DTOs
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<AlmacenDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<Almacen> almacenes = almacenService.buscarPorNombre(nombre);
        List<AlmacenDTO> almacenesDTO = almacenes.stream()
                .map(AlmacenDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(almacenesDTO);
    }

    /**
     * Obtener el almacén principal de una sucursal específica como DTO
     */
    @GetMapping("/sucursal/{idSucursal}/principal")
    public ResponseEntity<AlmacenDTO> obtenerAlmacenPrincipalDeSucursal(@PathVariable Integer idSucursal) {
        Almacen almacenPrincipal = almacenService.obtenerAlmacenPrincipalDeSucursal(idSucursal);
        if (almacenPrincipal != null) {
            return ResponseEntity.ok(new AlmacenDTO(almacenPrincipal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Verificar si una sucursal tiene almacén principal
     */
    @GetMapping("/sucursal/{idSucursal}/tiene-principal")
    public ResponseEntity<Boolean> tieneAlmacenPrincipal(@PathVariable Integer idSucursal) {
        boolean tienePrincipal = almacenService.tieneAlmacenPrincipal(idSucursal);
        return ResponseEntity.ok(tienePrincipal);
    }
} 