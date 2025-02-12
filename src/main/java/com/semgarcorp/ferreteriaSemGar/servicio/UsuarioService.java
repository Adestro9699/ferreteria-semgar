package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepositorio;
    private final AccesoRepository accesoRepositorio;

    public UsuarioService(UsuarioRepository usuarioRepositorio, AccesoRepository accesoRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.accesoRepositorio = accesoRepositorio;
    }

    // Listar todos los usuarios
    public List<Usuario> listar() {
        return usuarioRepositorio.findAll();
    }

    // Obtener un usuario por ID
    public Usuario obtenerPorId(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo usuario
    public Usuario guardar(Usuario usuario) {
        // Hashear la contraseña antes de guardarla
        String hashedPassword = BCrypt.hashpw(usuario.getContrasena(), BCrypt.gensalt());
        usuario.setContrasena(hashedPassword);

        // Guardar el usuario en la base de datos
        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        // Crear el acceso asociado al usuario
        Acceso acceso = new Acceso();
        acceso.setRol("USER"); // Rol predeterminado
        acceso.setPermisos(null); // Permisos nulos inicialmente
        acceso.setUsuario(usuarioGuardado); // Asociar el usuario al acceso
        accesoRepositorio.save(acceso);

        return usuarioGuardado;
    }

    // Actualizar un usuario existente
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
    public Optional<Usuario> obtenerPorNombreUsuario(String nombreUsuario) {
        return usuarioRepositorio.findByNombreUsuario(nombreUsuario);
    }
}