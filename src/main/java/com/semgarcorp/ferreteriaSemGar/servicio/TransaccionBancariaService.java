package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.TransaccionBancaria;
import com.semgarcorp.ferreteriaSemGar.repositorio.TransaccionBancariaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionBancariaService {

    private final TransaccionBancariaRepository transaccionBancariaRepositorio;

    // Constructor con inyección del repositorio
    public TransaccionBancariaService(TransaccionBancariaRepository transaccionBancariaRepositorio) {
        this.transaccionBancariaRepositorio = transaccionBancariaRepositorio;
    }

    public List<TransaccionBancaria> listar() {
        return transaccionBancariaRepositorio.findAll();
    }

    public TransaccionBancaria obtenerPorId(Integer id) {
        return transaccionBancariaRepositorio.findById(id).orElse(null);
    }

    public TransaccionBancaria guardar(TransaccionBancaria acceso) {
        return transaccionBancariaRepositorio.save(acceso);
    }

    public TransaccionBancaria actualizar(TransaccionBancaria acceso) {
        return transaccionBancariaRepositorio.save(acceso);
    }

    public void eliminar(Integer id) {
        transaccionBancariaRepositorio.deleteById(id);
    }
}