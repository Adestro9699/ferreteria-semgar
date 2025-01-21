package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Subcategoria;
import com.semgarcorp.ferreteriaSemGar.servicio.SubcategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/subcategorias")
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    public SubcategoriaController(SubcategoriaService subcategoriaService) {
        this.subcategoriaService = subcategoriaService;
    }

    // Obtener la lista de todas las subcategorías
    @GetMapping
    public List<Subcategoria> listar() {
        return subcategoriaService.listar();
    }

    // Obtener una subcategoría por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Subcategoria> obtenerPorId(@PathVariable Integer id) {
        Subcategoria subcategoria = subcategoriaService.obtenerPorId(id);
        if (subcategoria != null) {
            return ResponseEntity.ok(subcategoria); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva subcategoría
    @PostMapping
    public ResponseEntity<Subcategoria> guardar(@RequestBody Subcategoria subcategoria) {
        // Guardar la subcategoría usando el servicio
        Subcategoria nuevaSubcategoria = subcategoriaService.guardar(subcategoria);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaSubcategoria.getIdSubcategoria()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevaSubcategoria);
    }

    // Actualizar una subcategoría existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Subcategoria> actualizar(@PathVariable Integer id, @RequestBody Subcategoria subcategoria) {
        // Obtener la subcategoría existente
        Subcategoria subcategoriaExistente = subcategoriaService.obtenerPorId(id);

        if (subcategoriaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la subcategoría
            subcategoria.setIdSubcategoria(id);

            // Aquí reemplazas completamente la subcategoría con la información que viene en el cuerpo
            Subcategoria subcategoriaActualizada = subcategoriaService.actualizar(subcategoria);

            return ResponseEntity.ok(subcategoriaActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una subcategoría por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Subcategoria subcategoriaExistente = subcategoriaService.obtenerPorId(id);
        if (subcategoriaExistente != null) {
            subcategoriaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
