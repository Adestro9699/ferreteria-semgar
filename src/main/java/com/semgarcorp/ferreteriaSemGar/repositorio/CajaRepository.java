package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.modelo.EstadoCaja;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaRepository extends JpaRepository<Caja, Integer> {

    boolean existsByNombreCaja(String nombreCaja);

    boolean existsByResponsableAndEstado(Usuario usuario, EstadoCaja estado);

    Optional<Caja> findByNombreCaja(String nombreCaja);

}
