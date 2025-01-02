package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cotizacion;
import com.semgarcorp.ferreteriaSemGar.repositorio.CotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepositorio;

    // Constructor para inyectar el repositorio
    public CotizacionService(CotizacionRepository cotizacionRepositorio) {
        this.cotizacionRepositorio = cotizacionRepositorio;
    }

    // Listar todas las cotizaciones
    public List<Cotizacion> listar() {
        return cotizacionRepositorio.findAll();
    }

    // Obtener una cotización por su ID
    public Cotizacion obtenerPorId(Integer id) {
        return cotizacionRepositorio.findById(id).orElse(null);
    }

    // Guardar una nueva cotización o actualiza una existente
    public Cotizacion guardar(Cotizacion cotizacion) {
        return cotizacionRepositorio.save(cotizacion);
    }

    public Cotizacion actualizar(Cotizacion cotizacion) {
        return cotizacionRepositorio.save(cotizacion);
    }

    // Eliminar una cotización por su ID
    public void eliminar(Integer id) {
        cotizacionRepositorio.deleteById(id);
    }
}
