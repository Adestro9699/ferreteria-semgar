package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.ProductoAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoAlmacenRepository extends JpaRepository<ProductoAlmacen, Integer> {
    
    // Buscar por producto
    List<ProductoAlmacen> findByProductoIdProducto(Integer idProducto);
    
    // Buscar por almacén
    List<ProductoAlmacen> findByAlmacenIdAlmacen(Integer idAlmacen);
    
    // Buscar por producto y almacén específicos
    Optional<ProductoAlmacen> findByProductoIdProductoAndAlmacenIdAlmacen(Integer idProducto, Integer idAlmacen);
    
    // Buscar productos con stock bajo (menos de un umbral)
    @Query("SELECT pa FROM ProductoAlmacen pa WHERE pa.stock < :umbral")
    List<ProductoAlmacen> findByStockBajo(@Param("umbral") Integer umbral);
    
    // Buscar productos sin stock (stock = 0)
    List<ProductoAlmacen> findByStock(Integer stock);
    
    // Buscar por almacén con stock mayor a 0
    @Query("SELECT pa FROM ProductoAlmacen pa WHERE pa.almacen.idAlmacen = :idAlmacen AND pa.stock > 0")
    List<ProductoAlmacen> findByAlmacenConStock(@Param("idAlmacen") Integer idAlmacen);
    
    // Obtener el stock total de un producto en todos los almacenes
    @Query("SELECT SUM(pa.stock) FROM ProductoAlmacen pa WHERE pa.producto.idProducto = :idProducto")
    Integer getStockTotalProducto(@Param("idProducto") Integer idProducto);
} 