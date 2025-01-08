package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ImpuestoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpuestoService {
    private final ImpuestoRepository impuestoRepositorio;

    //Constructor
    public ImpuestoService(ImpuestoRepository impuestoRepositorio) {
        this.impuestoRepositorio = impuestoRepositorio;
    }

    public List<Impuesto> listar() {
        return impuestoRepositorio.findAll();
    }

    public Impuesto obtenerPorId(Integer id) {
        return impuestoRepositorio.findById(id).orElse(null);
    }

    public Impuesto guardar(Impuesto impuesto) {
        return impuestoRepositorio.save(impuesto);
    }

    public Impuesto actualizar(Impuesto impuesto) {
        return impuestoRepositorio.save(impuesto);  // Esto persiste los cambios del impuesto existente
    }

    public void eliminar(Integer id) {
        impuestoRepositorio.deleteById(id);
    }
}
