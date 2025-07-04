package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.ProductoAlmacen;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoAlmacenRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoAlmacenService {

    private final ProductoAlmacenRepository productoAlmacenRepository;
    private final ProductoRepository productoRepository;
    private final AlmacenService almacenService;

    public ProductoAlmacenService(ProductoAlmacenRepository productoAlmacenRepository,
                                 ProductoRepository productoRepository,
                                 AlmacenService almacenService) {
        this.productoAlmacenRepository = productoAlmacenRepository;
        this.productoRepository = productoRepository;
        this.almacenService = almacenService;
    }

    /**
     * Lista todos los registros de ProductoAlmacen.
     *
     * @return Una lista de todos los registros.
     */
    public List<ProductoAlmacen> listar() {
        return productoAlmacenRepository.findAll();
    }

    /**
     * Obtiene un registro por su ID.
     *
     * @param id El ID del registro a buscar.
     * @return El registro encontrado, o null si no existe.
     */
    public ProductoAlmacen obtenerPorId(Integer id) {
        return productoAlmacenRepository.findById(id).orElse(null);
    }

    /**
     * Guarda un nuevo registro en la base de datos.
     *
     * @param productoAlmacen El registro a guardar.
     * @return El registro guardado.
     */
    @Transactional
    public ProductoAlmacen guardar(ProductoAlmacen productoAlmacen) {
        // Establecer fecha de última actualización
        productoAlmacen.setFechaUltimaActualizacion(LocalDate.now());
        
        return productoAlmacenRepository.save(productoAlmacen);
    }

    /**
     * Actualiza un registro existente en la base de datos.
     *
     * @param productoAlmacen El registro a actualizar.
     * @return El registro actualizado.
     */
    @Transactional
    public ProductoAlmacen actualizar(ProductoAlmacen productoAlmacen) {
        // Establecer fecha de última actualización
        productoAlmacen.setFechaUltimaActualizacion(LocalDate.now());
        
        return productoAlmacenRepository.save(productoAlmacen);
    }

    /**
     * Elimina un registro por su ID.
     *
     * @param id El ID del registro a eliminar.
     */
    public void eliminar(Integer id) {
        productoAlmacenRepository.deleteById(id);
    }

    /**
     * Busca registros por producto.
     *
     * @param idProducto El ID del producto.
     * @return Lista de registros del producto especificado.
     */
    public List<ProductoAlmacen> buscarPorProducto(Integer idProducto) {
        return productoAlmacenRepository.findByProductoIdProducto(idProducto);
    }

    /**
     * Busca registros por almacén.
     *
     * @param idAlmacen El ID del almacén.
     * @return Lista de registros del almacén especificado.
     */
    public List<ProductoAlmacen> buscarPorAlmacen(Integer idAlmacen) {
        return productoAlmacenRepository.findByAlmacenIdAlmacen(idAlmacen);
    }

    /**
     * Busca un registro específico por producto y almacén.
     *
     * @param idProducto El ID del producto.
     * @param idAlmacen El ID del almacén.
     * @return El registro encontrado, o null si no existe.
     */
    public ProductoAlmacen buscarPorProductoYAlmacen(Integer idProducto, Integer idAlmacen) {
        Optional<ProductoAlmacen> optional = productoAlmacenRepository.findByProductoIdProductoAndAlmacenIdAlmacen(idProducto, idAlmacen);
        return optional.orElse(null);
    }

    /**
     * Busca productos con stock bajo.
     *
     * @param umbral El umbral mínimo de stock.
     * @return Lista de registros con stock bajo.
     */
    public List<ProductoAlmacen> buscarStockBajo(Integer umbral) {
        return productoAlmacenRepository.findByStockBajo(umbral);
    }

    /**
     * Busca productos sin stock.
     *
     * @return Lista de registros con stock cero.
     */
    public List<ProductoAlmacen> buscarSinStock() {
        return productoAlmacenRepository.findByStock(0);
    }

    /**
     * Busca productos con stock en un almacén específico.
     *
     * @param idAlmacen El ID del almacén.
     * @return Lista de registros con stock mayor a 0 en el almacén.
     */
    public List<ProductoAlmacen> buscarConStockEnAlmacen(Integer idAlmacen) {
        return productoAlmacenRepository.findByAlmacenConStock(idAlmacen);
    }

    /**
     * Obtiene el stock total de un producto en todos los almacenes.
     *
     * @param idProducto El ID del producto.
     * @return El stock total del producto.
     */
    public Integer getStockTotalProducto(Integer idProducto) {
        Integer stockTotal = productoAlmacenRepository.getStockTotalProducto(idProducto);
        return stockTotal != null ? stockTotal : 0;
    }

    /**
     * Incrementa el stock de un producto en un almacén específico.
     *
     * @param idProducto El ID del producto.
     * @param idAlmacen El ID del almacén.
     * @param cantidad La cantidad a incrementar.
     * @param responsable El responsable de la actualización.
     * @return El registro actualizado.
     */
    @Transactional
    public ProductoAlmacen incrementarStock(Integer idProducto, Integer idAlmacen, Integer cantidad, String responsable) {
        ProductoAlmacen productoAlmacen = buscarPorProductoYAlmacen(idProducto, idAlmacen);
        
        if (productoAlmacen == null) {
            // Crear nuevo registro si no existe
            Producto producto = productoRepository.findById(idProducto).orElse(null);
            Almacen almacen = almacenService.obtenerPorId(idAlmacen);
            
            if (producto == null) {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + idProducto);
            }
            if (almacen == null) {
                throw new IllegalArgumentException("Almacén no encontrado con ID: " + idAlmacen);
            }
            
            productoAlmacen = new ProductoAlmacen();
            productoAlmacen.setProducto(producto);
            productoAlmacen.setAlmacen(almacen);
            productoAlmacen.setStock(0);
            productoAlmacen.setFechaUltimaActualizacion(LocalDate.now());
            productoAlmacen.setResponsableUltimaActualizacion(responsable);
        }
        
        productoAlmacen.setStock(productoAlmacen.getStock() + cantidad);
        productoAlmacen.setFechaUltimaActualizacion(LocalDate.now());
        productoAlmacen.setResponsableUltimaActualizacion(responsable);
        
        return productoAlmacenRepository.save(productoAlmacen);
    }

    /**
     * Reduce el stock de un producto en un almacén específico.
     *
     * @param idProducto El ID del producto.
     * @param idAlmacen El ID del almacén.
     * @param cantidad La cantidad a reducir.
     * @param responsable El responsable de la actualización.
     * @return El registro actualizado.
     */
    @Transactional
    public ProductoAlmacen reducirStock(Integer idProducto, Integer idAlmacen, Integer cantidad, String responsable) {
        ProductoAlmacen productoAlmacen = buscarPorProductoYAlmacen(idProducto, idAlmacen);
        
        if (productoAlmacen == null) {
            throw new IllegalArgumentException("No se encontró el registro de stock para el producto y almacén especificados");
        }
        
        if (productoAlmacen.getStock() < cantidad) {
            throw new IllegalArgumentException("No hay suficiente stock disponible");
        }
        
        productoAlmacen.setStock(productoAlmacen.getStock() - cantidad);
        productoAlmacen.setFechaUltimaActualizacion(LocalDate.now());
        productoAlmacen.setResponsableUltimaActualizacion(responsable);
        
        return productoAlmacenRepository.save(productoAlmacen);
    }
} 