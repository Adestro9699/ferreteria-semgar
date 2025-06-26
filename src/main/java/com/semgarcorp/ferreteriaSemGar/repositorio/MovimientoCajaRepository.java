package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.MovimientoCaja;
import com.semgarcorp.ferreteriaSemGar.modelo.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimientoCajaRepository extends JpaRepository <MovimientoCaja, Integer>{

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoCaja m " +
           "WHERE m.caja.idCaja = :cajaId " +
           "AND m.tipo = :tipo " +
           "AND m.fecha >= (SELECT c.fechaApertura FROM Caja c WHERE c.idCaja = :cajaId)")
    BigDecimal calcularTotalPorTipo(@Param("cajaId") Integer cajaId, @Param("tipo") TipoMovimiento tipo);

    @Query("SELECT m FROM MovimientoCaja m " +
           "WHERE m.caja.idCaja = :cajaId " +
           "AND m.fecha >= (SELECT c.fechaApertura FROM Caja c WHERE c.idCaja = :cajaId) " +
           "ORDER BY m.fecha")
    List<MovimientoCaja> findByCajaId(@Param("cajaId") Integer cajaId);
}


