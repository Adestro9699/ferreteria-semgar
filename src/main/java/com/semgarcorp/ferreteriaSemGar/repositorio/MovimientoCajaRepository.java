package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.modelo.MovimientoCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoCajaRepository extends JpaRepository <MovimientoCaja, Integer>{

    List<MovimientoCaja> findByCaja(Caja caja); // Buscar movimientos por caja

    List<MovimientoCaja> findByCajaAndCierreCajaIsNull(Caja caja);

}
