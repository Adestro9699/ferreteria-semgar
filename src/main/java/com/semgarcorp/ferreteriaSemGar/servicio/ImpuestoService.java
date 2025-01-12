package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ImpuestoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // Lanzamos una excepción si no se encuentra el impuesto
        return impuestoRepositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("Impuesto no encontrado"));
    }

    public Impuesto guardar(Impuesto impuesto) {
        // Validación: Verificar si ya existe un impuesto con el mismo nombre o porcentaje
        if (existeImpuesto(impuesto)) {
            throw new IllegalArgumentException("El impuesto ya existe.");
        }
        return impuestoRepositorio.save(impuesto);
    }

    public Impuesto actualizar(Impuesto impuesto) {
        // Validación: Verificar si el impuesto a actualizar existe en la base de datos
        if (!impuestoRepositorio.existsById(impuesto.getIdImpuesto())) {
            throw new IllegalArgumentException("El impuesto a actualizar no existe.");
        }
        return impuestoRepositorio.save(impuesto);
    }

    // Metodo privado para verificar si ya existe un impuesto con el mismo nombre o porcentaje y si está activo
    private boolean existeImpuesto(Impuesto impuesto) {
        // Buscar si ya existe un impuesto con el mismo nombre y estado activo
        Optional<Impuesto> impuestoExistentePorNombreYActivo = impuestoRepositorio.findByNombreImpuestoAndEstado(impuesto.getNombreImpuesto(), Impuesto.EstadoActivo.ACTIVO);
        // Buscar si ya existe un impuesto con el mismo porcentaje y estado activo
        Optional<Impuesto> impuestoExistentePorPorcentajeYActivo = impuestoRepositorio.findByPorcentajeAndEstado(impuesto.getPorcentaje(), Impuesto.EstadoActivo.ACTIVO);

        // Verificar si existe un impuesto con el mismo nombre o porcentaje, y que esté activo
        return impuestoExistentePorNombreYActivo.isPresent() || impuestoExistentePorPorcentajeYActivo.isPresent();
    }

    public void eliminar(Integer id) {
        impuestoRepositorio.deleteById(id);
    }

    //metodo para desactivar un impuesto
    public Impuesto desactivarImpuesto(Integer idImpuesto) {
        Impuesto impuesto = impuestoRepositorio.findById(idImpuesto)
                .orElseThrow(() -> new IllegalArgumentException("Impuesto no encontrado"));

        // Cambiar el estado a INACTIVO
        impuesto.setEstado(Impuesto.EstadoActivo.INACTIVO);
        return impuestoRepositorio.save(impuesto);
    }

    //metodo para activar un impuesto
    public Impuesto activarImpuesto(Integer idImpuesto) {
        Impuesto impuesto = impuestoRepositorio.findById(idImpuesto)
                .orElseThrow(() -> new IllegalArgumentException("Impuesto no encontrado"));

        // Cambiar el estado a ACTIVO
        impuesto.setEstado(Impuesto.EstadoActivo.ACTIVO);
        return impuestoRepositorio.save(impuesto);
    }

    //metodo para traer impuestos que tengan estado ACTIVO
    public List<Impuesto> listarActivos() {
        return impuestoRepositorio.findByEstado(Impuesto.EstadoActivo.ACTIVO);
    }
}
