package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.dto.TrabajadorDTO;
import com.semgarcorp.ferreteriaSemGar.servicio.TrabajadorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trabajadores")
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    // Obtener la lista de todos los trabajadores
    @GetMapping
    public List<TrabajadorDTO> listar() {
        List<Trabajador> trabajadores = trabajadorService.listar();
        return trabajadores.stream()
                .map(TrabajadorDTO::new)
                .collect(Collectors.toList());
    }

    // Obtener un trabajador por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorDTO> obtenerPorId(@PathVariable Integer id) {
        Trabajador trabajador = trabajadorService.obtenerPorId(id);
        if (trabajador != null) {
            return ResponseEntity.ok(new TrabajadorDTO(trabajador));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Crear un nuevo trabajador
    @PostMapping
    public ResponseEntity<TrabajadorDTO> guardar(@RequestBody TrabajadorDTO trabajadorDTO) {
        try {
            Trabajador trabajador = trabajadorDTO.toEntity();
            Trabajador nuevoTrabajador = trabajadorService.guardar(trabajador);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevoTrabajador.getIdTrabajador()).toUri();

            return ResponseEntity.created(location).body(new TrabajadorDTO(nuevoTrabajador));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Actualizar un trabajador existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TrabajadorDTO> actualizar(@PathVariable Integer id, @RequestBody TrabajadorDTO trabajadorDTO) {
        Trabajador trabajadorExistente = trabajadorService.obtenerPorId(id);
        if (trabajadorExistente != null) {
            trabajadorDTO.setIdTrabajador(id);
            trabajadorDTO.updateEntity(trabajadorExistente);
            Trabajador trabajadorActualizado = trabajadorService.actualizar(trabajadorExistente);
            return ResponseEntity.ok(new TrabajadorDTO(trabajadorActualizado));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar un trabajador por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Trabajador trabajadorExistente = trabajadorService.obtenerPorId(id);
        if (trabajadorExistente != null) {
            trabajadorService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
