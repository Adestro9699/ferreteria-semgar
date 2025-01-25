package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Categoria {

    public enum Estado {
        ACTIVO,    // Categoría en uso
        INACTIVO   // Categoría en desuso
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCategoria;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre; //nombre de la categoría

    @Size(max = 300, message = "La descripción no puede tener más de 300 caracteres")
    private String descripcion; //descripcion opcional de la categoría

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private Estado estado; //estado ACTIVO o INACTIVO de la categoría

    public Categoria() {
    }

    public Categoria(Integer idCategoria, String nombre, String descripcion, Estado estado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}