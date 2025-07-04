package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;
import java.time.LocalDate;

public class TrabajadorDTO {
    private Integer idTrabajador;
    private String nombreTrabajador;
    private String apellidoTrabajador;
    private String dniTrabajador;
    private String telefonoTrabajador;
    private String correoTrabajador;
    private String direccionTrabajador;
    private String cargoTrabajador;
    private String estadoTrabajador;
    private LocalDate fechaIngresoTrabajador;
    private LocalDate fechaSalidaTrabajador;
    private SucursalDTO sucursal;
    private Integer idSucursal; // Campo adicional para actualizaciones simples

    public TrabajadorDTO() {}

    public TrabajadorDTO(Trabajador trabajador) {
        this.idTrabajador = trabajador.getIdTrabajador();
        this.nombreTrabajador = trabajador.getNombreTrabajador();
        this.apellidoTrabajador = trabajador.getApellidoTrabajador();
        this.dniTrabajador = trabajador.getDniTrabajador();
        this.telefonoTrabajador = trabajador.getTelefonoTrabajador();
        this.correoTrabajador = trabajador.getCorreoTrabajador();
        this.direccionTrabajador = trabajador.getDireccionTrabajador();
        this.cargoTrabajador = trabajador.getCargoTrabajador();
        this.estadoTrabajador = trabajador.getEstadoTrabajador() != null ? trabajador.getEstadoTrabajador().name() : null;
        this.fechaIngresoTrabajador = trabajador.getFechaIngresoTrabajador();
        this.fechaSalidaTrabajador = trabajador.getFechaSalidaTrabajador();
        if (trabajador.getSucursal() != null) {
            this.sucursal = new SucursalDTO(trabajador.getSucursal(), false); // Sin almacenes para evitar ciclos
        }
    }

    // Getters y setters
    public Integer getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(Integer idTrabajador) { this.idTrabajador = idTrabajador; }
    public String getNombreTrabajador() { return nombreTrabajador; }
    public void setNombreTrabajador(String nombreTrabajador) { this.nombreTrabajador = nombreTrabajador; }
    public String getApellidoTrabajador() { return apellidoTrabajador; }
    public void setApellidoTrabajador(String apellidoTrabajador) { this.apellidoTrabajador = apellidoTrabajador; }
    public String getDniTrabajador() { return dniTrabajador; }
    public void setDniTrabajador(String dniTrabajador) { this.dniTrabajador = dniTrabajador; }
    public String getTelefonoTrabajador() { return telefonoTrabajador; }
    public void setTelefonoTrabajador(String telefonoTrabajador) { this.telefonoTrabajador = telefonoTrabajador; }
    public String getCorreoTrabajador() { return correoTrabajador; }
    public void setCorreoTrabajador(String correoTrabajador) { this.correoTrabajador = correoTrabajador; }
    public String getDireccionTrabajador() { return direccionTrabajador; }
    public void setDireccionTrabajador(String direccionTrabajador) { this.direccionTrabajador = direccionTrabajador; }
    public String getCargoTrabajador() { return cargoTrabajador; }
    public void setCargoTrabajador(String cargoTrabajador) { this.cargoTrabajador = cargoTrabajador; }
    public String getEstadoTrabajador() { return estadoTrabajador; }
    public void setEstadoTrabajador(String estadoTrabajador) { this.estadoTrabajador = estadoTrabajador; }
    public LocalDate getFechaIngresoTrabajador() { return fechaIngresoTrabajador; }
    public void setFechaIngresoTrabajador(LocalDate fechaIngresoTrabajador) { this.fechaIngresoTrabajador = fechaIngresoTrabajador; }
    public LocalDate getFechaSalidaTrabajador() { return fechaSalidaTrabajador; }
    public void setFechaSalidaTrabajador(LocalDate fechaSalidaTrabajador) { this.fechaSalidaTrabajador = fechaSalidaTrabajador; }
    public SucursalDTO getSucursal() { return sucursal; }
    public void setSucursal(SucursalDTO sucursal) { this.sucursal = sucursal; }
    public Integer getIdSucursal() { return idSucursal; }
    public void setIdSucursal(Integer idSucursal) { this.idSucursal = idSucursal; }

    public Trabajador toEntity() {
        Trabajador t = new Trabajador();
        t.setIdTrabajador(this.idTrabajador);
        t.setNombreTrabajador(this.nombreTrabajador);
        t.setApellidoTrabajador(this.apellidoTrabajador);
        t.setDniTrabajador(this.dniTrabajador);
        t.setTelefonoTrabajador(this.telefonoTrabajador);
        t.setCorreoTrabajador(this.correoTrabajador);
        t.setDireccionTrabajador(this.direccionTrabajador);
        t.setCargoTrabajador(this.cargoTrabajador);
        if (this.estadoTrabajador != null) {
            t.setEstadoTrabajador(Trabajador.EstadoTrabajador.valueOf(this.estadoTrabajador));
        }
        t.setFechaIngresoTrabajador(this.fechaIngresoTrabajador);
        t.setFechaSalidaTrabajador(this.fechaSalidaTrabajador);
        
        // Manejar la sucursal: priorizar idSucursal si está presente, sino usar el objeto sucursal
        if (this.idSucursal != null) {
            // Crear una sucursal con solo el ID para la actualización
            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(this.idSucursal);
            t.setSucursal(sucursal);
        } else if (this.sucursal != null) {
            t.setSucursal(this.sucursal.toEntity());
        }
        return t;
    }

    /**
     * Actualiza una entidad Trabajador existente con los datos de este DTO.
     * @param trabajador La entidad a actualizar
     */
    public void updateEntity(Trabajador trabajador) {
        trabajador.setNombreTrabajador(this.nombreTrabajador);
        trabajador.setApellidoTrabajador(this.apellidoTrabajador);
        trabajador.setDniTrabajador(this.dniTrabajador);
        trabajador.setTelefonoTrabajador(this.telefonoTrabajador);
        trabajador.setCorreoTrabajador(this.correoTrabajador);
        trabajador.setDireccionTrabajador(this.direccionTrabajador);
        trabajador.setCargoTrabajador(this.cargoTrabajador);
        if (this.estadoTrabajador != null) {
            trabajador.setEstadoTrabajador(Trabajador.EstadoTrabajador.valueOf(this.estadoTrabajador));
        }
        trabajador.setFechaIngresoTrabajador(this.fechaIngresoTrabajador);
        trabajador.setFechaSalidaTrabajador(this.fechaSalidaTrabajador);
        
        // Manejar la sucursal: priorizar idSucursal si está presente, sino usar el objeto sucursal
        if (this.idSucursal != null) {
            // Crear una sucursal con solo el ID para la actualización
            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(this.idSucursal);
            trabajador.setSucursal(sucursal);
        } else if (this.sucursal != null) {
            trabajador.setSucursal(this.sucursal.toEntity());
        }
    }
} 