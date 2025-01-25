package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
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
        // Hashear la contraseña antes de guardarla
        String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        usuario.setContrasena(hashedPassword);

        // Guardar el usuario en la base de datos
        return usuarioRepositorio.save(usuario);
    }

    public Usuario actualizar(Usuario usuario) {
        // Hashear la contraseña antes de guardarla
        String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        usuario.setContrasena(hashedPassword);

        // Guardar el usuario actualizado
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

    public boolean autenticarUsuario(String nombreUsuario, String password) {
        // Buscar al usuario por su nombre de usuario
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario);

        if (usuario != null) {
            // Verificar si la contraseña coincide con el hash almacenado
            return BCrypt.checkpw(password, usuario.getContrasena());
        }

        return false; // Usuario no encontrado
    }
}
