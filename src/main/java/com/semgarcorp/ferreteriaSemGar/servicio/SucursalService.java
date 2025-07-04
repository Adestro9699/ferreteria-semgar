package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;
import com.semgarcorp.ferreteriaSemGar.repositorio.SucursalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private final SucursalRepository sucursalRepositorio;

    //Constructor
    public SucursalService(SucursalRepository sucursalRepositorio) {
        this.sucursalRepositorio = sucursalRepositorio;
    }

    public List<Sucursal> listar() {
        return sucursalRepositorio.findAll();
    }

    public Sucursal obtenerPorId(Integer id) {
        return sucursalRepositorio.findById(id).orElse(null);
    }

    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepositorio.save(sucursal);
    }

    public Sucursal actualizar(Sucursal sucursal) {
        return sucursalRepositorio.save(sucursal);
    }

    public void eliminar(Integer id) {
        sucursalRepositorio.deleteById(id);
    }
}
