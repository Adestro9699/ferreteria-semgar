package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para transferir información de transferencias sin bucles en JSON.
 * Incluye toda la información de la transferencia y sus detalles.
 */
public class TransferenciaDTO {

    private Integer idTransferencia;
    private LocalDate fechaTransferencia;
    private String observaciones;
    private String estadoTransferencia;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
    
    // Información del almacén origen
    private Integer idAlmacenOrigen;
    private String nombreAlmacenOrigen;
    private String ubicacionAlmacenOrigen;
    private Integer idSucursalOrigen;
    private String nombreSucursalOrigen;
    
    // Información del almacén destino
    private Integer idAlmacenDestino;
    private String nombreAlmacenDestino;
    private String ubicacionAlmacenDestino;
    private Integer idSucursalDestino;
    private String nombreSucursalDestino;
    
    // Información del trabajador origen
    private Integer idTrabajadorOrigen;
    private String nombreTrabajadorOrigen;
    private String apellidoTrabajadorOrigen;
    private String dniTrabajadorOrigen;
    
    // Información del trabajador destino
    private Integer idTrabajadorDestino;
    private String nombreTrabajadorDestino;
    private String apellidoTrabajadorDestino;
    private String dniTrabajadorDestino;
    
    // Lista de detalles de la transferencia
    private List<DetalleTransferenciaDTO> detalles;

    public TransferenciaDTO() {
    }

    public TransferenciaDTO(Transferencia transferencia) {
        this.idTransferencia = transferencia.getIdTransferencia();
        this.fechaTransferencia = transferencia.getFechaTransferencia();
        this.observaciones = transferencia.getObservaciones();
        this.estadoTransferencia = transferencia.getEstadoTransferencia() != null ? 
            transferencia.getEstadoTransferencia().name() : null;
        this.fechaCreacion = transferencia.getFechaCreacion();
        this.fechaModificacion = transferencia.getFechaModificacion();
        
        // Información del almacén origen
        if (transferencia.getAlmacenOrigen() != null) {
            this.idAlmacenOrigen = transferencia.getAlmacenOrigen().getIdAlmacen();
            this.nombreAlmacenOrigen = transferencia.getAlmacenOrigen().getNombre();
            this.ubicacionAlmacenOrigen = transferencia.getAlmacenOrigen().getUbicacion();
            
            if (transferencia.getAlmacenOrigen().getSucursal() != null) {
                this.idSucursalOrigen = transferencia.getAlmacenOrigen().getSucursal().getIdSucursal();
                this.nombreSucursalOrigen = transferencia.getAlmacenOrigen().getSucursal().getNombre();
            }
        }
        
        // Información del almacén destino
        if (transferencia.getAlmacenDestino() != null) {
            this.idAlmacenDestino = transferencia.getAlmacenDestino().getIdAlmacen();
            this.nombreAlmacenDestino = transferencia.getAlmacenDestino().getNombre();
            this.ubicacionAlmacenDestino = transferencia.getAlmacenDestino().getUbicacion();
            
            if (transferencia.getAlmacenDestino().getSucursal() != null) {
                this.idSucursalDestino = transferencia.getAlmacenDestino().getSucursal().getIdSucursal();
                this.nombreSucursalDestino = transferencia.getAlmacenDestino().getSucursal().getNombre();
            }
        }
        
        // Información del trabajador origen
        if (transferencia.getTrabajadorOrigen() != null) {
            this.idTrabajadorOrigen = transferencia.getTrabajadorOrigen().getIdTrabajador();
            this.nombreTrabajadorOrigen = transferencia.getTrabajadorOrigen().getNombreTrabajador();
            this.apellidoTrabajadorOrigen = transferencia.getTrabajadorOrigen().getApellidoTrabajador();
            this.dniTrabajadorOrigen = transferencia.getTrabajadorOrigen().getDniTrabajador();
        }
        
        // Información del trabajador destino
        if (transferencia.getTrabajadorDestino() != null) {
            this.idTrabajadorDestino = transferencia.getTrabajadorDestino().getIdTrabajador();
            this.nombreTrabajadorDestino = transferencia.getTrabajadorDestino().getNombreTrabajador();
            this.apellidoTrabajadorDestino = transferencia.getTrabajadorDestino().getApellidoTrabajador();
            this.dniTrabajadorDestino = transferencia.getTrabajadorDestino().getDniTrabajador();
        }
        
        // Convertir detalles a DTOs
        if (transferencia.getDetalles() != null) {
            this.detalles = transferencia.getDetalles().stream()
                    .map(DetalleTransferenciaDTO::new)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Convierte este DTO en una entidad Transferencia
     */
    public Transferencia toEntity() {
        Transferencia transferencia = new Transferencia();
        transferencia.setIdTransferencia(this.idTransferencia);
        transferencia.setFechaTransferencia(this.fechaTransferencia);
        transferencia.setObservaciones(this.observaciones);
        if (this.estadoTransferencia != null) {
            transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.valueOf(this.estadoTransferencia));
        }
        transferencia.setFechaCreacion(this.fechaCreacion);
        transferencia.setFechaModificacion(this.fechaModificacion);
        
        // Crear almacén origen por ID
        if (this.idAlmacenOrigen != null) {
            var almacenOrigen = new com.semgarcorp.ferreteriaSemGar.modelo.Almacen();
            almacenOrigen.setIdAlmacen(this.idAlmacenOrigen);
            transferencia.setAlmacenOrigen(almacenOrigen);
        }
        
        // Crear almacén destino por ID
        if (this.idAlmacenDestino != null) {
            var almacenDestino = new com.semgarcorp.ferreteriaSemGar.modelo.Almacen();
            almacenDestino.setIdAlmacen(this.idAlmacenDestino);
            transferencia.setAlmacenDestino(almacenDestino);
        }
        
        // Crear trabajador origen por ID
        if (this.idTrabajadorOrigen != null) {
            var trabajadorOrigen = new com.semgarcorp.ferreteriaSemGar.modelo.Trabajador();
            trabajadorOrigen.setIdTrabajador(this.idTrabajadorOrigen);
            transferencia.setTrabajadorOrigen(trabajadorOrigen);
        }
        
        // Crear trabajador destino por ID
        if (this.idTrabajadorDestino != null) {
            var trabajadorDestino = new com.semgarcorp.ferreteriaSemGar.modelo.Trabajador();
            trabajadorDestino.setIdTrabajador(this.idTrabajadorDestino);
            transferencia.setTrabajadorDestino(trabajadorDestino);
        }
        
        // Convertir detalles
        if (this.detalles != null) {
            List<DetalleTransferencia> detallesEntidad = this.detalles.stream()
                    .map(dto -> {
                        DetalleTransferencia detalle = dto.toEntity();
                        detalle.setTransferencia(transferencia);
                        return detalle;
                    })
                    .collect(Collectors.toList());
            transferencia.setDetalles(detallesEntidad);
        }
        
        return transferencia;
    }

    // Getters y Setters
    public Integer getIdTransferencia() {
        return idTransferencia;
    }

    public void setIdTransferencia(Integer idTransferencia) {
        this.idTransferencia = idTransferencia;
    }

    public LocalDate getFechaTransferencia() {
        return fechaTransferencia;
    }

    public void setFechaTransferencia(LocalDate fechaTransferencia) {
        this.fechaTransferencia = fechaTransferencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstadoTransferencia() {
        return estadoTransferencia;
    }

    public void setEstadoTransferencia(String estadoTransferencia) {
        this.estadoTransferencia = estadoTransferencia;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Integer getIdAlmacenOrigen() {
        return idAlmacenOrigen;
    }

    public void setIdAlmacenOrigen(Integer idAlmacenOrigen) {
        this.idAlmacenOrigen = idAlmacenOrigen;
    }

    public String getNombreAlmacenOrigen() {
        return nombreAlmacenOrigen;
    }

    public void setNombreAlmacenOrigen(String nombreAlmacenOrigen) {
        this.nombreAlmacenOrigen = nombreAlmacenOrigen;
    }

    public String getUbicacionAlmacenOrigen() {
        return ubicacionAlmacenOrigen;
    }

    public void setUbicacionAlmacenOrigen(String ubicacionAlmacenOrigen) {
        this.ubicacionAlmacenOrigen = ubicacionAlmacenOrigen;
    }

    public Integer getIdSucursalOrigen() {
        return idSucursalOrigen;
    }

    public void setIdSucursalOrigen(Integer idSucursalOrigen) {
        this.idSucursalOrigen = idSucursalOrigen;
    }

    public String getNombreSucursalOrigen() {
        return nombreSucursalOrigen;
    }

    public void setNombreSucursalOrigen(String nombreSucursalOrigen) {
        this.nombreSucursalOrigen = nombreSucursalOrigen;
    }

    public Integer getIdAlmacenDestino() {
        return idAlmacenDestino;
    }

    public void setIdAlmacenDestino(Integer idAlmacenDestino) {
        this.idAlmacenDestino = idAlmacenDestino;
    }

    public String getNombreAlmacenDestino() {
        return nombreAlmacenDestino;
    }

    public void setNombreAlmacenDestino(String nombreAlmacenDestino) {
        this.nombreAlmacenDestino = nombreAlmacenDestino;
    }

    public String getUbicacionAlmacenDestino() {
        return ubicacionAlmacenDestino;
    }

    public void setUbicacionAlmacenDestino(String ubicacionAlmacenDestino) {
        this.ubicacionAlmacenDestino = ubicacionAlmacenDestino;
    }

    public Integer getIdSucursalDestino() {
        return idSucursalDestino;
    }

    public void setIdSucursalDestino(Integer idSucursalDestino) {
        this.idSucursalDestino = idSucursalDestino;
    }

    public String getNombreSucursalDestino() {
        return nombreSucursalDestino;
    }

    public void setNombreSucursalDestino(String nombreSucursalDestino) {
        this.nombreSucursalDestino = nombreSucursalDestino;
    }

    public Integer getIdTrabajadorOrigen() {
        return idTrabajadorOrigen;
    }

    public void setIdTrabajadorOrigen(Integer idTrabajadorOrigen) {
        this.idTrabajadorOrigen = idTrabajadorOrigen;
    }

    public String getNombreTrabajadorOrigen() {
        return nombreTrabajadorOrigen;
    }

    public void setNombreTrabajadorOrigen(String nombreTrabajadorOrigen) {
        this.nombreTrabajadorOrigen = nombreTrabajadorOrigen;
    }

    public String getApellidoTrabajadorOrigen() {
        return apellidoTrabajadorOrigen;
    }

    public void setApellidoTrabajadorOrigen(String apellidoTrabajadorOrigen) {
        this.apellidoTrabajadorOrigen = apellidoTrabajadorOrigen;
    }

    public String getDniTrabajadorOrigen() {
        return dniTrabajadorOrigen;
    }

    public void setDniTrabajadorOrigen(String dniTrabajadorOrigen) {
        this.dniTrabajadorOrigen = dniTrabajadorOrigen;
    }

    public Integer getIdTrabajadorDestino() {
        return idTrabajadorDestino;
    }

    public void setIdTrabajadorDestino(Integer idTrabajadorDestino) {
        this.idTrabajadorDestino = idTrabajadorDestino;
    }

    public String getNombreTrabajadorDestino() {
        return nombreTrabajadorDestino;
    }

    public void setNombreTrabajadorDestino(String nombreTrabajadorDestino) {
        this.nombreTrabajadorDestino = nombreTrabajadorDestino;
    }

    public String getApellidoTrabajadorDestino() {
        return apellidoTrabajadorDestino;
    }

    public void setApellidoTrabajadorDestino(String apellidoTrabajadorDestino) {
        this.apellidoTrabajadorDestino = apellidoTrabajadorDestino;
    }

    public String getDniTrabajadorDestino() {
        return dniTrabajadorDestino;
    }

    public void setDniTrabajadorDestino(String dniTrabajadorDestino) {
        this.dniTrabajadorDestino = dniTrabajadorDestino;
    }

    public List<DetalleTransferenciaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleTransferenciaDTO> detalles) {
        this.detalles = detalles;
    }
} 