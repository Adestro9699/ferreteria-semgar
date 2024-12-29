package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Entity
public class Trabajador {

    public enum EstadoTrabajador {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTrabajador;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(length = 100) // Definido como varchar(100)
    private String nombreTrabajador;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Column(length = 100) // Definido como varchar(100)
    private String apellidoTrabajador;

    @Column(unique = true, length = 8)
    @NotBlank(message = "El DNI no puede estar vacío")
    private String dniTrabajador;

    @Column(length = 15) // Definido como varchar(15)
    private String telefonoTrabajador;

    @Column(unique = true, length = 100)
    @Email(message = "El correo debe tener un formato válido")
    private String correoTrabajador;

    @Column(length = 200) // Definido como varchar(200)
    private String direccionTrabajador;

    @Column(length = 50) // Definido como varchar(50)
    private String cargoTrabajador;

    @Enumerated(EnumType.STRING) // Relaciona el enum con esta columna
    private EstadoTrabajador estadoTrabajador;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngresoTrabajador;

    private LocalDate fechaSalidaTrabajador;

    public Trabajador() {
    }

    public Trabajador(Integer idTrabajador, String nombreTrabajador, String apellidoTrabajador, String dniTrabajador,
                      String telefonoTrabajador, String correoTrabajador, String direccionTrabajador,
                      String cargoTrabajador, EstadoTrabajador estadoTrabajador, LocalDate fechaIngresoTrabajador,
                      LocalDate fechaSalidaTrabajador) {
        this.idTrabajador = idTrabajador;
        this.nombreTrabajador = nombreTrabajador;
        this.apellidoTrabajador = apellidoTrabajador;
        this.dniTrabajador = dniTrabajador;
        this.telefonoTrabajador = telefonoTrabajador;
        this.correoTrabajador = correoTrabajador;
        this.direccionTrabajador = direccionTrabajador;
        this.cargoTrabajador = cargoTrabajador;
        this.estadoTrabajador = estadoTrabajador;
        this.fechaIngresoTrabajador = fechaIngresoTrabajador;
        this.fechaSalidaTrabajador = fechaSalidaTrabajador;
    }

    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(Integer idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombreTrabajador() {
        return nombreTrabajador;
    }

    public void setNombreTrabajador(String nombreTrabajador) {
        this.nombreTrabajador = nombreTrabajador;
    }

    public String getApellidoTrabajador() {
        return apellidoTrabajador;
    }

    public void setApellidoTrabajador(String apellidoTrabajador) {
        this.apellidoTrabajador = apellidoTrabajador;
    }

    public String getDniTrabajador() {
        return dniTrabajador;
    }

    public void setDniTrabajador(String dniTrabajador) {
        this.dniTrabajador = dniTrabajador;
    }

    public String getTelefonoTrabajador() {
        return telefonoTrabajador;
    }

    public void setTelefonoTrabajador(String telefonoTrabajador) {
        this.telefonoTrabajador = telefonoTrabajador;
    }

    public String getCorreoTrabajador() {
        return correoTrabajador;
    }

    public void setCorreoTrabajador(String correoTrabajador) {
        this.correoTrabajador = correoTrabajador;
    }

    public String getDireccionTrabajador() {
        return direccionTrabajador;
    }

    public void setDireccionTrabajador(String direccionTrabajador) {
        this.direccionTrabajador = direccionTrabajador;
    }

    public String getCargoTrabajador() {
        return cargoTrabajador;
    }

    public void setCargoTrabajador(String cargoTrabajador) {
        this.cargoTrabajador = cargoTrabajador;
    }

    public EstadoTrabajador getEstadoTrabajador() {
        return estadoTrabajador;
    }

    public void setEstadoTrabajador(EstadoTrabajador estadoTrabajador) {
        this.estadoTrabajador = estadoTrabajador;
    }

    public LocalDate getFechaIngresoTrabajador() {
        return fechaIngresoTrabajador;
    }

    public void setFechaIngresoTrabajador(LocalDate fechaIngresoTrabajador) {
        this.fechaIngresoTrabajador = fechaIngresoTrabajador;
    }

    public LocalDate getFechaSalidaTrabajador() {
        return fechaSalidaTrabajador;
    }

    public void setFechaSalidaTrabajador(LocalDate fechaSalidaTrabajador) {
        this.fechaSalidaTrabajador = fechaSalidaTrabajador;
    }
}
