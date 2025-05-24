package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import com.semgarcorp.ferreteriaSemGar.servicio.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Page;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Obtener la lista de todos los clientes
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    // Obtener un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Integer id) {
        Cliente cliente = clienteService.obtenerPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente); // Respuesta simplificada con 200 OK
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
    }

    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> guardar(@RequestBody Cliente cliente) {
        // Guardar el cliente usando el servicio
        Cliente nuevoCliente = clienteService.guardar(cliente);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoCliente.getIdCliente()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
        return ResponseEntity.created(location).body(nuevoCliente);
    }

    // Actualizar un cliente existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id, @RequestBody Cliente cliente) {
        // Obtener el cliente existente
        Cliente clienteExistente = clienteService.obtenerPorId(id);

        if (clienteExistente != null) {
            // Asegurarse de que el ID se mantenga y reemplazar el cliente
            cliente.setIdCliente(id);

            // Aquí reemplazas completamente el cliente con la información que viene en el cuerpo
            Cliente clienteActualizado = clienteService.actualizar(cliente);

            return ResponseEntity.ok(clienteActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
        }
    }

    // Eliminar un cliente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Cliente clienteExistente = clienteService.obtenerPorId(id);
        if (clienteExistente != null) {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build(); // Respuesta sin contenido
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
    }

    @GetMapping("/buscarPorNumeroDocumento")
    public ResponseEntity<Cliente> buscarPorNumeroDocumento(@RequestParam String numeroDocumento) {
        // Verificar si el número de documento es nulo o está vacío
        if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar el cliente por número de documento
            Optional<Cliente> cliente = clienteService.buscarPorNumeroDocumento(numeroDocumento);

            // Verificar si se encontró el cliente
            return cliente.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build()); // 200 OK sin cuerpo si no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // Endpoint para buscar un cliente por su razón social
    @GetMapping("/buscarPorRazonSocial")
    public ResponseEntity<Cliente> buscarPorRazonSocial(@RequestParam String razonSocial) {
        // Verificar si la razón social es nula o está vacía
        if (razonSocial == null || razonSocial.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar el cliente por razón social
            Optional<Cliente> cliente = clienteService.buscarPorRazonSocial(razonSocial);

            // Verificar si se encontró el cliente
            return cliente.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok().build()); // 200 OK sin cuerpo si no se encuentra
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // Endpoint para buscar clientes por nombre
    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombres) {
        // Verificar si el nombre es nulo o está vacío
        if (nombres == null || nombres.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar clientes por nombre
            List<Cliente> clientes = clienteService.buscarPorNombre(nombres);

            // Verificar si se encontraron clientes
            if (clientes.isEmpty()) {
                return ResponseEntity.ok().build(); // 200 OK sin cuerpo
            }

            // Devolver la lista de clientes con un código 200 OK
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // Endpoint para buscar clientes por apellido
    @GetMapping("/buscarPorApellido")
    public ResponseEntity<List<Cliente>> buscarPorApellido(@RequestParam String apellidos) {
        // Verificar si el apellido es nulo o está vacío
        if (apellidos == null || apellidos.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // Buscar clientes por apellido
            List<Cliente> clientes = clienteService.buscarPorApellido(apellidos);

            // Verificar si se encontraron clientes
            if (clientes.isEmpty()) {
                return ResponseEntity.ok().build(); // 200 OK sin cuerpo
            }

            // Devolver la lista de clientes con un código 200 OK
            return ResponseEntity.ok(clientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // 1. Paginación TRADICIONAL (límite fijo de 10 registros por página)
    @GetMapping("/paginacion-tradicional")
    public ResponseEntity<Page<Cliente>> listarClientesConPaginacionTradicional(
            @RequestParam(defaultValue = "0") int pagina) {
        Page<Cliente> clientes = clienteService.listarConPaginacionTradicional(pagina);
        return ResponseEntity.ok(clientes);
    }

    // 2. Paginación DINÁMICA (el frontend elige cuántos registros por página)
    @GetMapping("/paginacion-dinamica")
    public ResponseEntity<Page<Cliente>> listarClientesConPaginacionDinamica(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int registrosPorPagina) {
        Page<Cliente> clientes = clienteService.listarConPaginacionDinamica(pagina, registrosPorPagina);
        return ResponseEntity.ok(clientes);
    }
}
