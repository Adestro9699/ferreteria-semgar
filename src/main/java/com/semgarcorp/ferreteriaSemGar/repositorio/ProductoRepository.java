package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Metodo para buscar productos por nombre
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    // Metodo para buscar productos por marca
    List<Producto> findByMarcaContainingIgnoreCase(String marca);

    @Query("SELECT p FROM Producto p JOIN p.subcategoria s JOIN s.categoria c WHERE LOWER(c.nombre) LIKE LOWER(concat(" +
            "'%', :nombreCategoria, '%'))")
    List<Producto> findByCategoriaNombreContainingIgnoreCase(@Param("nombreCategoria") String nombreCategoria);

    // Metodo para buscar productos por estado
    List<Producto> findByEstadoProducto(Producto.EstadoProducto estadoProducto);

    // Metodo para buscar productos por material
    List<Producto> findByMaterialContainingIgnoreCase(String material);

    // Metodo para buscar productos por código de barra
    List<Producto> findByCodigoBarra(String codigoBarra);

    // Metodo para buscar productos por nombre del proveedor
    List<Producto> findByProveedorNombreContainingIgnoreCase(String nombreProveedor);

    // Metodo para buscar productos por nombre de subcategoria
    List<Producto> findBySubcategoriaNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCodigoSKUContainingIgnoreCase(String codigoSKU);

}
