package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Integer> {

    /**
     * Busca un parámetro por su clave.
     *
     * @param clave La clave del parámetro (ej.: "IGV", "DESCUENTO_VENDEDOR").
     * @return Un Optional que contiene el parámetro si se encuentra, o vacío si no.
     */
    Optional<Parametro> findByClave(String clave);
}