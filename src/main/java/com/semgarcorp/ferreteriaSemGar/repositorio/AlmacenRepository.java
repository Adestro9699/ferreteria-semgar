package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {
    
    // Buscar almacenes por tipo (principales o de sucursal)
    List<Almacen> findByEsPrincipal(Boolean esPrincipal);
    
    // Buscar almacenes por estado
    List<Almacen> findByEstadoAlmacen(Almacen.EstadoAlmacen estadoAlmacen);
    
    // Buscar almacenes por sucursal
    List<Almacen> findBySucursalIdSucursal(Integer idSucursal);
    
    // Buscar almacenes principales (esPrincipal = true)
    List<Almacen> findByEsPrincipalTrue();
    
    // Buscar almacenes de sucursal (esPrincipal = false)
    List<Almacen> findByEsPrincipalFalse();
    
    // Buscar almacenes principales por sucursal específica
    List<Almacen> findBySucursalIdSucursalAndEsPrincipalTrue(Integer idSucursal);
    
    // Buscar almacenes por nombre
    List<Almacen> findByNombreContainingIgnoreCase(String nombre);
} 