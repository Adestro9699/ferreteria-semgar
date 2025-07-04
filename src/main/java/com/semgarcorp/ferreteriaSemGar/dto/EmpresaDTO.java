package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Empresa;

public class EmpresaDTO {
    private Integer idEmpresa;
    private String ruc;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String correo;
    private String estado;

    public EmpresaDTO() {}

    public EmpresaDTO(Empresa empresa) {
        this.idEmpresa = empresa.getIdEmpresa();
        this.ruc = empresa.getRuc();
        this.razonSocial = empresa.getRazonSocial();
        this.direccion = empresa.getDireccion();
        this.telefono = empresa.getTelefono();
        this.correo = empresa.getCorreo();
        this.estado = empresa.getEstado() != null ? empresa.getEstado().name() : null;
    }

    // Getters y setters
    public Integer getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Integer idEmpresa) { this.idEmpresa = idEmpresa; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Empresa toEntity() {
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(this.idEmpresa);
        empresa.setRuc(this.ruc);
        empresa.setRazonSocial(this.razonSocial);
        empresa.setDireccion(this.direccion);
        empresa.setTelefono(this.telefono);
        empresa.setCorreo(this.correo);
        if (this.estado != null) {
            empresa.setEstado(Empresa.EstadoEmpresa.valueOf(this.estado));
        }
        return empresa;
    }
} 