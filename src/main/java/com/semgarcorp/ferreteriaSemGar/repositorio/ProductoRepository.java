package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Búsqueda de productos cuyo nombre contenga el texto proporcionado (insensible al caso)
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    // Búsqueda de productos por marca
    List<Producto> findByMarcaContainingIgnoreCase(String marca);

    // Búsqueda de productos por categoría
    List<Producto> findByCategoriaNombreContainingIgnoreCase(String nombreCategoria);
}
