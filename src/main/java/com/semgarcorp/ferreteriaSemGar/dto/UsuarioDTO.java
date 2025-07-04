package com.semgarcorp.ferreteriaSemGar.dto;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import java.time.LocalDate;

public class UsuarioDTO {
    private Integer idUsuario;
    private String nombreUsuario;
    private String estadoUsuario;
    private LocalDate fechaCreacion;
    private TrabajadorDTO trabajador;

    public UsuarioDTO() {}

    public UsuarioDTO(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.estadoUsuario = usuario.getEstadoUsuario() != null ? usuario.getEstadoUsuario().name() : null;
        this.fechaCreacion = usuario.getFechaCreacion();
        if (usuario.getTrabajador() != null) {
            this.trabajador = new TrabajadorDTO(usuario.getTrabajador());
        }
    }

    // Getters y setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getEstadoUsuario() { return estadoUsuario; }
    public void setEstadoUsuario(String estadoUsuario) { this.estadoUsuario = estadoUsuario; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public TrabajadorDTO getTrabajador() { return trabajador; }
    public void setTrabajador(TrabajadorDTO trabajador) { this.trabajador = trabajador; }

    public Usuario toEntity() {
        Usuario u = new Usuario();
        u.setIdUsuario(this.idUsuario);
        u.setNombreUsuario(this.nombreUsuario);
        if (this.estadoUsuario != null) {
            u.setEstadoUsuario(Usuario.EstadoUsuario.valueOf(this.estadoUsuario));
        }
        u.setFechaCreacion(this.fechaCreacion);
        if (this.trabajador != null) {
            u.setTrabajador(this.trabajador.toEntity());
        }
        return u;
    }
} 