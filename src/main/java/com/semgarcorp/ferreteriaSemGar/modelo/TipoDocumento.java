package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigoSunat")  // Asegura que no haya códigos duplicados
})
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoDocumento;

    @Column(length = 50, nullable = false)
    private String nombre; // Ejemplo: "Documento nacional de Identidad", "Registro único de contribuyente", "Carné de Extranjería"

    @Column(length = 1, nullable = false, unique = true)
    private Integer codigoNubeFact;  //RUC:6, DNI:1, CARNÉ DE EXTRANJERÍA:4, PASAPORTE:7

    @Column(length = 10)
    private String abreviatura;  // Ej: "DNI", "RUC", "CE"

    public TipoDocumento() {
    }

    public TipoDocumento(Integer idTipoDocumento, String nombre, Integer codigoNubeFact, String abreviatura) {
        this.idTipoDocumento = idTipoDocumento;
        this.nombre = nombre;
        this.codigoNubeFact = codigoNubeFact;
        this.abreviatura = abreviatura;
    }

    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCodigoNubeFact() {
        return codigoNubeFact;
    }

    public void setCodigoNubeFact(Integer codigoNubeFact) {
        this.codigoNubeFact = codigoNubeFact;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }
}
