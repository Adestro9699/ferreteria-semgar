package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Compra;
import com.semgarcorp.ferreteriaSemGar.repositorio.CompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepositorio;

    //Constructor
    public CompraService(CompraRepository compraRepositorio) {
        this.compraRepositorio = compraRepositorio;
    }

    public List<Compra> listar() {
        return compraRepositorio.findAll();
    }

    public Compra obtenerPorId(Integer id) {
        return compraRepositorio.findById(id).orElse(null);
    }

    public Compra guardar(Compra compra) {
        return compraRepositorio.save(compra);
    }

    public Compra actualizar(Compra compra) {
        return compraRepositorio.save(compra);  // Esto persiste los cambios de compra existente
    }

    public void eliminar(Integer id) {
        compraRepositorio.deleteById(id);
    }
}
