package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Empresa;
import com.semgarcorp.ferreteriaSemGar.servicio.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // Obtener la lista de todas las empresas
    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listar();
    }

    // Obtener una empresa por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerPorId(@PathVariable Integer id) {
        Empresa empresa = empresaService.obtenerPorId(id);
        if (empresa != null) {
            return ResponseEntity.ok(empresa); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva empresa
    @PostMapping
    public ResponseEntity<Empresa> guardar(@RequestBody Empresa empresa) {
        // Guardar la empresa usando el servicio
        Empresa nuevaEmpresa = empresaService.guardar(empresa);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaEmpresa.getIdEmpresa()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevaEmpresa);
    }

    // Actualizar una empresa existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Empresa> actualizar(@PathVariable Integer id, @RequestBody Empresa empresa) {
        // Obtener la empresa existente
        Empresa empresaExistente = empresaService.obtenerPorId(id);

        if (empresaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la empresa
            empresa.setIdEmpresa(id);

            // Aquí reemplazamos completamente la empresa con la información que viene en el cuerpo
            Empresa empresaActualizada = empresaService.actualizar(empresa);

            return ResponseEntity.ok(empresaActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una empresa por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Empresa empresaExistente = empresaService.obtenerPorId(id);
        if (empresaExistente != null) {
            empresaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}