package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {

    Optional<TipoDocumento> findByCodigoNubeFact(Integer codigoNubeFact);

}