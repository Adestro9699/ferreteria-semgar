package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TipoCambioRepository extends JpaRepository<TipoCambio, Integer> {

    Optional<TipoCambio> findByFecha(LocalDate fecha);

}
