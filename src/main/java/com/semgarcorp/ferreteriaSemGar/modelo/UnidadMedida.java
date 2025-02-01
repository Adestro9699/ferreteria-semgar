package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idUnidadMedida;

    @NotNull(message = "El nombre de la unidad no puede ser nulo")
    @Size(min = 3, max = 150, message = "El nombre de la unidad debe tener entre 3 y 150 caracteres")
    private String nombreUnidad;

    @NotNull(message = "La abreviatura no puede ser nula")
    @Size(min = 1, max = 10, message = "La abreviatura debe tener entre 1 y 10 caracteres")
    private String abreviatura;

    @Size(max = 255, message = "La descripción no puede exceder los 255 caracteres")
    @Column(length = 255)
    private String descripcion;

    public UnidadMedida() {
    }

    public UnidadMedida(Integer idUnidadMedida, String nombreUnidad, String abreviatura, String descripcion) {
        this.idUnidadMedida = idUnidadMedida;
        this.nombreUnidad = nombreUnidad;
        this.abreviatura = abreviatura;
        this.descripcion = descripcion;
    }

    public Integer getIdUnidadMedida() {
        return idUnidadMedida;
    }

    public void setIdUnidadMedida(Integer idUnidadMedida) {
        this.idUnidadMedida = idUnidadMedida;
    }

    public String getNombreUnidad() {
        return nombreUnidad;
    }

    public void setNombreUnidad(String nombreUnidad) {
        this.nombreUnidad = nombreUnidad;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}