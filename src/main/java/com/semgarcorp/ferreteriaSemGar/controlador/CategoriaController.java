package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.servicio.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Obtener la lista de todas las categorías
    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    // Obtener una categoría por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.obtenerPorId(id);
        if (categoria != null) {
            return ResponseEntity.ok(categoria); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear una nueva categoría
    @PostMapping
    public ResponseEntity<Categoria> guardar(@RequestBody Categoria categoria) {
        Categoria nuevaCategoria = categoriaService.guardar(categoria);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaCategoria.getIdCategoria()).toUri();

        return ResponseEntity.created(location).body(nuevaCategoria);
    }

    // Actualizar una categoría existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Integer id, @RequestBody Categoria categoria) {
        // Obtener la categoría existente
        Categoria categoriaExistente = categoriaService.obtenerPorId(id);

        if (categoriaExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar la categoría
            categoria.setIdCategoria(id);

            // Aquí reemplazas completamente la categoría con la información que viene en el cuerpo
            Categoria categoriaActualizada = categoriaService.actualizar(categoria);

            return ResponseEntity.ok(categoriaActualizada); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar una categoría por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Categoria categoriaExistente = categoriaService.obtenerPorId(id);
        if (categoriaExistente != null) {
            categoriaService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }
}
