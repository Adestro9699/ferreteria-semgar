package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import com.semgarcorp.ferreteriaSemGar.repositorio.TransferenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferenciaService {
    private final TransferenciaRepository transferenciaRepositorio;

    // Constructor para inyectar el repositorio
    public TransferenciaService(TransferenciaRepository transferenciaRepositorio) {
        this.transferenciaRepositorio = transferenciaRepositorio;
    }

    // Listar todas las transferencias
    public List<Transferencia> listar() {
        return transferenciaRepositorio.findAll();
    }

    // Obtener una transferencia por su ID
    public Transferencia obtenerPorId(Integer id) {
        return transferenciaRepositorio.findById(id).orElse(null);
    }

    // Guardar una nueva transferencia o actualiza una existente
    public Transferencia guardar(Transferencia transferencia) {
        return transferenciaRepositorio.save(transferencia);
    }

    public Transferencia actualizar(Transferencia transferencia) {
        return transferenciaRepositorio.save(transferencia);
    }

    // Eliminar una transferencia por su ID
    public void eliminar(Integer id) {
        transferenciaRepositorio.deleteById(id);
    }
}
