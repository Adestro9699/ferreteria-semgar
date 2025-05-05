package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepositorio;

    // Constructor
    public ProductoService(ProductoRepository productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> listar() {
        return productoRepositorio.findAll();
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public Producto guardar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    public Producto actualizar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepositorio.deleteById(id);
    }

    public void eliminarProductosPorIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede estar vacía.");
        }
        // Metodo de JpaRepository para eliminar por IDs
        productoRepositorio.deleteAllById(ids);
    }

    //Metodo para paginación de productos
    public Map<String, Object> obtenerProductosPaginados(int page, int pageSize) {
        // Calcular el offset (PageRequest usa índices base 0)
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        // Obtener los productos paginados
        Page<Producto> productosPage = productoRepositorio.findAll(pageable);
        // Crear la respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("data", productosPage.getContent()); // Lista de productos de la página actual
        response.put("total", productosPage.getTotalElements()); // Total de productos
        response.put("page", page); // Página actual
        response.put("pageSize", pageSize); // Tamaño de la página
        response.put("totalPages", productosPage.getTotalPages()); // Total de páginas
        return response;
    }

    //Metodo para reducir stock cuando una venta cambia a estado COMPLETADA
    @Transactional
    public void reducirStock(Integer idProducto, BigDecimal cantidadVendida) {
        // 1. Buscar el producto por ID
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));
        // 2. Validar que la cantidad sea un número positivo
        if (cantidadVendida.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número positivo.");
        }
        // 3. Validar que la cantidad no tenga decimales
        if (cantidadVendida.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número entero.");
        }
        // 4. Validar que haya suficiente stock
        if (producto.getStock().compareTo(cantidadVendida) < 0) {
            throw new IllegalStateException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
        }
        // 5. Reducir el stock
        producto.setStock(producto.getStock().subtract(cantidadVendida));
        // 6. Guardar el producto actualizado
        productoRepositorio.save(producto);
    }

    // Metodo para buscar productos por nombre
    public List<Producto> buscarProductosPorNombre(String nombreProducto) {
        // Verificar si el nombre es nulo o está vacío
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            // Si el nombre es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }
        try {
            // Buscar productos que contengan el nombre ingresado (sin importar mayúsculas/minúsculas)
            return productoRepositorio.findByNombreProductoContainingIgnoreCase(nombreProducto.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por marca
    public List<Producto> buscarProductosPorMarca(String marca) {
        // Verificar si la marca es nula o está vacía
        if (marca == null || marca.trim().isEmpty()) {
            // Si la marca es nula o vacía, devolver una lista vacía
            return Collections.emptyList();
        }
        try {
            // Buscar productos cuya marca contenga la cadena ingresada (sin importar mayúsculas/minúsculas)
            return productoRepositorio.findByMarcaContainingIgnoreCase(marca.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por categoría
    public List<Producto> buscarProductosPorCategoria(String nombreCategoria) {
        // Verificar si el nombre de la categoría es nulo o está vacío
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            // Si es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }
        try {
            // Buscar productos cuya categoría contenga el nombre ingresado (ignorar mayúsculas/minúsculas)
            return productoRepositorio.findByCategoriaNombreContainingIgnoreCase(nombreCategoria.trim());
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por estado
    public List<Producto> buscarProductosPorEstado(String estado) {
        // Verificar si el estado es nulo o está vacío
        if (estado == null || estado.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            // Convertir el String a enum EstadoProducto (ignorando mayúsculas/minúsculas)
            Producto.EstadoProducto estadoProducto = Producto.EstadoProducto.valueOf(estado.toUpperCase());
            // Buscar productos por estado
            return productoRepositorio.findByEstadoProducto(estadoProducto);
        } catch (IllegalArgumentException e) {
            // Manejar el caso en que el estado no sea válido
            return Collections.emptyList();
        } catch (Exception e) {
            // Manejar otros errores
            return Collections.emptyList();
        }
    }

    // Metodo para buscar productos por material
    public List<Producto> buscarProductosPorMaterial(String material) {
        // Verificar si el material es nulo o está vacío
        if (material == null || material.trim().isEmpty()) {
            // Si el material es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }
        try {
            // Buscar productos por material (ignorando mayúsculas/minúsculas)
            return productoRepositorio.findByMaterialContainingIgnoreCase(material.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por código de barra
    public List<Producto> buscarProductosPorCodigoBarra(String codigoBarra) {
        // Verificar si el código de barra es nulo o está vacío
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            // Si el código de barra es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }
        try {
            // Buscar productos por código de barra
            return productoRepositorio.findByCodigoBarra(codigoBarra.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por proveedor
    public List<Producto> buscarProductosPorNombreProveedor(String nombreProveedor) {
        // Verificar si el nombre del proveedor es nulo o está vacío
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            // Buscar productos por nombre del proveedor
            return productoRepositorio.findByProveedorNombreContainingIgnoreCase(nombreProveedor.trim());
        } catch (Exception e) {
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por nombre de subcategoría
    public List<Producto> buscarProductosPorNombreSubcategoria(String nombreSubcategoria) {
        // Verificar si el nombre de la subcategoría es nulo o está vacío
        if (nombreSubcategoria == null || nombreSubcategoria.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            // Buscar productos por nombre de la subcategoría
            return productoRepositorio.findBySubcategoriaNombreContainingIgnoreCase(nombreSubcategoria.trim());
        } catch (Exception e) {
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // Metodo para buscar productos por código SKU
    public List<Producto> buscarProductosPorCodigoSKU(String codigoSKU) {
        // Verificar si el código SKU es nulo o está vacío
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            return Collections.emptyList(); // Retorna una lista vacía si el código SKU está vacío
        }
        try {
            // Buscar productos por código SKU
            return productoRepositorio.findByCodigoSKUContainingIgnoreCase(codigoSKU.trim());
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver una lista vacía
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    /**
     * Metodo para actualizar el precio de venta de un producto basado en el precio de compra,
     * utilizando los parámetros de IGV y utilidad.
     */
    @Transactional
    public void actualizarPrecioVenta(Integer idProducto, BigDecimal precioCompra) {
        // 1. Buscar el producto por ID
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        // 2. Obtener los valores de IGV y utilidad desde ParametroService
        BigDecimal igv = obtenerValorIGV();
        BigDecimal utilidad = obtenerValorUtilidad();

        // 3. Calcular el precio de venta
        BigDecimal precioVentaSinIGV = precioCompra.multiply(utilidad.add(BigDecimal.ONE));
        BigDecimal precioVentaConIGV = precioVentaSinIGV.multiply(igv.add(BigDecimal.ONE));

        // 4. Actualizar el precio de venta en el producto
        producto.setPrecio(precioVentaConIGV);

        // 5. Guardar el producto actualizado
        productoRepositorio.save(producto);
    }

    /**
     * Metodo auxiliar para obtener el valor del IGV desde ParametroService.
     */
    private BigDecimal obtenerValorIGV() {
        // Supongamos que tienes acceso al servicio ParametroService
        // Aquí debes inyectar ParametroService en el constructor de ProductoService
        // y usar el metodo obtenerValorIGV() de ese servicio.
        return BigDecimal.valueOf(0.18); // Ejemplo temporal
    }

    /**
     * Metodo auxiliar para obtener el valor de la utilidad desde ParametroService.
     */
    private BigDecimal obtenerValorUtilidad() {
        // Supongamos que tienes acceso al servicio ParametroService
        // Aquí debes inyectar ParametroService en el constructor de ProductoService
        // y usar el metodo obtenerValorUtilidad() de ese servicio.
        return BigDecimal.valueOf(0.30); // Ejemplo temporal
    }

    @Transactional
    public void incrementarStock(Integer idProducto, BigDecimal cantidadComprada) {
        // 1. Buscar el producto por ID
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        // 2. Validar que la cantidad sea un número positivo
        if (cantidadComprada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad comprada debe ser un número positivo.");
        }

        // 3. Incrementar el stock
        producto.setStock(producto.getStock().add(cantidadComprada));

        // 4. Guardar el producto actualizado
        productoRepositorio.save(producto);
    }
}