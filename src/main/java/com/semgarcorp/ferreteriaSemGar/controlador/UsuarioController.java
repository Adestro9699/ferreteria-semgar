package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.servicio.UsuarioService;
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

    // Constructor con inyección del servicio
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint GET para listar todos los usuarios
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    // Endpoint GET por ID para obtener un usuario específico
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    // Endpoint POST para guardar un nuevo usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Responde con estado 201 (Creado)
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        // Guardar el usuario usando el servicio
        Usuario nuevoUsuario = usuarioService.guardar(usuario);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.getIdUsuario())  // Suponiendo que el campo es "id_usuario"
                .toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoUsuario);
    }


    // Endpoint PUT para actualizar un usuario existente (reemplaza el recurso completo)
    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        // Se podría agregar lógica adicional para verificar si el usuario existe antes de actualizarlo
        usuario.setIdUsuario(id); // Asegurarse de que el id no cambie
        return usuarioService.guardar(usuario);
    }

    // Endpoint PATCH para actualizar parcialmente un usuario
    @PatchMapping("/{id}")
    public Usuario actualizarParcial(@PathVariable Long id, @RequestBody Usuario usuario) {
        Usuario usuarioExistente = usuarioService.obtenerPorId(id);

        if (usuarioExistente != null) {
            // Actualizar solo los campos que no sean nulos
            if (usuario.getNombreUsuario() != null) {
                usuarioExistente.setNombreUsuario(usuario.getNombreUsuario());
            }
            if (usuario.getCorreoUsuario() != null) {
                usuarioExistente.setCorreoUsuario(usuario.getCorreoUsuario());
            }
            if (usuario.getEstadoUsuario() != null) {
                usuarioExistente.setEstadoUsuario(usuario.getEstadoUsuario());
            }
            // Aquí podrías agregar más validaciones si es necesario

            return usuarioService.guardar(usuarioExistente);
        }
        return null; // O lanzar una excepción si el usuario no existe
    }

    // Endpoint DELETE para eliminar un usuario por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Responde con estado 204 (Sin contenido)
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
