package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.dto.CotizacionDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.CotizacionResumenDTO;
import com.semgarcorp.ferreteriaSemGar.dto.DetalleCotizacionDTO;
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

    @Query("""
        SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.CotizacionDetalleCompletoDTO(
            c.idCotizacion,
            COALESCE(tp.nombre, 'N/A'),
            c.totalCotizacion,
            c.fechaCotizacion,
            COALESCE(cli.nombres, ''),
            COALESCE(cli.apellidos, ''),
            COALESCE(cli.razonSocial, ''),
            COALESCE(emp.razonSocial, 'N/A'),
            COALESCE(trab.nombreTrabajador, ''),
            COALESCE(trab.apellidoTrabajador, ''),
            null
        )
        FROM Cotizacion c
        LEFT JOIN c.cliente cli
        LEFT JOIN c.empresa emp
        LEFT JOIN c.tipoPago tp
        LEFT JOIN c.trabajador trab
        WHERE c.idCotizacion = :idCotizacion
    """)
    Optional<CotizacionDetalleCompletoDTO> findCotizacionDetalleCompletoById(Integer idCotizacion);

    @Query("""
        SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.DetalleCotizacionDTO(
            d.idDetalleCotizacion,
            d.cotizacion.idCotizacion,
            d.producto.idProducto,
            p.nombreProducto,
            um.nombreUnidad,
            d.cantidad,
            d.precioUnitario,
            d.descuento,
            d.subtotal,
            d.subtotalSinIGV,
            d.igvAplicado
        )
        FROM DetalleCotizacion d
        JOIN d.producto p
        JOIN p.unidadMedida um
        WHERE d.cotizacion.idCotizacion = :idCotizacion
    """)
    List<DetalleCotizacionDTO> findDetallesByCotizacionId(Integer idCotizacion);
}