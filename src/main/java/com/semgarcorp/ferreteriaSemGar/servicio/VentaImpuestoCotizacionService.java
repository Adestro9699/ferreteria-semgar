package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.VentaImpuestoCotizacion;
import com.semgarcorp.ferreteriaSemGar.repositorio.VentaImpuestoCotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaImpuestoCotizacionService {
    private final VentaImpuestoCotizacionRepository ventaImpuestoCotizacionRepositorio;

    //Constructor
    public VentaImpuestoCotizacionService(VentaImpuestoCotizacionRepository ventaImpuestoCotizacionRepositorio) {
        this.ventaImpuestoCotizacionRepositorio = ventaImpuestoCotizacionRepositorio;
    }

    public List<VentaImpuestoCotizacion> listar() {
        return ventaImpuestoCotizacionRepositorio.findAll();
    }

    public VentaImpuestoCotizacion obtenerPorId(Integer id) {
        return ventaImpuestoCotizacionRepositorio.findById(id).orElse(null);
    }

    public VentaImpuestoCotizacion guardar(VentaImpuestoCotizacion ventaImpuestoCotizacion) {
        return ventaImpuestoCotizacionRepositorio.save(ventaImpuestoCotizacion);
    }

    public VentaImpuestoCotizacion actualizar(VentaImpuestoCotizacion ventaImpuestoCotizacion) {
        return ventaImpuestoCotizacionRepositorio.save(ventaImpuestoCotizacion);  // Esto persiste los cambios del ventaImpuestoCotizacion existente
    }

    public void eliminar(Integer id) {
        ventaImpuestoCotizacionRepositorio.deleteById(id);
    }
}
