package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.servicio.ProductoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/productos")
public class ProductoController {
    private final ProductoService productoService;
    private static final String UPLOAD_DIR = "C:/uploads/";

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

    // Eliminar múltiples productos por su ID
    @DeleteMapping("/eliminar-multiples")
    public ResponseEntity<Void> eliminarProductos(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.badRequest().build(); // Respuesta 400 Bad Request si la lista está vacía
            }
            System.out.println("IDs recibidos: " + ids); // Log para depuración
            productoService.eliminarProductosPorIds(ids);

            return ResponseEntity.noContent().build(); // Respuesta 204 No Content si la operación fue exitosa
        } catch (IllegalArgumentException e) {
            System.err.println("Error en la solicitud: " + e.getMessage());
            return ResponseEntity.badRequest().build(); // Respuesta 400 Bad Request si hay argumentos inválidos
        } catch (Exception e) {
            System.err.println("Error al eliminar productos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Respuesta 500 si ocurre un error interno
        }
    }

    //endpoint para buscar productos por nombre
    //productos/buscarPorNombre?nombre=nombreDelProducto
    //http://localhost:8080/fs/productos/buscarPorNombre?nombre=Taladro
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String nombre) {
        // Verificar si el nombre es nulo o está vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            // Devolver una respuesta 400 Bad Request sin body
            return ResponseEntity.badRequest().build();
        }

        try {
            // Buscar productos por nombre
            List<Producto> productos = productoService.buscarProductosPorNombre(nombre);

            // Devolver la lista de productos con un código 200 OK, incluso si está vacía
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            // Manejar otros errores y devolver una respuesta 500 Internal Server Error sin body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por marca
    //productos/buscarPorMarca?marca=nombreDeLaMarca
    //http://localhost:8080/fs/productos/buscarPorMarca?marca=Truper
    @GetMapping("/buscarPorMarca")
    public ResponseEntity<?> buscarProductosPorMarca(@RequestParam String marca) {
        // Verificar si la marca es nula o está vacía
        if (marca == null || marca.trim().isEmpty()) {
            // Devolver una respuesta 400 Bad Request sin body
            return ResponseEntity.badRequest().build();
        }

        try {
            // Buscar productos por marca
            List<Producto> productos = productoService.buscarProductosPorMarca(marca);

            // Devolver 200 OK sin body si no hay productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            // Devolver la lista de productos con un código 200 OK
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            // Manejar otros errores y devolver una respuesta 500 Internal Server Error sin body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // endpoint para buscar productos por nombre de categoría
    // productos/buscarPorCategoria?categoria=nombreDeLaCategoria
    // http://localhost:8080/fs/productos/buscarPorCategoria?categoria=Herramientas
    @GetMapping("/buscarPorCategoria")
    public ResponseEntity<?> buscarProductosPorCategoria(@RequestParam String categoria) {
        // Verificar si la categoría es nula o está vacía
        if (categoria == null || categoria.trim().isEmpty()) {
            // Devolver una respuesta 400 Bad Request sin body
            return ResponseEntity.badRequest().build();
        }

        try {
            // Buscar productos por categoría
            List<Producto> productos = productoService.buscarProductosPorCategoria(categoria);

            // Devolver 200 OK sin body si no hay productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            // Devolver la lista de productos con un código 200 OK
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            // Manejar otros errores y devolver una respuesta 500 Internal Server Error sin body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // endpoint para buscar productos por estado
    // productos/buscarPorEstado?estado=ACTIVO
    // http://localhost:8080/fs/productos/buscarPorEstado?estado=ACTIVO
    @GetMapping("/buscarPorEstado")
    public ResponseEntity<List<Producto>> buscarProductosPorEstado(@RequestParam String estado) {
        // Verificar si el estado es nulo o está vacío
        if (estado == null || estado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request si el parámetro es inválido
        }

        try {
            List<Producto> productos = productoService.buscarProductosPorEstado(estado);

            // Devolver 200 OK sin body si no se encuentran productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // endpoint para buscar productos por material
    // productos/buscarPorMaterial?material=Acero
    // http://localhost:8080/fs/productos/buscarPorMaterial?material=Acero
    @GetMapping("/buscarPorMaterial")
    public ResponseEntity<List<Producto>> buscarProductosPorMaterial(@RequestParam String material) {
        // Verificar si el material es nulo o está vacío
        if (material == null || material.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request si el parámetro es inválido
        }

        try {
            List<Producto> productos = productoService.buscarProductosPorMaterial(material);

            // Devolver 200 OK sin body si no se encuentran productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // endpoint para buscar por codigo de barra
    // productos/buscarPorCodigoBarra?codigoBarra=VALOR_CODIGO_BARRA
    // http://localhost:8080/fs/productos/buscarPorCodigoBarra?codigoBarra=VALOR_CODIGO_BARRA
    @GetMapping("/buscarPorCodigoBarra")
    public ResponseEntity<List<Producto>> buscarProductosPorCodigoBarra(@RequestParam String codigoBarra) {
        // Verificar si el código de barra es nulo o está vacío
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request si el parámetro es inválido
        }

        try {
            List<Producto> productos = productoService.buscarProductosPorCodigoBarra(codigoBarra);

            // Devolver 200 OK sin body si no se encuentran productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // endpoint para buscar por nombre de proveedor
    // productos/buscarPorProveedor?idProveedor=VALOR_ID_PROVEEDOR
    // http://localhost:8080/fs/productos/buscarPorNombreProveedor?nombreProveedor=VALOR_NOMBRE_PROVEEDOR
    @GetMapping("/buscarPorNombreProveedor")
    public ResponseEntity<List<Producto>> buscarProductosPorNombreProveedor(@RequestParam String nombreProveedor) {
        // Verificar si el nombre del proveedor es nulo o está vacío
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar productos por nombre del proveedor
            List<Producto> productos = productoService.buscarProductosPorNombreProveedor(nombreProveedor);

            // Devolver 200 OK sin body si no se encuentran productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // endpoint para buscar por nombre de subcategoria
    // productos/buscarPorNombreSubcategoria?nombreSubcategoria=VALOR_NOMBRE_SUBCATEGORIA
    // http://localhost:8080/fs/productos/buscarPorNombreSubcategoria?nombreSubcategoria=VALOR_NOMBRE_SUBCATEGORIA
    @GetMapping("/buscarPorNombreSubcategoria")
    public ResponseEntity<List<Producto>> buscarProductosPorNombreSubcategoria(@RequestParam String nombreSubcategoria) {
        // Verificar si el nombre de la subcategoría es nulo o está vacío
        if (nombreSubcategoria == null || nombreSubcategoria.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar productos por nombre de la subcategoría
            List<Producto> productos = productoService.buscarProductosPorNombreSubcategoria(nombreSubcategoria);

            // Verificar si se encontraron productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build(); // 200 OK sin cuerpo
            }

            // Devolver la lista de productos con un código 200 OK
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // http://localhost:8080/fs/productos/buscarPorCodigoSKU?codigoSKU=VALOR_CODIGOSKU
    @GetMapping("/buscarPorCodigoSKU")
    public ResponseEntity<List<Producto>> buscarProductosPorCodigoSKU(@RequestParam String codigoSKU) {
        // Verificar si el código SKU es nulo o está vacío
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar productos por código SKU
            List<Producto> productos = productoService.buscarProductosPorCodigoSKU(codigoSKU);

            // Verificar si se encontraron productos
            if (productos.isEmpty()) {
                return ResponseEntity.ok().build(); // 200 OK sin cuerpo
            }

            // Devolver la lista de productos con un código 200 OK
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @GetMapping("/paginados")
    public ResponseEntity<Map<String, Object>> obtenerProductosPaginados(
            @RequestParam(defaultValue = "1") int page,          // Página actual (valor por defecto: 1)
            @RequestParam(defaultValue = "10") int pageSize) {   // Tamaño de la página (valor por defecto: 10)

        // Llamar al servicio para obtener los productos paginados
        Map<String, Object> response = productoService.obtenerProductosPaginados(page, pageSize);

        // Devolver la respuesta con el código de estado HTTP 200 (OK)
        return ResponseEntity.ok(response);
    }

    // Endpoint para subir imágenes
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Verificar si el directorio existe, si no, crearlo
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Obtener el nombre original del archivo
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Guardar el archivo en el directorio
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path);

            // Devolver la ruta absoluta del archivo
            String fileUrl = path.toAbsolutePath().toString(); // Ruta completa
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir la imagen");
        }
    }

    // Endpoint para servir imágenes
    @GetMapping("/imagen/{fileName:.+}")
    public ResponseEntity<FileSystemResource> servirImagen(@PathVariable String fileName) {
        // Construir la ruta completa del archivo
        File file = new File(UPLOAD_DIR + fileName);

        // Verificar si el archivo existe
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Devolver el archivo como recurso
        return ResponseEntity.ok(new FileSystemResource(file));
    }
}
