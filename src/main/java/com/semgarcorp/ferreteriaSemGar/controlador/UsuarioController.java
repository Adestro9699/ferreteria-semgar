package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import com.semgarcorp.ferreteriaSemGar.seguridad.CustomUserDetailsService;
import com.semgarcorp.ferreteriaSemGar.seguridad.JwtResponse;
import com.semgarcorp.ferreteriaSemGar.seguridad.JwtUtil;
import com.semgarcorp.ferreteriaSemGar.servicio.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final AccesoRepository accesoRepository;

    // Constructor con todas las dependencias
    @Autowired
    public UsuarioController(UsuarioService usuarioService,
                             CustomUserDetailsService customUserDetailsService,
                             JwtUtil jwtUtil,
                             PasswordEncoder passwordEncoder,
                             UsuarioRepository usuarioRepository,
                             AccesoRepository accesoRepository) {
        this.usuarioService = usuarioService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.accesoRepository = accesoRepository;
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioService.listar();
        return ResponseEntity.ok(usuarios);
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Guardar un nuevo usuario
    @PostMapping
    public ResponseEntity<Usuario> guardar(@Valid @RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioService.guardar(usuario);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.getIdUsuario())
                .toUri();
        return ResponseEntity.created(location).body(nuevoUsuario);
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente != null) {
            usuario.setIdUsuario(id);
            Usuario usuarioActualizado = usuarioService.actualizar(usuario);
            return ResponseEntity.ok(usuarioActualizado);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Usuario usuarioExistente = usuarioService.obtenerPorId(id);
        if (usuarioExistente != null) {
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioRequest) {
        // Cargar los detalles del usuario usando la instancia inyectada de UsuarioRepository
        Usuario usuario = usuarioRepository.findByNombreUsuario(usuarioRequest.getNombreUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si la contraseña es válida
        if (!passwordEncoder.matches(usuarioRequest.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        // Obtener el acceso asociado al usuario desde el repositorio usando el ID del usuario
        Optional<Acceso> accesoOptional = accesoRepository.findAll().stream()
                .filter(acceso -> acceso.getUsuario().getIdUsuario().equals(usuario.getIdUsuario()))
                .findFirst();

        if (accesoOptional.isEmpty() || accesoOptional.get().getRol() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El usuario no tiene un rol asignado");
        }

        // Obtener el rol y los permisos del acceso
        String rol = accesoOptional.get().getRol();
        Map<String, Boolean> permisos = accesoOptional.get().getPermisos(); // Asegúrate de que `permisos` sea un mapa

        // Generar el token JWT con el nombre de usuario, el rol y los permisos
        String token = jwtUtil.generarToken(usuario.getNombreUsuario(), rol, permisos);

        // Devolver el token en la respuesta
        return ResponseEntity.ok(new JwtResponse(token));
    }
}