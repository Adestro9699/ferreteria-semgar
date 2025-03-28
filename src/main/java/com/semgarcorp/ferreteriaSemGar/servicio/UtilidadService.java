package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.Utilidad;
import com.semgarcorp.ferreteriaSemGar.repositorio.UtilidadRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UtilidadService {

    private final UtilidadRepository utilidadRepositorio;

    public UtilidadService(UtilidadRepository utilidadRepositorio) {
        this.utilidadRepositorio = utilidadRepositorio;
    }

    public List<Utilidad> listar() {
        return utilidadRepositorio.findAll();
    }

    public Utilidad obtenerPorId(Integer id) {
        return utilidadRepositorio.findById(id).orElse(null);
    }

    public Utilidad guardar(Utilidad utilidad) {
        return utilidadRepositorio.save(utilidad);
    }

    public Utilidad actualizar(Utilidad utilidad) {
        return utilidadRepositorio.save(utilidad);
    }

    public void eliminar(Integer id) {
        utilidadRepositorio.deleteById(id);
    }

    public Optional<BigDecimal> obtenerPorcentajeUtilidadPorProducto(Producto producto) {
        return utilidadRepositorio.findByProducto(producto)
                .map(utilidad -> utilidad.getPorcentajeUtilidadComoDecimal());
    }

    public Optional<BigDecimal> obtenerPorcentajeUtilidadPorCategoria(Categoria categoria) {
        return utilidadRepositorio.findByCategoria(categoria)
                .map(utilidad -> utilidad.getPorcentajeUtilidadComoDecimal());
    }
}