package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;
import com.semgarcorp.ferreteriaSemGar.dto.SucursalDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.SucursalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import com.semgarcorp.ferreteriaSemGar.servicio.UsuarioService;

@RestController
@RequestMapping("/sucursales")
@CrossOrigin(origins = "*")
public class SucursalController {

    private final SucursalService sucursalService;
    private final UsuarioService usuarioService;

    public SucursalController(SucursalService sucursalService, UsuarioService usuarioService) {
        this.sucursalService = sucursalService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<SucursalDTO> listar() {
        List<Sucursal> sucursales = sucursalService.listar();
        return sucursales.stream()
                .map(sucursal -> new SucursalDTO(sucursal, true)) // Con almacenes para transferencias
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> obtenerPorId(@PathVariable Integer id) {
        Sucursal sucursal = sucursalService.obtenerPorId(id);
        if (sucursal != null) {
            return ResponseEntity.ok(new SucursalDTO(sucursal, false)); // Sin almacenes
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/completa")
    public ResponseEntity<SucursalDTO> obtenerPorIdConAlmacenes(@PathVariable Integer id) {
        Sucursal sucursal = sucursalService.obtenerPorId(id);
        if (sucursal != null) {
            return ResponseEntity.ok(new SucursalDTO(sucursal, true)); // Con almacenes
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> guardar(@RequestBody SucursalDTO sucursalDTO) {
        Sucursal sucursal = sucursalDTO.toEntity();
        Sucursal nuevaSucursal = sucursalService.guardar(sucursal);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaSucursal.getIdSucursal()).toUri();

        return ResponseEntity.created(location).body(new SucursalDTO(nuevaSucursal, false));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> actualizar(@PathVariable Integer id, @RequestBody SucursalDTO sucursalDTO) {
        Sucursal sucursalExistente = sucursalService.obtenerPorId(id);

        if (sucursalExistente != null) {
            sucursalDTO.setIdSucursal(id);
            sucursalDTO.updateEntity(sucursalExistente);

            Sucursal sucursalActualizado = sucursalService.actualizar(sucursalExistente);

            return ResponseEntity.ok(new SucursalDTO(sucursalActualizado, false));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Sucursal sucursalExistente = sucursalService.obtenerPorId(id);
        if (sucursalExistente != null) {
            sucursalService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Obtiene las sucursales disponibles para transferencias.
     * Excluye la sucursal del usuario autenticado.
     */
    @GetMapping("/disponibles-transferencia")
    public ResponseEntity<List<SucursalDTO>> obtenerSucursalesDisponiblesParaTransferencia() {
        try {
            // Obtener la sucursal del usuario autenticado
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            var usuarioOpt = usuarioService.obtenerPorNombreUsuario(username);
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            var usuario = usuarioOpt.get();
            var trabajador = usuario.getTrabajador();
            
            if (trabajador == null || trabajador.getSucursal() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            Integer idSucursalUsuario = trabajador.getSucursal().getIdSucursal();
            
            // Obtener todas las sucursales excepto la del usuario
            List<Sucursal> todasLasSucursales = sucursalService.listar();
            List<Sucursal> sucursalesDisponibles = todasLasSucursales.stream()
                    .filter(sucursal -> !sucursal.getIdSucursal().equals(idSucursalUsuario))
                    .collect(Collectors.toList());
            
            List<SucursalDTO> sucursalesDTO = sucursalesDisponibles.stream()
                    .map(sucursal -> new SucursalDTO(sucursal, false))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(sucursalesDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
