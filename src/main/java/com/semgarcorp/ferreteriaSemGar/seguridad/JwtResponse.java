package com.semgarcorp.ferreteriaSemGar.seguridad;

import com.semgarcorp.ferreteriaSemGar.dto.UsuarioDTO;

import java.util.Map;

public class JwtResponse {
    private String token;
    private UsuarioDTO usuario;
    private String rol;
    private Map<String, Boolean> permisos;

    // Constructor
    public JwtResponse() {
    }

    public JwtResponse(String token, UsuarioDTO usuario, String rol, Map<String, Boolean> permisos) {
        this.token = token;
        this.usuario = usuario;
        this.rol = rol;
        this.permisos = permisos;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Map<String, Boolean> getPermisos() {
        return permisos;
    }

    public void setPermisos(Map<String, Boolean> permisos) {
        this.permisos = permisos;
    }
}