package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.ComprobanteNubeFact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ComprobanteNubeFactRepository extends JpaRepository<ComprobanteNubeFact, Integer> {
    ComprobanteNubeFact findByVentaIdVenta(Integer idVenta);
} 