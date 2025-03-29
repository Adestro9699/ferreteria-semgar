package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer> {

    Optional<Parametro> findByClave(String clave);

    @Transactional
    @Modifying
    @Query("DELETE FROM Parametro p WHERE p.clave = :clave")
    void deleteByClave(@Param("clave") String clave);
}
