package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Proveedor {

    // Enum para el estado del proveedor
    public enum Estado {
        ACTIVO,    // Proveedor activo
        INACTIVO   // Proveedor inactivo
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String nombre;

    @NotNull(message = "La dirección no puede estar vacía")
    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String direccion;

    @NotNull(message = "El teléfono no puede estar vacío")
    @Size(max = 50, message = "El teléfono no puede tener más de 50 caracteres")
    private String telefono;

    @NotNull(message = "El correo no puede estar vacío")
    @Size(max = 255, message = "El correo no puede tener más de 255 caracteres")
    private String correoProveedor;

    @NotNull(message = "El país no puede estar vacío")
    @Size(max = 100, message = "El país no puede tener más de 100 caracteres")
    private String pais;

    @NotNull(message = "La fecha de registro no puede estar vacía")
    private LocalDate fechaRegistro;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Proveedor() {
    }

    public Proveedor(Integer idProveedor, String nombre, String direccion, String telefono, String correoProveedor,
                     String pais, LocalDate fechaRegistro, Estado estado) {
        this.idProveedor = idProveedor;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.correoProveedor = correoProveedor;
        this.pais = pais;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
    }

    // Getters y Setters

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
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

    public String getCorreoProveedor() {
        return correoProveedor;
    }

    public void setCorreoProveedor(String correoProveedor) {
        this.correoProveedor = correoProveedor;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
