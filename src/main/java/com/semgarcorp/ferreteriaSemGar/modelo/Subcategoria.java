package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Subcategoria {

    public enum Estado {
        ACTIVO,    // Subcategoría en uso
        INACTIVO   // Subcategoría en desuso
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idSubcategoria;

    @NotNull(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre; //nombre de la subcategoría

    @Size(max = 300, message = "La descripción no puede tener más de 300 caracteres")
    private String descripcion; //descripción opcional de la subcategoría

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private Estado estado; //estado ACTIVO o INACTIVO de la subcategoría

    public Subcategoria() {
    }

    public Subcategoria(Integer idSubcategoria, String nombre, String descripcion, Estado estado) {
        this.idSubcategoria = idSubcategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Integer getIdSubcategoria() {
        return idSubcategoria;
    }

    public void setIdSubcategoria(Integer idSubcategoria) {
        this.idSubcategoria = idSubcategoria;
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
