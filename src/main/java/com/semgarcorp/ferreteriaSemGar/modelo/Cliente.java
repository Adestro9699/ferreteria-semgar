package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Cliente {

    public enum TipoCliente { //en POSTMAN usar una String
        NATURAL, //0
        JURIDICA //1
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idCliente;

    private TipoCliente tipoCliente;

    private String nombreCompletoCliente;

    private String razonSocial;

    @Column(unique = true)
    private String ruc;

    //@NotBlank(message = "El dni no puede estar vacío")
    @Column(unique = true)
    private String dniCliente;

    private String direccionCliente;

    private String telefonoCliente;

    @Column(unique = true)
    @Email(message = "El correo debe tener un formato válido")
    private String correoCliente;

    public Cliente() {
    }

    public Cliente(Long idCliente, TipoCliente tipoCliente, String nombreCompletoCliente, String razonSocial,
                   String ruc, String dniCliente, String direccionCliente, String telefonoCliente,
                   String correoCliente) {
        this.idCliente = idCliente;
        this.tipoCliente = tipoCliente;
        this.nombreCompletoCliente = nombreCompletoCliente;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.dniCliente = dniCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNombreCompletoCliente() {
        return nombreCompletoCliente;
    }

    public void setNombreCompletoCliente(String nombreCompletoCliente) {
        this.nombreCompletoCliente = nombreCompletoCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }
}