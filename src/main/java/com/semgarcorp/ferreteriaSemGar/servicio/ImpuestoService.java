package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ImpuestoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ImpuestoService {
    private final ImpuestoRepository impuestoRepositorio;

    //Constructor
    public ImpuestoService(ImpuestoRepository impuestoRepositorio) {
        this.impuestoRepositorio = impuestoRepositorio;
    }

    //metodo para obtener impuestos ACTIVOS
    public List<Impuesto> obtenerImpuestosActivos() {
        return impuestoRepositorio.findByEstado(Impuesto.EstadoActivo.ACTIVO);
    }

    //metodo para activar o desactivar un impuesto
    public void actualizarEstadoImpuesto(Integer idImpuesto, Impuesto.EstadoActivo estado) {
        Impuesto impuesto = impuestoRepositorio.findById(idImpuesto)
                .orElseThrow(() -> new IllegalArgumentException("Impuesto no encontrado"));
        impuesto.setEstado(estado);
        impuestoRepositorio.save(impuesto);
    }

    public List<Impuesto> listar() {
        return impuestoRepositorio.findAll();
    }

    public Impuesto obtenerPorId(Integer id) {
        // Lanzamos una excepción si no se encuentra el impuesto
        return impuestoRepositorio.findById(id).orElseThrow(() -> new IllegalArgumentException("Impuesto no encontrado"));
    }

    public Impuesto guardar(Impuesto impuesto) {
        return impuestoRepositorio.save(impuesto);
    }

    public Impuesto actualizar(Impuesto impuesto) {
        // Validación: Verificar si el impuesto a actualizar existe en la base de datos
        if (!impuestoRepositorio.existsById(impuesto.getIdImpuesto())) {
            throw new IllegalArgumentException("El impuesto a actualizar no existe.");
        }
        return impuestoRepositorio.save(impuesto);
    }

    public void eliminar(Integer id) {
        impuestoRepositorio.deleteById(id);
    }

    //metodo que calcula el valor del impuesto basado en un subtotal y el porcentaje del impuesto
    public BigDecimal calcularValorImpuesto(BigDecimal subtotal, Impuesto impuesto) {
        if (subtotal == null || impuesto == null || impuesto.getPorcentaje() == null) {
            return BigDecimal.ZERO;
        }
        return subtotal.multiply(impuesto.getPorcentaje().divide(BigDecimal.valueOf(100)))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
