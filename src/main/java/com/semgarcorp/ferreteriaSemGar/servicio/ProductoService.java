package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.ProductoAlmacen;
import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import com.semgarcorp.ferreteriaSemGar.dto.ProductoDTO;
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
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.ArrayList;
import org.springframework.security.core.context.SecurityContextHolder;
import com.semgarcorp.ferreteriaSemGar.servicio.UsuarioContextService;

@Service
public class ProductoService {

    private final ProductoRepository productoRepositorio;
    private final ParametroService parametroService; // Servicio para obtener parámetros globales (IGV, utilidad)
    private final UtilidadService utilidadService;   // Servicio para gestionar utilidades específicas
    private final ProductoAlmacenService productoAlmacenService;
    private final AlmacenService almacenService;
    private final UsuarioService usuarioService;
    private final UsuarioContextService usuarioContextService;

    /**
     * Constructor de ProductoService.
     * Inyecta las dependencias necesarias: ProductoRepository, ParametroService y UtilidadService.
     */
    public ProductoService(ProductoRepository productoRepositorio,
                           ParametroService parametroService,
                           UtilidadService utilidadService,
                           ProductoAlmacenService productoAlmacenService,
                           AlmacenService almacenService,
                           UsuarioService usuarioService,
                           UsuarioContextService usuarioContextService) {
        this.productoRepositorio = productoRepositorio;
        this.parametroService = parametroService; // Inyectado
        this.utilidadService = utilidadService;   // Inyectado
        this.productoAlmacenService = productoAlmacenService;
        this.almacenService = almacenService;
        this.usuarioService = usuarioService;
        this.usuarioContextService = usuarioContextService;
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
     * MÉTODO DEPRECADO: Usar reducirStockEnAlmacen() en su lugar.
     *
     * @param idProducto      El ID del producto cuyo stock se reducirá.
     * @param cantidadVendida La cantidad vendida del producto.
     * @throws EntityNotFoundException Si el producto no existe.
     * @throws IllegalArgumentException Si la cantidad vendida no es válida.
     * @throws IllegalStateException    Si no hay suficiente stock disponible.
     */
    @Transactional
    @Deprecated
    public void reducirStock(Integer idProducto, BigDecimal cantidadVendida) {
        // Por defecto, reducir del primer almacén disponible (compatibilidad)
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        List<ProductoAlmacen> productoAlmacenes = productoAlmacenService.buscarPorProducto(idProducto);
        
        if (productoAlmacenes.isEmpty()) {
            throw new IllegalStateException("No hay stock disponible para el producto: " + producto.getNombreProducto());
        }

        // Buscar el almacén con más stock disponible
        ProductoAlmacen almacenConStock = productoAlmacenes.stream()
            .filter(pa -> pa.getStock() >= cantidadVendida.intValue())
            .max((pa1, pa2) -> Integer.compare(pa1.getStock(), pa2.getStock()))
            .orElseThrow(() -> new IllegalStateException("No hay suficiente stock disponible para el producto: " + producto.getNombreProducto()));
        
        reducirStockEnAlmacen(idProducto, almacenConStock.getAlmacen().getIdAlmacen(), cantidadVendida, "Sistema");
    }

    /**
     * Incrementa el stock de un producto cuando se realiza una compra.
     * MÉTODO DEPRECADO: Usar incrementarStockEnAlmacen() en su lugar.
     *
     * @param idProducto       El ID del producto cuyo stock se incrementará.
     * @param cantidadComprada La cantidad comprada del producto.
     * @throws EntityNotFoundException Si el producto no existe.
     * @throws IllegalArgumentException Si la cantidad comprada no es válida.
     */
    @Transactional
    @Deprecated
    public void incrementarStock(Integer idProducto, BigDecimal cantidadComprada) {
        // Por defecto, incrementar en el almacén principal (compatibilidad)
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        List<ProductoAlmacen> almacenesPrincipales = productoAlmacenService.buscarPorProducto(idProducto).stream()
            .filter(pa -> Boolean.TRUE.equals(pa.getAlmacen().getEsPrincipal()))
            .collect(Collectors.toList());
        
        Integer idAlmacenPrincipal;
        if (almacenesPrincipales.isEmpty()) {
            // Si no hay almacén principal, usar el primer almacén disponible
            List<ProductoAlmacen> todosLosAlmacenes = productoAlmacenService.buscarPorProducto(idProducto);
            if (todosLosAlmacenes.isEmpty()) {
                throw new IllegalStateException("No hay almacenes disponibles para el producto: " + producto.getNombreProducto());
            }
            idAlmacenPrincipal = todosLosAlmacenes.get(0).getAlmacen().getIdAlmacen();
        } else {
            idAlmacenPrincipal = almacenesPrincipales.get(0).getAlmacen().getIdAlmacen();
        }
        
        incrementarStockEnAlmacen(idProducto, idAlmacenPrincipal, cantidadComprada, "Sistema");
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

        // Usar el nuevo método de validación por almacén
        Integer stockTotal = getStockTotal(idProducto);
        if (stockTotal < cantidadRequerida.intValue()) {
            throw new IllegalStateException(
                    String.format("Stock insuficiente para %s. Stock: %s, Requerido: %s",
                            producto.getNombreProducto(),
                            stockTotal.toString(),
                            cantidadRequerida.toPlainString())
            );
        }
    }

    /**
     * Reduce el stock de un producto en un almacén específico.
     *
     * @param idProducto      El ID del producto cuyo stock se reducirá.
     * @param idAlmacen       El ID del almacén donde se reducirá el stock.
     * @param cantidadVendida La cantidad vendida del producto.
     * @param responsable     El responsable de la operación.
     * @throws EntityNotFoundException Si el producto o almacén no existe.
     * @throws IllegalArgumentException Si la cantidad vendida no es válida.
     * @throws IllegalStateException    Si no hay suficiente stock disponible.
     */
    @Transactional
    public void reducirStockEnAlmacen(Integer idProducto, Integer idAlmacen, BigDecimal cantidadVendida, String responsable) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        if (cantidadVendida.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número positivo.");
        }

        if (cantidadVendida.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser un número entero.");
        }

        // Buscar el ProductoAlmacen específico
        ProductoAlmacen productoAlmacen = productoAlmacenService.buscarPorProductoYAlmacen(idProducto, idAlmacen);
        
        if (productoAlmacen == null) {
            throw new EntityNotFoundException("No se encontró stock del producto en el almacén especificado");
        }

        if (productoAlmacen.getStock() < cantidadVendida.intValue()) {
            throw new IllegalStateException("No hay suficiente stock en el almacén para el producto: " + producto.getNombreProducto());
        }

        // Reducir stock en el almacén específico
        productoAlmacenService.reducirStock(idProducto, idAlmacen, cantidadVendida.intValue(), responsable);
    }

    /**
     * Incrementa el stock de un producto en un almacén específico.
     *
     * @param idProducto       El ID del producto cuyo stock se incrementará.
     * @param idAlmacen        El ID del almacén donde se incrementará el stock.
     * @param cantidadComprada La cantidad comprada del producto.
     * @param responsable      El responsable de la operación.
     * @throws EntityNotFoundException Si el producto o almacén no existe.
     * @throws IllegalArgumentException Si la cantidad comprada no es válida.
     */
    @Transactional
    public void incrementarStockEnAlmacen(Integer idProducto, Integer idAlmacen, BigDecimal cantidadComprada, String responsable) {
        Producto producto = productoRepositorio.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + idProducto + " no encontrado"));

        if (cantidadComprada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad comprada debe ser un número positivo.");
        }

        // Buscar o crear el ProductoAlmacen específico
        ProductoAlmacen productoAlmacen = productoAlmacenService.buscarPorProductoYAlmacen(idProducto, idAlmacen);
        
        if (productoAlmacen == null) {
            // Crear nuevo registro de ProductoAlmacen
            productoAlmacen = new ProductoAlmacen();
            productoAlmacen.setProducto(producto);
            productoAlmacen.setAlmacen(almacenService.obtenerPorId(idAlmacen));
            productoAlmacen.setStock(0);
            productoAlmacenService.guardar(productoAlmacen);
        }

        // Incrementar stock en el almacén específico
        productoAlmacenService.incrementarStock(idProducto, idAlmacen, cantidadComprada.intValue(), responsable);
    }

    /**
     * Valida si hay suficiente stock de un producto en un almacén específico.
     *
     * @param idProducto El ID del producto a validar.
     * @param idAlmacen  El ID del almacén donde validar el stock.
     * @param cantidad   La cantidad requerida.
     * @return true si hay suficiente stock, false en caso contrario.
     */
    public boolean validarStockEnAlmacen(Integer idProducto, Integer idAlmacen, BigDecimal cantidad) {
        ProductoAlmacen productoAlmacen = productoAlmacenService.buscarPorProductoYAlmacen(idProducto, idAlmacen);
        
        if (productoAlmacen == null) {
            return false;
        }
        
        return productoAlmacen.getStock() >= cantidad.intValue();
    }

    /**
     * Obtiene el stock disponible de un producto en un almacén específico.
     *
     * @param idProducto El ID del producto.
     * @param idAlmacen  El ID del almacén.
     * @return El stock disponible en el almacén, o 0 si no existe.
     */
    public Integer getStockEnAlmacen(Integer idProducto, Integer idAlmacen) {
        ProductoAlmacen productoAlmacen = productoAlmacenService.buscarPorProductoYAlmacen(idProducto, idAlmacen);
        return productoAlmacen != null ? productoAlmacen.getStock() : 0;
    }

    /**
     * Obtiene el stock total de un producto en todos los almacenes.
     *
     * @param idProducto El ID del producto.
     * @return El stock total del producto.
     */
    public Integer getStockTotal(Integer idProducto) {
        return productoAlmacenService.getStockTotalProducto(idProducto);
    }

    /**
     * Obtiene todos los productos que tienen stock en un almacén específico.
     *
     * @param idAlmacen El ID del almacén.
     * @return Lista de productos con stock en el almacén especificado.
     */
    public List<Producto> obtenerProductosPorAlmacen(Integer idAlmacen) {
        List<ProductoAlmacen> productoAlmacenes = productoAlmacenService.buscarPorAlmacen(idAlmacen);
        
        return productoAlmacenes.stream()
            .filter(pa -> pa.getStock() > 0) // Solo productos con stock > 0
            .map(ProductoAlmacen::getProducto)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los productos que tienen stock en una sucursal específica.
     *
     * @param idSucursal El ID de la sucursal.
     * @return Lista de productos con stock en la sucursal especificada.
     */
    public List<Producto> obtenerProductosPorSucursal(Integer idSucursal) {
        // Obtener todos los almacenes de la sucursal
        List<Almacen> almacenesSucursal = almacenService.buscarPorSucursal(idSucursal);
        
        // Obtener todos los ProductoAlmacen de esos almacenes
        Set<Producto> productosConStock = new HashSet<>();
        
        for (Almacen almacen : almacenesSucursal) {
            List<ProductoAlmacen> productoAlmacenes = productoAlmacenService.buscarPorAlmacen(almacen.getIdAlmacen());
            
            productoAlmacenes.stream()
                .filter(pa -> pa.getStock() > 0) // Solo productos con stock > 0
                .map(ProductoAlmacen::getProducto)
                .forEach(productosConStock::add);
        }
        
        return new ArrayList<>(productosConStock);
    }

    /**
     * Obtiene el stock total de un producto en una sucursal específica.
     *
     * @param idProducto El ID del producto.
     * @param idSucursal El ID de la sucursal.
     * @return El stock total del producto en la sucursal.
     */
    public Integer getStockTotalEnSucursal(Integer idProducto, Integer idSucursal) {
        // Obtener todos los almacenes de la sucursal
        List<Almacen> almacenesSucursal = almacenService.buscarPorSucursal(idSucursal);
        
        int stockTotal = 0;
        
        for (Almacen almacen : almacenesSucursal) {
            ProductoAlmacen productoAlmacen = productoAlmacenService.buscarPorProductoYAlmacen(idProducto, almacen.getIdAlmacen());
            if (productoAlmacen != null) {
                stockTotal += productoAlmacen.getStock();
            }
        }
        
        return stockTotal;
    }

    /**
     * Obtiene un resumen de stock de un producto en todos los almacenes.
     *
     * @param idProducto El ID del producto.
     * @return Mapa con el stock del producto en cada almacén.
     */
    public Map<String, Integer> getResumenStockPorAlmacen(Integer idProducto) {
        List<ProductoAlmacen> productoAlmacenes = productoAlmacenService.buscarPorProducto(idProducto);
        
        Map<String, Integer> resumen = new HashMap<>();
        
        for (ProductoAlmacen pa : productoAlmacenes) {
            String nombreAlmacen = pa.getAlmacen().getNombre();
            resumen.put(nombreAlmacen, pa.getStock());
        }
        
        return resumen;
    }

    // ========== MÉTODOS PARA CONVERTIR A DTO ==========

    /**
     * Convierte un Producto a ProductoDTO.
     *
     * @param producto El producto a convertir.
     * @return El DTO del producto.
     */
    public ProductoDTO convertirADTO(Producto producto) {
        return new ProductoDTO(producto);
    }

    /**
     * Convierte una lista de Productos a ProductoDTOs.
     *
     * @param productos La lista de productos a convertir.
     * @return La lista de DTOs de productos.
     */
    public List<ProductoDTO> convertirListaADTO(List<Producto> productos) {
        return productos.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    /**
     * Lista todos los productos como DTOs.
     *
     * @return Una lista de todos los productos como DTOs.
     */
    public List<ProductoDTO> listarComoDTO() {
        List<Producto> productos = listar();
        return convertirListaADTO(productos);
    }

    /**
     * Obtiene un producto por su ID como DTO.
     *
     * @param id El ID del producto a buscar.
     * @return El DTO del producto encontrado, o null si no existe.
     */
    public ProductoDTO obtenerPorIdComoDTO(Integer id) {
        Producto producto = obtenerPorId(id);
        return producto != null ? convertirADTO(producto) : null;
    }

    /**
     * Obtiene todos los productos que tienen stock en un almacén específico como DTOs.
     *
     * @param idAlmacen El ID del almacén.
     * @return Lista de DTOs de productos con stock en el almacén especificado.
     */
    public List<ProductoDTO> obtenerProductosPorAlmacenComoDTO(Integer idAlmacen) {
        List<Producto> productos = obtenerProductosPorAlmacen(idAlmacen);
        return convertirListaADTO(productos);
    }

    /**
     * Obtiene todos los productos que tienen stock en una sucursal específica como DTOs.
     *
     * @param idSucursal El ID de la sucursal.
     * @return Lista de DTOs de productos con stock en la sucursal especificada.
     */
    public List<ProductoDTO> obtenerProductosPorSucursalComoDTO(Integer idSucursal) {
        List<Producto> productos = obtenerProductosPorSucursal(idSucursal);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por nombre y los devuelve como DTOs.
     *
     * @param nombreProducto El nombre del producto a buscar.
     * @return Lista de DTOs de productos que coinciden con el nombre.
     */
    public List<ProductoDTO> buscarProductosPorNombreComoDTO(String nombreProducto) {
        List<Producto> productos = buscarProductosPorNombre(nombreProducto);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por marca y los devuelve como DTOs.
     *
     * @param marca La marca del producto a buscar.
     * @return Lista de DTOs de productos que coinciden con la marca.
     */
    public List<ProductoDTO> buscarProductosPorMarcaComoDTO(String marca) {
        List<Producto> productos = buscarProductosPorMarca(marca);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por categoría y los devuelve como DTOs.
     *
     * @param nombreCategoria El nombre de la categoría a buscar.
     * @return Lista de DTOs de productos que pertenecen a la categoría.
     */
    public List<ProductoDTO> buscarProductosPorCategoriaComoDTO(String nombreCategoria) {
        List<Producto> productos = buscarProductosPorCategoria(nombreCategoria);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por código de barras y los devuelve como DTOs.
     *
     * @param codigoBarra El código de barras del producto a buscar.
     * @return Lista de DTOs de productos que coinciden con el código de barras.
     */
    public List<ProductoDTO> buscarProductosPorCodigoBarraComoDTO(String codigoBarra) {
        List<Producto> productos = buscarProductosPorCodigoBarra(codigoBarra);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por estado y los devuelve como DTOs.
     */
    public List<ProductoDTO> buscarProductosPorEstadoComoDTO(String estado) {
        List<Producto> productos = buscarProductosPorEstado(estado);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por material y los devuelve como DTOs.
     */
    public List<ProductoDTO> buscarProductosPorMaterialComoDTO(String material) {
        List<Producto> productos = buscarProductosPorMaterial(material);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por nombre de proveedor y los devuelve como DTOs.
     */
    public List<ProductoDTO> buscarProductosPorNombreProveedorComoDTO(String nombreProveedor) {
        List<Producto> productos = buscarProductosPorNombreProveedor(nombreProveedor);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por nombre de subcategoría y los devuelve como DTOs.
     */
    public List<ProductoDTO> buscarProductosPorNombreSubcategoriaComoDTO(String nombreSubcategoria) {
        List<Producto> productos = buscarProductosPorNombreSubcategoria(nombreSubcategoria);
        return convertirListaADTO(productos);
    }

    /**
     * Busca productos por código SKU y los devuelve como DTOs.
     */
    public List<ProductoDTO> buscarProductosPorCodigoSKUComoDTO(String codigoSKU) {
        List<Producto> productos = buscarProductosPorCodigoSKU(codigoSKU);
        return convertirListaADTO(productos);
    }

    /**
     * Lista los productos mostrando el stock solo del almacén de la sucursal del usuario autenticado.
     * Devuelve una lista de ProductoDTO con el stock correspondiente a ese almacén en el campo stockSucursal.
     */
    public List<ProductoDTO> listarProductosParaUsuarioAutenticado() {
        try {
            // Usar el UsuarioContextService para obtener el almacén del usuario
            Integer idAlmacenUsuario = usuarioContextService.obtenerIdAlmacenUsuario();
            
            // Listar todos los productos y mapear el stock de ese almacén
            List<Producto> productos = listar();
            return productos.stream().map(producto -> {
                // Buscar el stock en ese almacén
                Integer stockSucursal = productoAlmacenService.buscarPorProducto(producto.getIdProducto()).stream()
                    .filter(pa -> pa.getAlmacen().getIdAlmacen().equals(idAlmacenUsuario))
                    .map(pa -> pa.getStock() != null ? pa.getStock() : 0)
                    .findFirst().orElse(0);
                ProductoDTO dto = new ProductoDTO(producto);
                dto.setStockSucursal(stockSucursal); // Este es el campo correcto para ventas
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos para usuario autenticado: " + e.getMessage(), e);
        }
    }

    /**
     * Lista los productos con stock en el almacén del usuario autenticado para transferencias.
     * Solo devuelve productos que tienen stock > 0 en el almacén del usuario.
     * Úsalo para mostrar productos en el modal de transferencias.
     */
    public List<ProductoDTO> listarProductosParaTransferencia() {
        try {
            // Usar el UsuarioContextService para obtener el almacén del usuario
            Integer idAlmacenUsuario = usuarioContextService.obtenerIdAlmacenUsuario();
            
            // Obtener productos con stock en ese almacén
            List<ProductoAlmacen> productoAlmacenes = productoAlmacenService.buscarConStockEnAlmacen(idAlmacenUsuario);
            
            // Convertir a DTOs y establecer el stock
            return productoAlmacenes.stream().map(pa -> {
                ProductoDTO dto = new ProductoDTO(pa.getProducto());
                dto.setStockSucursal(pa.getStock()); // Stock disponible para transferir
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos para transferencia: " + e.getMessage(), e);
        }
    }
}