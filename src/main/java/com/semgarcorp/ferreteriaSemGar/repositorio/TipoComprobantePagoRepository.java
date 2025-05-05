package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoComprobantePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoComprobantePagoRepository extends JpaRepository<TipoComprobantePago, Integer> {

    Optional<TipoComprobantePago> findByNombre(String nombre);


}
