package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;

import java.time.LocalDate;

public class AlmacenDTO {
    
    private Integer idAlmacen;
    private String nombre;
    private String ubicacion;
    private String telefono;
    private Boolean esPrincipal;
    private String estadoAlmacen;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
    private String observaciones;
    
    // Información de la sucursal
    private Integer idSucursal;
    private String nombreSucursal;
    private String direccionSucursal;
    private String telefonoSucursal;
    private String estadoSucursal;
    
    public AlmacenDTO() {
    }
    
    public AlmacenDTO(Almacen almacen) {
        this.idAlmacen = almacen.getIdAlmacen();
        this.nombre = almacen.getNombre();
        this.ubicacion = almacen.getUbicacion();
        this.telefono = almacen.getTelefono();
        this.esPrincipal = almacen.getEsPrincipal();
        this.estadoAlmacen = almacen.getEstadoAlmacen().name();
        this.fechaCreacion = almacen.getFechaCreacion();
        this.fechaModificacion = almacen.getFechaModificacion();
        this.observaciones = almacen.getObservaciones();
        
        // Información de la sucursal
        Sucursal sucursal = almacen.getSucursal();
        if (sucursal != null) {
            this.idSucursal = sucursal.getIdSucursal();
            this.nombreSucursal = sucursal.getNombre();
            this.direccionSucursal = sucursal.getDireccion();
            this.telefonoSucursal = sucursal.getTelefono();
            this.estadoSucursal = sucursal.getEstadoSucursal().name();
        }
    }
    
    /**
     * Convierte el DTO a una entidad Almacen
     * @return Almacen creado desde el DTO
     */
    public Almacen toEntity() {
        Almacen almacen = new Almacen();
        almacen.setIdAlmacen(this.idAlmacen);
        almacen.setNombre(this.nombre);
        almacen.setUbicacion(this.ubicacion);
        almacen.setTelefono(this.telefono);
        almacen.setEsPrincipal(this.esPrincipal);
        
        if (this.estadoAlmacen != null) {
            almacen.setEstadoAlmacen(Almacen.EstadoAlmacen.valueOf(this.estadoAlmacen));
        }
        
        almacen.setFechaCreacion(this.fechaCreacion);
        almacen.setFechaModificacion(this.fechaModificacion);
        almacen.setObservaciones(this.observaciones);
        
        // Crear la sucursal si se proporciona el ID
        if (this.idSucursal != null) {
            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(this.idSucursal);
            almacen.setSucursal(sucursal);
        }
        
        return almacen;
    }
    
    /**
     * Actualiza una entidad Almacen existente con los datos del DTO
     * @param almacen La entidad a actualizar
     */
    public void updateEntity(Almacen almacen) {
        almacen.setNombre(this.nombre);
        almacen.setUbicacion(this.ubicacion);
        almacen.setTelefono(this.telefono);
        almacen.setEsPrincipal(this.esPrincipal);
        
        if (this.estadoAlmacen != null) {
            almacen.setEstadoAlmacen(Almacen.EstadoAlmacen.valueOf(this.estadoAlmacen));
        }
        
        almacen.setFechaModificacion(this.fechaModificacion);
        almacen.setObservaciones(this.observaciones);
        
        // Actualizar la sucursal si se proporciona el ID
        if (this.idSucursal != null) {
            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(this.idSucursal);
            almacen.setSucursal(sucursal);
        }
    }
    
    // Getters y setters
    public Integer getIdAlmacen() {
        return idAlmacen;
    }
    
    public void setIdAlmacen(Integer idAlmacen) {
        this.idAlmacen = idAlmacen;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getUbicacion() {
        return ubicacion;
    }
    
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public Boolean getEsPrincipal() {
        return esPrincipal;
    }
    
    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
    
    public String getEstadoAlmacen() {
        return estadoAlmacen;
    }
    
    public void setEstadoAlmacen(String estadoAlmacen) {
        this.estadoAlmacen = estadoAlmacen;
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
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Integer getIdSucursal() {
        return idSucursal;
    }
    
    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }
    
    public String getNombreSucursal() {
        return nombreSucursal;
    }
    
    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }
    
    public String getDireccionSucursal() {
        return direccionSucursal;
    }
    
    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }
    
    public String getTelefonoSucursal() {
        return telefonoSucursal;
    }
    
    public void setTelefonoSucursal(String telefonoSucursal) {
        this.telefonoSucursal = telefonoSucursal;
    }
    
    public String getEstadoSucursal() {
        return estadoSucursal;
    }
    
    public void setEstadoSucursal(String estadoSucursal) {
        this.estadoSucursal = estadoSucursal;
    }
} 