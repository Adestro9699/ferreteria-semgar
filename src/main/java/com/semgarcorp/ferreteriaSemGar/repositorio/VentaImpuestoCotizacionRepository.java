package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.VentaImpuestoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaImpuestoCotizacionRepository extends JpaRepository<VentaImpuestoCotizacion, Integer> {
}
