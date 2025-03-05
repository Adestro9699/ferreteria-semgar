package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import com.semgarcorp.ferreteriaSemGar.seguridad.JwtResponse;
import com.semgarcorp.ferreteriaSemGar.seguridad.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepositorio;
    private final AccesoRepository accesoRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UsuarioService(UsuarioRepository usuarioRepositorio, AccesoRepository accesoRepositorio,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.accesoRepositorio = accesoRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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

    public JwtResponse autenticarUsuario(String nombreUsuario, String contrasena) {
        // Cargar los detalles del usuario
        Usuario usuario = usuarioRepositorio.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si la contraseña es válida
        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Obtener el acceso asociado al usuario
        Optional<Acceso> accesoOptional = accesoRepositorio.findAll().stream()
                .filter(acceso -> acceso.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst();

        if (accesoOptional.isEmpty() || accesoOptional.get().getRol() == null) {
            throw new RuntimeException("El usuario no tiene un rol asignado");
        }

        // Obtener el rol y los permisos del acceso
        String rol = accesoOptional.get().getRol();
        Map<String, Boolean> permisos = accesoOptional.get().getPermisos();

        // Obtener el trabajador relacionado con usuario
        Trabajador trabajador = usuario.getTrabajador();

        // Generar el token JWT
        String token = jwtUtil.generarToken(usuario.getNombreUsuario(), rol, permisos);

        // Devolver la respuesta
        return new JwtResponse(token, usuario, rol, permisos);
    }
}
