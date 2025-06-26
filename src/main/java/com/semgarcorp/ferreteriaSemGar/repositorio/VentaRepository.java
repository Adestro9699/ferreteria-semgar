package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import com.semgarcorp.ferreteriaSemGar.modelo.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("SELECT MAX(CAST(v.numeroComprobante AS int)) FROM Venta v WHERE v.serieComprobante = :serie AND v.tipoComprobantePago.codigoNubefact = :codigoNubefact")
    Optional<Integer> findMaxNumeroBySerieAndTipo(
            @Param("serie") String serie,
            @Param("codigoNubefact") Integer codigoNubefact);

    //consulta JPQL para poder filtrar por un estado
    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO(" +
            "v.idVenta, v.serieComprobante, v.numeroComprobante, " +
            "v.tipoComprobantePago.nombre, " + // Nombre del tipo de comprobante
            "v.tipoPago.nombre, " + // Nombre del tipo de pago
            "v.totalVenta, v.fechaVenta, v.estadoVenta, " +
            "c.nombres, c.apellidos, c.razonSocial, e.razonSocial) " +
            "FROM Venta v " +
            "LEFT JOIN v.cliente c " +
            "LEFT JOIN v.empresa e " + // Asume que existe esta relación en Venta
            "WHERE v.estadoVenta = :estado " +
            "ORDER BY v.fechaVenta DESC")
    Page<VentaResumenDTO> findAllVentasResumen(@Param("estado") EstadoVenta estado, Pageable pageable);

    //consulta JPQL para poder filtrar por varios estados
    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO(" +
            "v.idVenta, v.serieComprobante, v.numeroComprobante, " +
            "v.tipoComprobantePago.nombre, v.tipoPago.nombre, " +
            "v.totalVenta, v.fechaVenta, v.estadoVenta, " +
            "c.nombres, c.apellidos, c.razonSocial, e.razonSocial) " +
            "FROM Venta v " +
            "LEFT JOIN v.cliente c " +
            "LEFT JOIN v.empresa e " +
            "WHERE v.estadoVenta IN :estados " + // Filtra por lista de estados
            "ORDER BY v.fechaVenta DESC")
    Page<VentaResumenDTO> findAllVentasResumenByEstados(
            @Param("estados") List<EstadoVenta> estados,
            Pageable pageable);

    // Consulta para datos principales
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
                    COALESCE(v.valorIgvActual, 0),
                    COALESCE(cli.nombres, ''),
                    COALESCE(cli.apellidos, ''),
                    COALESCE(cli.razonSocial, ''),
                    COALESCE(emp.razonSocial, 'N/A'),
                    COALESCE(emp.ruc, ''),
                    COALESCE(cli.numeroDocumento, ''),
                    COALESCE(td.abreviatura, ''),
                    COALESCE(cli.direccion, ''),
                    CONCAT(trab.apellidoTrabajador, ' ', trab.nombreTrabajador),
                    CONCAT(caj.responsable.trabajador.apellidoTrabajador, ' ', caj.responsable.trabajador.nombreTrabajador)
                )
                FROM Venta v
                LEFT JOIN v.cliente cli
                LEFT JOIN cli.tipoDocumento td
                LEFT JOIN v.empresa emp
                LEFT JOIN v.tipoComprobantePago tcp
                LEFT JOIN v.tipoPago tp
                LEFT JOIN v.trabajador trab
                LEFT JOIN v.caja caj
                WHERE v.idVenta = :idVenta
            """)
    Optional<VentaDetalleCompletoDTO> findVentaDetalleCompletoById(Integer idVenta);

    // Consulta alternativa para ventas PENDIENTES que maneja mejor los valores nulos
    // Usado en VentaService.obtenerVentaDetalleCompleto()
    @Query("""
                SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.VentaDetalleCompletoDTO(
                    v.idVenta,
                    COALESCE(v.serieComprobante, ''),
                    COALESCE(v.numeroComprobante, ''),
                    COALESCE(tcp.nombre, 'N/A'),
                    COALESCE(tp.nombre, 'N/A'),
                    v.totalVenta,
                    v.fechaVenta,
                    v.estadoVenta,
                    v.moneda,
                    COALESCE(v.valorIgvActual, 0),
                    COALESCE(cli.nombres, ''),
                    COALESCE(cli.apellidos, ''),
                    COALESCE(cli.razonSocial, ''),
                    COALESCE(emp.razonSocial, 'N/A'),
                    COALESCE(emp.ruc, ''),
                    COALESCE(cli.numeroDocumento, ''),
                    COALESCE(td.abreviatura, ''),
                    COALESCE(cli.direccion, ''),
                    CASE 
                        WHEN trab IS NOT NULL 
                        THEN CONCAT(COALESCE(trab.apellidoTrabajador, ''), ' ', COALESCE(trab.nombreTrabajador, ''))
                        ELSE 'N/A'
                    END,
                    CASE 
                        WHEN caj IS NOT NULL AND caj.responsable IS NOT NULL AND caj.responsable.trabajador IS NOT NULL 
                        THEN CONCAT(COALESCE(caj.responsable.trabajador.apellidoTrabajador, ''), ' ', COALESCE(caj.responsable.trabajador.nombreTrabajador, ''))
                        ELSE 'N/A'
                    END
                )
                FROM Venta v
                LEFT JOIN v.cliente cli
                LEFT JOIN cli.tipoDocumento td
                LEFT JOIN v.empresa emp
                LEFT JOIN v.tipoComprobantePago tcp
                LEFT JOIN v.tipoPago tp
                LEFT JOIN v.trabajador trab
                LEFT JOIN v.caja caj
                WHERE v.idVenta = :idVenta
            """)
    Optional<VentaDetalleCompletoDTO> findVentaDetalleCompletoByIdOptimized(Integer idVenta);

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
                    d.igvAplicado,
                    p.codigoBarra
                )
                FROM DetalleVenta d
                JOIN d.producto p
                JOIN p.unidadMedida um
                WHERE d.venta.idVenta = :idVenta
            """)
    List<DetalleVentaDTO> findDetallesByVentaId(Integer idVenta);

    // Consulta optimizada para detalles que maneja mejor los valores nulos
    // Usado en VentaService.obtenerVentaDetalleCompleto()
    @Query("""
                SELECT NEW com.semgarcorp.ferreteriaSemGar.dto.DetalleVentaDTO(
                    d.idDetalleVenta,
                    d.venta.idVenta,
                    d.producto.idProducto,
                    COALESCE(p.nombreProducto, 'Producto sin nombre'),
                    COALESCE(um.nombreUnidad, 'Sin unidad'),
                    d.cantidad,
                    d.precioUnitario,
                    d.descuento,
                    d.subtotal,
                    d.subtotalSinIGV,
                    d.igvAplicado,
                    COALESCE(p.codigoBarra, '')
                )
                FROM DetalleVenta d
                JOIN d.producto p
                LEFT JOIN p.unidadMedida um
                WHERE d.venta.idVenta = :idVenta
            """)
    List<DetalleVentaDTO> findDetallesByVentaIdOptimized(Integer idVenta);

    Optional<Venta> findBySerieComprobanteAndNumeroComprobante(String serieComprobante, String numeroComprobante);

    @Query("SELECT v FROM Venta v JOIN v.cliente c WHERE " +
           "LOWER(CONCAT(c.nombres, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Venta> findByNombreCliente(@Param("nombre") String nombre);

    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO(" +
           "v.idVenta, v.serieComprobante, v.numeroComprobante, " +
           "v.tipoComprobantePago.nombre, v.tipoPago.nombre, " +
           "v.totalVenta, v.fechaVenta, v.estadoVenta, " +
           "c.nombres, c.apellidos, c.razonSocial, e.razonSocial) " +
           "FROM Venta v " +
           "LEFT JOIN v.cliente c " +
           "LEFT JOIN v.empresa e " +
           "WHERE v.estadoVenta IN :estados " +
           "AND v.serieComprobante = :serie " +
           "AND v.numeroComprobante = :numero " +
           "ORDER BY v.fechaVenta DESC")
    Page<VentaResumenDTO> findVentasResumenBySerieNumeroAndEstados(
        @Param("serie") String serie,
        @Param("numero") String numero,
        @Param("estados") List<EstadoVenta> estados,
        Pageable pageable
    );

    @Query("SELECT new com.semgarcorp.ferreteriaSemGar.dto.VentaResumenDTO(" +
           "v.idVenta, v.serieComprobante, v.numeroComprobante, " +
           "v.tipoComprobantePago.nombre, v.tipoPago.nombre, " +
           "v.totalVenta, v.fechaVenta, v.estadoVenta, " +
           "c.nombres, c.apellidos, c.razonSocial, e.razonSocial) " +
           "FROM Venta v " +
           "LEFT JOIN v.cliente c " +
           "LEFT JOIN v.empresa e " +
           "LEFT JOIN v.tipoComprobantePago " +
           "LEFT JOIN v.tipoPago " +
           "WHERE v.estadoVenta IN :estados " +
           "AND (LOWER(CONCAT(c.nombres, ' ', c.apellidos)) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "ORDER BY v.fechaVenta DESC")
    Page<VentaResumenDTO> findVentasResumenByNombreClienteAndEstados(
        @Param("nombre") String nombre,
        @Param("estados") List<EstadoVenta> estados,
        Pageable pageable
    );
}
