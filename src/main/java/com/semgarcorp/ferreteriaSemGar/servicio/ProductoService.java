package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductoService {

    private final ProductoRepository productoRepositorio;
    private final ParametroService parametroService; // Servicio para obtener parámetros globales (IGV, utilidad)
    private final UtilidadService utilidadService;   // Servicio para gestionar utilidades específicas

    /**
     * Constructor de ProductoService.
     * Inyecta las dependencias necesarias: ProductoRepository, ParametroService y UtilidadService.
     */
    public ProductoService(ProductoRepository productoRepositorio,
                           ParametroService parametroService,
                           UtilidadService utilidadService) {
        this.productoRepositorio = productoRepositorio;
        this.parametroService = parametroService; // Inyectado
        this.utilidadService = utilidadService;   // Inyectado
    }

    /**
     * Lista todos los productos disponibles.
     *
     * @return Una lista de todos los productos.
     */
    public List<Producto> listar() {
        return productoRepositorio.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id El ID del producto a buscar.
     * @return El producto encontrado, o null si no existe.
     */
    public Producto obtenerPorId(Integer id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param producto El producto a guardar.
     * @return El producto guardado.
     */
    public Producto guardar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param producto El producto a actualizar.
     * @return El producto actualizado.
     */
    public Producto actualizar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id El ID del producto a eliminar.
     */
    public void eliminar(Integer id) {
        productoRepositorio.deleteById(id);
    }

    /**
     * Elimina múltiples productos por sus IDs.
     *
     * @param ids Una lista de IDs de productos a eliminar.
     * @throws IllegalArgumentException Si la lista de IDs está vacía.
     */
    public void eliminarProductosPorIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede estar vacía.");
        }
        productoRepositorio.deleteAllById(ids);
    }

    /**
     * Reduce el stock de un producto cuando se realiza una venta.
     *
     * @param idProducto      El ID del producto cuyo stock se reducirá.
     * @param cantidadVendida La cantidad vendida del producto.
     * @throws EntityNotFoundException Si el producto no existe.
     * @throws IllegalArgumentException Si la cantidad vendida no es válida.
     * @throws IllegalStateException    Si no hay suficiente stock disponible.
     */
    @Transactional
    public void reducirStock(Integer idProducto, BigDecimal cantidadVendida) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        if (cantidadVendida.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número positivo.");
        }

        if (cantidadVendida.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número entero.");
        }

        if (producto.getStock().compareTo(cantidadVendida) < 0) {
            throw new IllegalStateException("No hay suficiente stock para el producto: " + producto.getNombreProducto());
        }

        producto.setStock(producto.getStock().subtract(cantidadVendida));
        productoRepositorio.save(producto);
    }

    /**
     * Incrementa el stock de un producto cuando se realiza una compra.
     *
     * @param idProducto       El ID del producto cuyo stock se incrementará.
     * @param cantidadComprada La cantidad comprada del producto.
     * @throws EntityNotFoundException Si el producto no existe.
     * @throws IllegalArgumentException Si la cantidad comprada no es válida.
     */
    @Transactional
    public void incrementarStock(Integer idProducto, BigDecimal cantidadComprada) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        if (cantidadComprada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad comprada debe ser un número positivo.");
        }

        producto.setStock(producto.getStock().add(cantidadComprada));
        productoRepositorio.save(producto);
    }

    /**
     * Actualiza el precio de venta de un producto basado en el precio de compra,
     * utilizando los parámetros de IGV y utilidad.
     *
     * @param idProducto  El ID del producto cuyo precio de venta se actualizará.
     * @param precioCompra El precio de compra del producto.
     * @throws EntityNotFoundException Si el producto no existe.
     */
    @Transactional
    public void actualizarPrecioVenta(Integer idProducto, BigDecimal precioCompra, BigDecimal utilidadManual) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        BigDecimal utilidad;
        if (utilidadManual != null) {
            // Usar utilidad manual si fue especificada
            utilidad = utilidadManual.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            // Mantener la lógica existente de jerarquía de utilidades
            utilidad = obtenerPorcentajeUtilidad(producto);
        }

        BigDecimal igv = obtenerValorIGV();
        BigDecimal precioVentaSinIGV = precioCompra.multiply(utilidad.add(BigDecimal.ONE));
        BigDecimal precioVentaConIGV = precioVentaSinIGV.multiply(igv.add(BigDecimal.ONE));

        producto.setPrecio(precioVentaConIGV);
        productoRepositorio.save(producto);
    }

    /**
     * Método auxiliar para obtener el valor del IGV desde ParametroService.
     *
     * @return El valor del IGV como un BigDecimal.
     */
    private BigDecimal obtenerValorIGV() {
        return parametroService.obtenerValorIGV(); // Delega al servicio ParametroService
    }
    private BigDecimal obtenerValorUtilidad() {
        return parametroService.obtenerValorUtilidad(); // Usa el método del servicio ParametroService
    }

    /**
     * Método auxiliar para obtener el porcentaje de utilidad según la jerarquía:
     * 1. Utilidad específica del producto.
     * 2. Utilidad específica de la categoría del producto (a través de la subcategoría).
     * 3. Utilidad global predeterminada.
     *
     * @param producto El producto para el cual se busca la utilidad.
     * @return El porcentaje de utilidad como un BigDecimal.
     */
    private BigDecimal obtenerPorcentajeUtilidad(Producto producto) {
        // Buscar utilidad específica para el producto
        BigDecimal utilidadProducto = utilidadService.obtenerPorcentajeUtilidadPorProducto(producto)
                .orElse(null);

        if (utilidadProducto != null) {
            return utilidadProducto; // Ya viene convertido a decimal desde el servicio
        }

        // Obtener la categoría del producto a través de la subcategoría
        Categoria categoria = producto.getSubcategoria() != null ? producto.getSubcategoria().getCategoria() : null;

        // Buscar utilidad específica para la categoría del producto
        if (categoria != null) {
            BigDecimal utilidadCategoria = utilidadService.obtenerPorcentajeUtilidadPorCategoria(categoria)
                    .orElse(null);

            if (utilidadCategoria != null) {
                return utilidadCategoria; // Ya viene convertido a decimal desde el servicio
            }
        }

        // Usar utilidad global predeterminada
        return parametroService.obtenerValorUtilidad(); // Delega al servicio ParametroService
    }

    /**
     * Busca productos por nombre (insensible a mayúsculas/minúsculas).
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return Una lista de productos que coinciden con el nombre, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorNombre(String nombreProducto) {
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByNombreProductoContainingIgnoreCase(nombreProducto.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por marca (insensible a mayúsculas/minúsculas).
     *
     * @param marca La marca del producto a buscar.
     * @return Una lista de productos que coinciden con la marca, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByMarcaContainingIgnoreCase(marca.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por categoría (insensible a mayúsculas/minúsculas).
     *
     * @param nombreCategoria El nombre de la categoría a buscar.
     * @return Una lista de productos que pertenecen a la categoría, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorCategoria(String nombreCategoria) {
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByCategoriaNombreContainingIgnoreCase(nombreCategoria.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por estado (activo/inactivo).
     *
     * @param estado El estado del producto a buscar (ACTIVO o INACTIVO).
     * @return Una lista de productos que coinciden con el estado, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            Producto.EstadoProducto estadoProducto = Producto.EstadoProducto.valueOf(estado.toUpperCase());
            return productoRepositorio.findByEstadoProducto(estadoProducto);
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por material (insensible a mayúsculas/minúsculas).
     *
     * @param material El material del producto a buscar.
     * @return Una lista de productos que coinciden con el material, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorMaterial(String material) {
        if (material == null || material.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByMaterialContainingIgnoreCase(material.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por código de barra.
     *
     * @param codigoBarra El código de barra del producto a buscar.
     * @return Una lista de productos que coinciden con el código de barra, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorCodigoBarra(String codigoBarra) {
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByCodigoBarra(codigoBarra.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por nombre del proveedor (insensible a mayúsculas/minúsculas).
     *
     * @param nombreProveedor El nombre del proveedor a buscar.
     * @return Una lista de productos que coinciden con el proveedor, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorNombreProveedor(String nombreProveedor) {
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByProveedorNombreContainingIgnoreCase(nombreProveedor.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por nombre de la subcategoría (insensible a mayúsculas/minúsculas).
     *
     * @param nombreSubcategoria El nombre de la subcategoría a buscar.
     * @return Una lista de productos que coinciden con la subcategoría, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorNombreSubcategoria(String nombreSubcategoria) {
        if (nombreSubcategoria == null || nombreSubcategoria.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findBySubcategoriaNombreContainingIgnoreCase(nombreSubcategoria.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Busca productos por código SKU (insensible a mayúsculas/minúsculas).
     *
     * @param codigoSKU El código SKU del producto a buscar.
     * @return Una lista de productos que coinciden con el código SKU, o una lista vacía si no hay coincidencias.
     */
    public List<Producto> buscarProductosPorCodigoSKU(String codigoSKU) {
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return productoRepositorio.findByCodigoSKUContainingIgnoreCase(codigoSKU.trim());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
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

    public void validarStock(Integer idProducto, BigDecimal cantidadRequerida) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (cantidadRequerida.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad requerida debe ser mayor a cero");
        }

        if (producto.getStock().compareTo(cantidadRequerida) < 0) {
            throw new IllegalStateException(
                    String.format("Stock insuficiente para %s. Stock: %s, Requerido: %s",
                            producto.getNombreProducto(),
                            producto.getStock().toPlainString(),
                            cantidadRequerida.toPlainString())
            );
        }
    }
}