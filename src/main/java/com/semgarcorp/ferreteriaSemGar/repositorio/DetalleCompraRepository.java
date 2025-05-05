package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
            
@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    /**
     * Consulta para calcular la suma de los subtotales de los detalles de una compra específica.
     */
    @Query("SELECT SUM(dc.subtotal) FROM DetalleCompra dc WHERE dc.compra.idCompra = :idCompra")
    Optional<BigDecimal> calcularTotalPorCompra(@Param("idCompra") Integer idCompra);

    /**
     * Consulta para obtener todos los detalles de una compra específica.
     */
    @Query("SELECT dc FROM DetalleCompra dc WHERE dc.compra.idCompra = :idCompra")
    List<DetalleCompra> findByCompraId(@Param("idCompra") Integer idCompra);
}