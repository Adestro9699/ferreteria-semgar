package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SucursalDTO {
    
    private Integer idSucursal;
    private String nombre;
    private String direccion;
    private String telefono;
    private String estadoSucursal;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
    private String observaciones;
    
    // Lista de almacenes de la sucursal
    private List<AlmacenDTO> almacenes;
    
    public SucursalDTO() {
    }
    
    public SucursalDTO(Sucursal sucursal) {
        this.idSucursal = sucursal.getIdSucursal();
        this.nombre = sucursal.getNombre();
        this.direccion = sucursal.getDireccion();
        this.telefono = sucursal.getTelefono();
        this.estadoSucursal = sucursal.getEstadoSucursal() != null ? sucursal.getEstadoSucursal().name() : null;
        this.fechaCreacion = sucursal.getFechaCreacion();
        this.fechaModificacion = sucursal.getFechaModificacion();
        this.observaciones = sucursal.getObservaciones();
        
        // Convertir almacenes a DTOs
        if (sucursal.getAlmacenes() != null) {
            this.almacenes = sucursal.getAlmacenes().stream()
                    .map(AlmacenDTO::new)
                    .collect(Collectors.toList());
        }
    }
    
    // Constructor sin almacenes (para evitar referencias circulares)
    public SucursalDTO(Sucursal sucursal, boolean incluirAlmacenes) {
        this.idSucursal = sucursal.getIdSucursal();
        this.nombre = sucursal.getNombre();
        this.direccion = sucursal.getDireccion();
        this.telefono = sucursal.getTelefono();
        this.estadoSucursal = sucursal.getEstadoSucursal() != null ? sucursal.getEstadoSucursal().name() : null;
        this.fechaCreacion = sucursal.getFechaCreacion();
        this.fechaModificacion = sucursal.getFechaModificacion();
        this.observaciones = sucursal.getObservaciones();
        
        if (incluirAlmacenes && sucursal.getAlmacenes() != null) {
            this.almacenes = sucursal.getAlmacenes().stream()
                    .map(AlmacenDTO::new)
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * Convierte el DTO a una entidad Sucursal
     * @return Sucursal creada desde el DTO
     */
    public Sucursal toEntity() {
        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(this.idSucursal);
        sucursal.setNombre(this.nombre);
        sucursal.setDireccion(this.direccion);
        sucursal.setTelefono(this.telefono);
        
        if (this.estadoSucursal != null) {
            sucursal.setEstadoSucursal(Sucursal.EstadoSucursal.valueOf(this.estadoSucursal));
        }
        
        sucursal.setFechaCreacion(this.fechaCreacion);
        sucursal.setFechaModificacion(this.fechaModificacion);
        sucursal.setObservaciones(this.observaciones);
        
        return sucursal;
    }
    
    /**
     * Actualiza una entidad Sucursal existente con los datos del DTO
     * @param sucursal La entidad a actualizar
     */
    public void updateEntity(Sucursal sucursal) {
        sucursal.setNombre(this.nombre);
        sucursal.setDireccion(this.direccion);
        sucursal.setTelefono(this.telefono);
        
        if (this.estadoSucursal != null) {
            sucursal.setEstadoSucursal(Sucursal.EstadoSucursal.valueOf(this.estadoSucursal));
        }
        
        sucursal.setFechaModificacion(this.fechaModificacion);
        sucursal.setObservaciones(this.observaciones);
    }
    
    // Getters y setters
    public Integer getIdSucursal() {
        return idSucursal;
    }
    
    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEstadoSucursal() {
        return estadoSucursal;
    }
    
    public void setEstadoSucursal(String estadoSucursal) {
        this.estadoSucursal = estadoSucursal;
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
    
    public List<AlmacenDTO> getAlmacenes() {
        return almacenes;
    }
    
    public void setAlmacenes(List<AlmacenDTO> almacenes) {
        this.almacenes = almacenes;
    }
} 