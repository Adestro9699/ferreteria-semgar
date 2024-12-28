package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Constructor con inyección del repositorio
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los usuarios
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerPorId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        return usuario.orElse(null); // Retorna null si no encuentra el usuario
    }

    // Guardar un nuevo usuario o actualiza uno existente
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Eliminar un usuario por ID
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Métodos adicionales, si son necesarios, como buscar por nombre o correo
    // Obtener usuario por correo electrónico
    /*
    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo); // Asumiendo que existe este metodo en el repositorio
    }
    */
}
