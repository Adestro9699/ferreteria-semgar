package com.semgarcorp.ferreteriaSemGar.repositorio;


import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("SELECT MAX(CAST(v.numeroComprobante AS int)) FROM Venta v WHERE v.serieComprobante = :serie AND v.tipoComprobantePago.codigoNubefact = :codigoNubefact")
    Optional<Integer> findMaxNumeroBySerieAndTipo(
            @Param("serie") String serie,
            @Param("codigoNubefact") Integer codigoNubefact
    );

    // Consulta optimizada para el listado (solo trae los datos necesarios)
    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO(" +
            "v.idVenta, v.serieComprobante, v.numeroComprobante, " +
            "v.tipoComprobantePago.nombre, " +  // Nombre del tipo de comprobante
            "v.tipoPago.nombre, " +             // Nombre del tipo de pago
            "v.totalVenta, v.fechaVenta, v.estadoVenta, " +
            "c.nombres, c.apellidos, c.razonSocial, e.razonSocial) " +
            "FROM Venta v " +
            "LEFT JOIN v.cliente c " +
            "LEFT JOIN v.empresa e " +  // Asume que existe esta relación en Venta
            "ORDER BY v.fechaVenta DESC")
    List<VentaResumenDTO> findAllVentasResumen();

    //Consulta para datos principales
    @Query("""
    SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO(
        v.idVenta,
        v.serieComprobante,
        v.numeroComprobante,
        COALESCE(tcp.nombre, 'N/A'),
        COALESCE(tp.nombre, 'N/A'),
        v.totalVenta,
        v.fechaVenta,
        v.estadoVenta,
        v.moneda,
        COALESCE(cli.nombres, ''),
        COALESCE(cli.apellidos, ''),
        COALESCE(cli.razonSocial, ''),
        COALESCE(emp.razonSocial, 'N/A')
    )
    FROM Venta v
    LEFT JOIN v.cliente cli
    LEFT JOIN v.empresa emp
    LEFT JOIN v.tipoComprobantePago tcp
    LEFT JOIN v.tipoPago tp
    WHERE v.idVenta = :idVenta
""")
    Optional<VentaDetalleCompletoDTO> findVentaDetalleCompletoById(Integer idVenta);

    // Consulta para detalles
    @Query("""
    SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO(
        d.idDetalleVenta, 
        d.venta.idVenta, 
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
    FROM DetalleVenta d
    JOIN d.producto p
    JOIN p.unidadMedida um  
    WHERE d.venta.idVenta = :idVenta
""")
    List<DetalleVentaDTO> findDetallesByVentaId(Integer idVenta);
}
