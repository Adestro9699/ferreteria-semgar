package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import com.semgarcorp.ferreteriaSemGar.repositorio.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
