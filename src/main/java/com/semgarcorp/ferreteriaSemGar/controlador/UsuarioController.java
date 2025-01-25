package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.servicio.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        // Verificar las credenciales usando el metodo autenticarUsuario
        boolean autenticado = usuarioService.autenticarUsuario(usuario.getNombreUsuario(), usuario.getContrasena());

        if (autenticado) {
            return ResponseEntity.ok("Autenticación exitosa");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }
}


