package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleVenta;
import com.semgarcorp.ferreteriaSemGar.repositorio.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepositorio;

    public DetalleVentaService(DetalleVentaRepository detalleVentaRepositorio) {
        this.detalleVentaRepositorio = detalleVentaRepositorio;
    }

    // Listar todos los detalles de venta
    public List<DetalleVenta> listar() {
        return detalleVentaRepositorio.findAll();
    }

    // Obtener un detalle de venta por su ID
    public DetalleVenta obtenerPorId(Integer id) {
        return detalleVentaRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo detalle de venta o actualizar uno existente
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        return detalleVentaRepositorio.save(detalleVenta);
    }

    // Actualizar un detalle de venta existente
    public DetalleVenta actualizar(DetalleVenta detalleVenta) {
        return detalleVentaRepositorio.save(detalleVenta);
    }

    // Eliminar un detalle de venta por su ID
    public void eliminar(Integer id) {
        detalleVentaRepositorio.deleteById(id);
    }


}