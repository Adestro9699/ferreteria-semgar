package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.UnidadMedida;
import com.semgarcorp.ferreteriaSemGar.repositorio.UnidadMedidaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadMedidaService {

    private final UnidadMedidaRepository unidadMedidaRepositorio;

    public UnidadMedidaService(UnidadMedidaRepository unidadMedidaRepositorio) {
        this.unidadMedidaRepositorio = unidadMedidaRepositorio;
    }

    public List<UnidadMedida> listar() {
        return unidadMedidaRepositorio.findAll();
    }

    public UnidadMedida obtenerPorId(Integer id) {
        return unidadMedidaRepositorio.findById(id).orElse(null);
    }

    public UnidadMedida guardar(UnidadMedida unidadMedida) {
        return unidadMedidaRepositorio.save(unidadMedida);
    }

    public UnidadMedida actualizar(UnidadMedida unidadMedida) {
        return unidadMedidaRepositorio.save(unidadMedida);
    }

    public void eliminar(Integer id) {
        unidadMedidaRepositorio.deleteById(id);
    }
}