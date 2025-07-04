package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleTransferenciaRepository extends JpaRepository<DetalleTransferencia, Integer> {
    
    /**
     * Buscar detalles por transferencia
     */
    List<DetalleTransferencia> findByTransferenciaIdTransferencia(Integer idTransferencia);
    
    /**
     * Buscar detalles por producto
     */
    List<DetalleTransferencia> findByProductoIdProducto(Integer idProducto);
} 