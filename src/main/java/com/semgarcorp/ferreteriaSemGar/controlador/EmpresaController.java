package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.EmpresaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Empresa;
import com.semgarcorp.ferreteriaSemGar.servicio.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // Obtener la lista de todas las empresas (como DTO)
    @GetMapping
    public List<EmpresaDTO> listar() {
        return empresaService.listar().stream().map(EmpresaDTO::new).collect(Collectors.toList());
    }

    // Obtener una empresa por su ID (como DTO)
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> obtenerPorId(@PathVariable Integer id) {
        Empresa empresa = empresaService.obtenerPorId(id);
        if (empresa != null) {
            return ResponseEntity.ok(new EmpresaDTO(empresa));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Crear una nueva empresa (recibe y devuelve DTO)
    @PostMapping
    public ResponseEntity<EmpresaDTO> guardar(@RequestBody EmpresaDTO empresaDTO) {
        Empresa empresa = empresaDTO.toEntity();
        Empresa nuevaEmpresa = empresaService.guardar(empresa);
        EmpresaDTO nuevaEmpresaDTO = new EmpresaDTO(nuevaEmpresa);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaEmpresa.getIdEmpresa()).toUri();
        return ResponseEntity.created(location).body(nuevaEmpresaDTO);
    }

    // Actualizar una empresa existente (PUT, recibe y devuelve DTO)
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> actualizar(@PathVariable Integer id, @RequestBody EmpresaDTO empresaDTO) {
        Empresa empresaExistente = empresaService.obtenerPorId(id);
        if (empresaExistente != null) {
            empresaDTO.setIdEmpresa(id);
            Empresa empresaActualizada = empresaService.actualizar(empresaDTO.toEntity());
            EmpresaDTO empresaActualizadaDTO = new EmpresaDTO(empresaActualizada);
            return ResponseEntity.ok(empresaActualizadaDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar una empresa por su ID (sin DTO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Empresa empresaExistente = empresaService.obtenerPorId(id);
        if (empresaExistente != null) {
            empresaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}