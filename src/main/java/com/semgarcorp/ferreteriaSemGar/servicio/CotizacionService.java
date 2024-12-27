package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cotizacion;
import com.semgarcorp.ferreteriaSemGar.repositorio.CotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;

    // Constructor para inyectar el repositorio
    public CotizacionService(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    // Listar todas las cotizaciones
    public List<Cotizacion> listar() {
        return cotizacionRepository.findAll();
    }

    // Obtener una cotización por su ID
    public Cotizacion obtenerPorId(Long id) {
        Optional<Cotizacion> cotizacion = cotizacionRepository.findById(id);
        return cotizacion.orElse(null);  // Retorna null si no se encuentra la cotización
    }

    // Guardar una nueva cotización o actualizar una existente
    public Cotizacion guardar(Cotizacion cotizacion) {
        return cotizacionRepository.save(cotizacion);
    }

    // Eliminar una cotización por su ID
    public void eliminar(Long id) {
        cotizacionRepository.deleteById(id);
    }

    // Otras consultas personalizadas pueden ser agregadas aquí si es necesario
}
