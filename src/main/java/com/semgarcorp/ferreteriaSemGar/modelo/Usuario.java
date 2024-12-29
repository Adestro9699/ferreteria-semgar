package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Usuario {

    public enum EstadoUsuario {
        ACTIVO,
        INACTIVO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUsuario;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 3, max = 100, message = "El nombre de usuario debe tener entre 3 y 100 caracteres")
    @Column(length = 100)
    private String nombreUsuario;

    @Email(message = "El correo debe ser válido")
    @NotNull(message = "El correo no puede ser nulo")
    @Column(name = "correo_usuario", unique = true, length = 100) // Conservado en snake_case
    private String correoUsuario;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(length = 255)
    private String contrasena;

    @Enumerated(EnumType.STRING) // Relaciona el enum con esta columna
    @Column(length = 10)
    private EstadoUsuario estadoUsuario;

    @NotNull(message = "La fecha de creación no puede ser nula")
    private LocalDate fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "idTrabajador")
    private Trabajador trabajador;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String nombreUsuario, String correoUsuario, String contrasena, EstadoUsuario estadoUsuario,
                   LocalDate fechaCreacion, Trabajador trabajador) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.contrasena = contrasena;
        this.estadoUsuario = estadoUsuario;
        this.fechaCreacion = fechaCreacion;
        this.trabajador = trabajador;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public EstadoUsuario getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuario estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }
}