package com.semgarcorp.ferreteriaSemGar.repositorio;


import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.modelo.CierreCaja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CierreCajaRepository extends JpaRepository<CierreCaja, Integer> {

    List<CierreCaja> findByCajaOrderByFechaCierreDesc(Caja caja);

    Optional<CierreCaja> findFirstByCajaOrderByFechaCierreDesc(Caja caja);
}
