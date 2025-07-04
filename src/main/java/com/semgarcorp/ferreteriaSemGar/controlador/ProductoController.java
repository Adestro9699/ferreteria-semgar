package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.dto.ProductoDTO;
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
    public List<ProductoDTO> listar() {
        return productoService.listarComoDTO();
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerPorId(@PathVariable Integer id) {
        ProductoDTO producto = productoService.obtenerPorIdComoDTO(id);
        if (producto != null) {
            return ResponseEntity.ok(producto); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<ProductoDTO> guardar(@RequestBody ProductoDTO productoDTO) {
        Producto producto = productoDTO.toEntity();
        Producto nuevoProducto = productoService.guardar(producto);
        ProductoDTO nuevoProductoDTO = new ProductoDTO(nuevoProducto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoProducto.getIdProducto()).toUri();
        return ResponseEntity.created(location).body(nuevoProductoDTO);
    }

    // Actualizar un producto existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizar(@PathVariable Integer id, @RequestBody ProductoDTO productoDTO) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente != null) {
            productoDTO.setIdProducto(id);
            productoDTO.updateEntity(productoExistente);
            Producto productoActualizado = productoService.actualizar(productoExistente);
            ProductoDTO productoActualizadoDTO = new ProductoDTO(productoActualizado);
            return ResponseEntity.ok(productoActualizadoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un producto por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Producto productoExistente = productoService.obtenerPorId(id);
        if (productoExistente != null) {
            productoService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar múltiples productos por su ID
    @DeleteMapping("/eliminar-multiples")
    public ResponseEntity<Void> eliminarProductos(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            productoService.eliminarProductosPorIds(ids);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por nombre
    //productos/buscarPorNombre?nombre=nombreDelProducto
    //http://localhost:8080/fs/productos/buscarPorNombre?nombre=Taladro
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String nombre) {
        // Verificar si el nombre es nulo o está vacío
        if (nombre == null || nombre.trim().isEmpty()) {
            // Devolver una respuesta 400 Bad Request sin body
            return ResponseEntity.badRequest().build();
        }

        try {
            // Buscar productos por nombre
            List<ProductoDTO> productos = productoService.buscarProductosPorNombreComoDTO(nombre);

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
            List<ProductoDTO> productos = productoService.buscarProductosPorMarcaComoDTO(marca);

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
            List<ProductoDTO> productos = productoService.buscarProductosPorCategoriaComoDTO(categoria);

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

    //endpoint para buscar productos por estado (devuelve DTO)
    //http://localhost:8080/fs/productos/buscarPorEstado?estado=ACTIVO
    @GetMapping("/buscarPorEstado")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorEstado(@RequestParam String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ProductoDTO> productos = productoService.buscarProductosPorEstadoComoDTO(estado);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por material (devuelve DTO)
    //http://localhost:8080/fs/productos/buscarPorMaterial?material=valorMaterial
    @GetMapping("/buscarPorMaterial")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorMaterial(@RequestParam String material) {
        if (material == null || material.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ProductoDTO> productos = productoService.buscarProductosPorMaterialComoDTO(material);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // endpoint para buscar por codigo de barra
    // productos/buscarPorCodigoBarra?codigoBarra=VALOR_CODIGO_BARRA
    // http://localhost:8080/fs/productos/buscarPorCodigoBarra?codigoBarra=VALOR_CODIGO_BARRA
    @GetMapping("/buscarPorCodigoBarra")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorCodigoBarra(@RequestParam String codigoBarra) {
        // Verificar si el código de barras es nulo o está vacío
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            // Devolver una respuesta 400 Bad Request sin body
            return ResponseEntity.badRequest().build();
        }

        try {
            // Buscar productos por código de barras
            List<ProductoDTO> productos = productoService.buscarProductosPorCodigoBarraComoDTO(codigoBarra);

            // Devolver la lista de productos con un código 200 OK, incluso si está vacía
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            // Manejar otros errores y devolver una respuesta 500 Internal Server Error sin body
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por nombre de proveedor (devuelve DTO)
    //http://localhost:8080/fs/productos/buscarPorNombreProveedor?nombreProveedor=la estrella
    @GetMapping("/buscarPorNombreProveedor")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorNombreProveedor(@RequestParam String nombreProveedor) {
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ProductoDTO> productos = productoService.buscarProductosPorNombreProveedorComoDTO(nombreProveedor);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por nombre de subcategoria (devuelve DTO)
    //http://localhost:8080/fs/productos/buscarPorNombreSubcategoria?nombreSubcategoria=Manzanas
    @GetMapping("/buscarPorNombreSubcategoria")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorNombreSubcategoria(@RequestParam String nombreSubcategoria) {
        if (nombreSubcategoria == null || nombreSubcategoria.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ProductoDTO> productos = productoService.buscarProductosPorNombreSubcategoriaComoDTO(nombreSubcategoria);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //endpoint para buscar productos por codigoSKU (devuelve DTO)
    //http://localhost:8080/fs/productos/buscarPorCodigoSKU?codigoSKU=MART-16OZ
    @GetMapping("/buscarPorCodigoSKU")
    public ResponseEntity<List<ProductoDTO>> buscarProductosPorCodigoSKU(@RequestParam String codigoSKU) {
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            List<ProductoDTO> productos = productoService.buscarProductosPorCodigoSKUComoDTO(codigoSKU);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

    // Endpoint para eliminar imágenes
    @DeleteMapping("/imagen/{fileName:.+}")
    public ResponseEntity<String> eliminarImagen(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return ResponseEntity.ok("Imagen eliminada exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la imagen: " + e.getMessage());
        }
    }

    // ========== ENDPOINTS PARA FILTRAR PRODUCTOS POR ALMACÉN Y SUCURSAL ==========

    /**
     * Obtiene todos los productos que tienen stock en un almacén específico.
     * 
     * @param idAlmacen El ID del almacén.
     * @return Lista de productos con stock en el almacén especificado.
     */
    @GetMapping("/almacen/{idAlmacen}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorAlmacen(@PathVariable Integer idAlmacen) {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosPorAlmacenComoDTO(idAlmacen);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene todos los productos que tienen stock en una sucursal específica.
     * 
     * @param idSucursal El ID de la sucursal.
     * @return Lista de productos con stock en la sucursal especificada.
     */
    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<ProductoDTO>> obtenerProductosPorSucursal(@PathVariable Integer idSucursal) {
        try {
            List<ProductoDTO> productos = productoService.obtenerProductosPorSucursalComoDTO(idSucursal);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el stock de un producto específico en un almacén específico.
     * 
     * @param idProducto El ID del producto.
     * @param idAlmacen El ID del almacén.
     * @return El stock del producto en el almacén especificado.
     */
    @GetMapping("/{idProducto}/almacen/{idAlmacen}/stock")
    public ResponseEntity<Map<String, Object>> obtenerStockProductoEnAlmacen(
            @PathVariable Integer idProducto, 
            @PathVariable Integer idAlmacen) {
        try {
            Integer stock = productoService.getStockEnAlmacen(idProducto, idAlmacen);
            
            Map<String, Object> respuesta = Map.of(
                "idProducto", idProducto,
                "idAlmacen", idAlmacen,
                "stock", stock
            );
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el stock total de un producto en una sucursal específica.
     * 
     * @param idProducto El ID del producto.
     * @param idSucursal El ID de la sucursal.
     * @return El stock total del producto en la sucursal especificada.
     */
    @GetMapping("/{idProducto}/sucursal/{idSucursal}/stock")
    public ResponseEntity<Map<String, Object>> obtenerStockProductoEnSucursal(
            @PathVariable Integer idProducto, 
            @PathVariable Integer idSucursal) {
        try {
            Integer stockTotal = productoService.getStockTotalEnSucursal(idProducto, idSucursal);
            
            Map<String, Object> respuesta = Map.of(
                "idProducto", idProducto,
                "idSucursal", idSucursal,
                "stockTotal", stockTotal
            );
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un resumen del stock de un producto en todos los almacenes.
     * 
     * @param idProducto El ID del producto.
     * @return Resumen del stock del producto en cada almacén.
     */
    @GetMapping("/{idProducto}/stock/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenStockProducto(@PathVariable Integer idProducto) {
        try {
            Map<String, Integer> resumenStock = productoService.getResumenStockPorAlmacen(idProducto);
            Integer stockTotal = productoService.getStockTotal(idProducto);
            
            Map<String, Object> respuesta = Map.of(
                "idProducto", idProducto,
                "stockTotal", stockTotal,
                "stockPorAlmacen", resumenStock
            );
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene el stock total de un producto en todos los almacenes.
     * 
     * @param idProducto El ID del producto.
     * @return El stock total del producto.
     */
    @GetMapping("/{idProducto}/stock/total")
    public ResponseEntity<Map<String, Object>> obtenerStockTotalProducto(@PathVariable Integer idProducto) {
        try {
            Integer stockTotal = productoService.getStockTotal(idProducto);
            
            Map<String, Object> respuesta = Map.of(
                "idProducto", idProducto,
                "stockTotal", stockTotal
            );
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint para obtener los productos con el stock correspondiente al almacén de la sucursal
     * del usuario autenticado. Úsalo para mostrar productos en la vista de ventas.
     * Solo muestra el stock que el usuario puede vender.
     */
    @GetMapping("/venta")
    public ResponseEntity<List<ProductoDTO>> listarProductosParaVenta() {
        List<ProductoDTO> productos = productoService.listarProductosParaUsuarioAutenticado();
        return ResponseEntity.ok(productos);
    }

    /**
     * Endpoint para obtener los productos del almacén del usuario autenticado para transferencias.
     * Úsalo para mostrar productos en el modal de transferencias.
     * Solo muestra productos que tienen stock en el almacén del usuario.
     */
    @GetMapping("/transferencia")
    public ResponseEntity<List<ProductoDTO>> listarProductosParaTransferencia() {
        List<ProductoDTO> productos = productoService.listarProductosParaTransferencia();
        return ResponseEntity.ok(productos);
    }
}
