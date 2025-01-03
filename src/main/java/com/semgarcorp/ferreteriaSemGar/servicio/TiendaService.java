package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Tienda;
import com.semgarcorp.ferreteriaSemGar.repositorio.TiendaRepository;

import java.util.List;

public class TiendaService {

    private final TiendaRepository tiendaRepositorio;

    //Constructor
    public TiendaService(TiendaRepository tiendaRepositorio) {
        this.tiendaRepositorio = tiendaRepositorio;
    }

    public List<Tienda> listar() {
        return tiendaRepositorio.findAll();
    }

    public Tienda obtenerPorId(Integer id) {
        return tiendaRepositorio.findById(id).orElse(null);
    }

    public Tienda guardar(Tienda tienda) {
        return tiendaRepositorio.save(tienda);
    }

    public Tienda actualizar(Tienda tienda) {
        return tiendaRepositorio.save(tienda);
    }

    public void eliminar(Integer id) {
        tiendaRepositorio.deleteById(id);
    }
}
