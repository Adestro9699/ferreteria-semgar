package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class Cliente {

    public enum TipoCliente {
        NATURAL,
        JURIDICA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCliente;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    @Column(length = 255)
    private String nombresCliente;

    @Column(length = 255)
    private String apellidosCliente;

    @Column(length = 255)
    private String razonSocial;

    @Column(unique = true, length = 11)
    private String ruc;

    @Column(unique = true, length = 8)
    private String dniCliente;

    @Column(length = 200)
    private String direccionCliente;

    @Column(length = 50)
    private String telefonoCliente;

    @Column(unique = true, length = 100)
    @Email(message = "El correo debe tener un formato válido")
    private String correoCliente;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    public Cliente() {
    }

    public Cliente(Integer idCliente, TipoCliente tipoCliente, String nombresCliente, String apellidosCliente,
                   String razonSocial, String ruc, String dniCliente, String direccionCliente, String telefonoCliente,
                   String correoCliente, TipoDocumento tipoDocumento) {
        this.idCliente = idCliente;
        this.tipoCliente = tipoCliente;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.dniCliente = dniCliente;
        this.direccionCliente = direccionCliente;
        this.telefonoCliente = telefonoCliente;
        this.correoCliente = correoCliente;
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
