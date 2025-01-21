package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.servicio.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/productos")
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Obtener la lista de todos los productos
    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        // Guardar el producto usando el servicio
        Producto nuevoProducto = productoService.guardar(producto);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoProducto.getIdProducto()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoProducto);
    }

    // Actualizar un producto existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Integer id, @RequestBody Producto producto) {
        // Obtener el producto existente
        Producto productoExistente = productoService.obtenerPorId(id);

        if (productoExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el producto
            producto.setIdProducto(id);

            // Aquí reemplazas completamente el producto con la información que viene en el cuerpo
            Producto productoActualizado = productoService.actualizar(producto);

            return ResponseEntity.ok(productoActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un producto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente != null) {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    //endpoint para filtrar productos por nombre
    //productos/buscarPorNombre?nombre=nombreDelProducto
    //http://localhost:8080/productos/buscarPorNombre?nombre=Taladro
    @GetMapping("/buscarPorNombre")
    public List<Producto> buscarProductos(@RequestParam String nombre) {
        return productoService.buscarProductosPorNombre(nombre);
    }

    //endpoint para filtrar productos por marca
    //productos/buscarPorMarca?marca=nombreDeLaMarca
    //http://localhost:8080/productos/buscarPorMarca?marca=Truper
    @GetMapping("/buscarPorMarca")
    public List<Producto> buscarProductosPorMarca(@RequestParam String marca) {
        return productoService.buscarProductosPorMarca(marca);
    }

    //endpoint para filtrar productos por categoría
    //productos/buscarPorCategoria?categoria=nombreDeLaCategoria
    //http://localhost:8080/productos/buscarPorCategoria?categoria=Herramientas
    @GetMapping("/buscarPorCategoria")
    public List<Producto> buscarProductosPorCategoria(@RequestParam String categoria) {
        return productoService.buscarProductosPorCategoria(categoria);
    }
}
