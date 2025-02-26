package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import com.semgarcorp.ferreteriaSemGar.repositorio.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepositorio;

    public ClienteService(ClienteRepository clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    public List<Cliente> listar() {
        return clienteRepositorio.findAll();
    }

    public Cliente obtenerPorId(Integer id) {
        return clienteRepositorio.findById(id).orElse(null);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    public Cliente actualizar(Cliente cliente) {
        return clienteRepositorio.save(cliente);  // Esto persiste los cambios del cliente existente
    }

    public void eliminar(Integer id) {
        clienteRepositorio.deleteById(id);
    }

    // Metodo para buscar un cliente por su número de documento
    public Optional<Cliente> buscarPorNumeroDocumento(String numeroDocumento) {
        return clienteRepositorio.findByNumeroDocumento(numeroDocumento);
    }

    // Metodo para buscar un cliente por su razón social
    public Optional<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepositorio.findByRazonSocialContainingIgnoreCase(razonSocial);
    }

    // Metodo para buscar clientes por nombre
    public List<Cliente> buscarPorNombre(String nombres) {
        return clienteRepositorio.findByNombresContainingIgnoreCase(nombres);
    }

    // Metodo para buscar clientes por apellido
    public List<Cliente> buscarPorApellido(String apellidos) {
        return clienteRepositorio.findByApellidosContaining(apellidos);
    }
}
