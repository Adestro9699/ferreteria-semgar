package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.repositorio.TrabajadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepositorio;

    public TrabajadorService(TrabajadorRepository trabajadorRepositorio) {
        this.trabajadorRepositorio = trabajadorRepositorio;
    }

    public List<Trabajador> listar() {
        return trabajadorRepositorio.findAll();
    }

    public Trabajador obtenerPorId(Integer id) {
        return trabajadorRepositorio.findById(id).orElse(null);
    }

    public Trabajador guardar(Trabajador trabajador) {
        return trabajadorRepositorio.save(trabajador);
    }

    public Trabajador actualizar(Trabajador trabajador) {
        return trabajadorRepositorio.save(trabajador);
    }

    public void eliminar(Integer id) {
        trabajadorRepositorio.deleteById(id);
    }

    public Optional<Trabajador> buscarPorId(Integer id) {
        return trabajadorRepositorio.findById(id);
    }
}
