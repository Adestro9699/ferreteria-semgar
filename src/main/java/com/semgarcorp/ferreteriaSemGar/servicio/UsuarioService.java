package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepositorio;

    public UsuarioService(UsuarioRepository usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    // Listar todos los usuarios
    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    // Eliminar un usuario por ID
    public void eliminar(Integer id) {
        usuarioRepositorio.deleteById(id);
    }

    // Obtener usuario por nombre de usuario
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
    }
}
