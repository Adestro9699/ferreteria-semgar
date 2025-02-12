package com.semgarcorp.ferreteriaSemGar.seguridad;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AccesoRepository accesoRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        // Cargar el usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Obtener el acceso asociado al usuario desde el repositorio usando el ID del usuario
        Optional<Acceso> accesoOptional = accesoRepository.findAll().stream()
                .filter(acceso -> acceso.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst();

        if (accesoOptional.isEmpty() || accesoOptional.get().getRol() == null) {
            throw new UsernameNotFoundException("El usuario no tiene un rol asignado");
        }

        // Obtener el rol y los permisos del acceso
        String rol = accesoOptional.get().getRol();
        Map<String, Boolean> permisos = accesoOptional.get().getPermisos(); // Asegúrate de que `permisos` sea un mapa

        // Crear una lista de autoridades (roles) para Spring Security
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rol));

        return new User(
                usuario.getNombreUsuario(),
                usuario.getContrasena(),
                authorities
        );
    }

    /**
     * Genera un token JWT para el usuario, incluyendo su rol y permisos.
     *
     * @param usuario El usuario para el cual se generará el token.
     * @return Token JWT generado.
     */
    public String generarTokenParaUsuario(Usuario usuario) {
        // Obtener el acceso asociado al usuario desde el repositorio usando el ID del usuario
        Optional<Acceso> accesoOptional = accesoRepository.findAll().stream()
                .filter(acceso -> acceso.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst();

        if (accesoOptional.isEmpty() || accesoOptional.get().getRol() == null) {
            throw new RuntimeException("El usuario no tiene un rol asignado");
        }

        // Obtener el rol y los permisos del acceso
        String rol = accesoOptional.get().getRol();
        Map<String, Boolean> permisos = accesoOptional.get().getPermisos(); // Asegúrate de que `permisos` sea un mapa

        // Generar el token JWT con el nombre de usuario, el rol y los permisos
        return jwtUtil.generarToken(usuario.getNombreUsuario(), rol, permisos);
    }
}