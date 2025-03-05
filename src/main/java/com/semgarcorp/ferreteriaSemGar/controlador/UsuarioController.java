package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.UsuarioCompletoDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
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
import java.util.stream.Collectors;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuarioRequest) {
        try {
            // Llamar al servicio para manejar la lógica de autenticación
            JwtResponse jwtResponse = usuarioService.autenticarUsuario(
                    usuarioRequest.getNombreUsuario(),
                    usuarioRequest.getContrasena()
            );
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Nuevo endpoint para obtener los datos completos de los usuarios
    @GetMapping("/completo")
    public ResponseEntity<List<UsuarioCompletoDTO>> obtenerUsuariosCompletos() {
        List<Usuario> usuarios = usuarioService.listar();
        List<UsuarioCompletoDTO> usuariosCompletos = usuarios.stream().map(usuario -> {
            // Obtener el trabajador asociado al usuario
            Trabajador trabajador = usuario.getTrabajador();

            // Obtener el acceso asociado al usuario
            Acceso acceso = accesoRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Acceso no encontrado para el usuario"));

            // Crear el DTO con los datos combinados
            return new UsuarioCompletoDTO(
                    trabajador.getNombreTrabajador(),
                    trabajador.getCargoTrabajador(),
                    usuario.getNombreUsuario(),
                    acceso.getRol(),
                    acceso.getPermisos(),
                    acceso.getIdAcceso()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(usuariosCompletos);
    }
}