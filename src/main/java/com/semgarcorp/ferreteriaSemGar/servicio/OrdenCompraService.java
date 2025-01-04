package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.OrdenCompra;
import com.semgarcorp.ferreteriaSemGar.repositorio.OrdenCompraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepositorio;

    //Constructor
    public OrdenCompraService(OrdenCompraRepository ordenCompraRepositorio) {
        this.ordenCompraRepositorio = ordenCompraRepositorio;
    }

    public List<OrdenCompra> listar() {
        return ordenCompraRepositorio.findAll();
    }

    public OrdenCompra obtenerPorId(Integer id) {
        return ordenCompraRepositorio.findById(id).orElse(null);
    }

    public OrdenCompra guardar(OrdenCompra ordenCompra) {
        return ordenCompraRepositorio.save(ordenCompra);
    }

    public OrdenCompra actualizar(OrdenCompra ordenCompra) {
        return ordenCompraRepositorio.save(ordenCompra);  // Esto persiste los cambios del ordenCompra existente
    }

    public void eliminar(Integer id) {
        ordenCompraRepositorio.deleteById(id);
    }
}
