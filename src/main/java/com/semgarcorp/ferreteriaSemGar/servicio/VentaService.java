package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import com.semgarcorp.ferreteriaSemGar.repositorio.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepositorio;

    public VentaService(VentaRepository ventaRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
    }

    public List<Venta> listar() {
        return ventaRepositorio.findAll();
    }

    public Venta obtenerPorId(Integer id) {
        return ventaRepositorio.findById(id).orElse(null);
    }

    public Venta guardar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    public Venta actualizar(Venta venta) {
        return ventaRepositorio.save(venta);
    }

    public void eliminar(Integer id) {
        ventaRepositorio.deleteById(id);
    }
}