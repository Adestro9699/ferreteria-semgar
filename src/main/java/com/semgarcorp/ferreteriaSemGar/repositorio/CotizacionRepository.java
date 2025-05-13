package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.dto.CotizacionResumenDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {

    Optional<Cotizacion> findByCodigoCotizacion(String codigoCotizacion);

    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.CotizacionResumenDTO(" +
            "c.idCotizacion, " +
            "c.codigoCotizacion, " +
            "c.fechaCotizacion, " +
            "c.totalCotizacion, " +
            "cli.nombres, " +
            "cli.apellidos, " +
            "cli.razonSocial, " +
            "emp.razonSocial) " +
            "FROM Cotizacion c " +
            "LEFT JOIN c.cliente cli " +
            "LEFT JOIN c.empresa emp " +
            "ORDER BY c.fechaCotizacion DESC")
    List<CotizacionResumenDTO> findAllCotizacionesResumen();
}