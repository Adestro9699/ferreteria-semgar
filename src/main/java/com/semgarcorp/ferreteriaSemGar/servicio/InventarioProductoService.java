package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.InventarioProducto;
import com.semgarcorp.ferreteriaSemGar.repositorio.InventarioProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioProductoService {
    private final InventarioProductoRepository inventarioProductoRepositorio;

    //Constructor
    public InventarioProductoService(InventarioProductoRepository inventarioProductoRepositorio) {
        this.inventarioProductoRepositorio = inventarioProductoRepositorio;
    }

    public List<InventarioProducto> listar() {
        return inventarioProductoRepositorio.findAll();
    }

    public InventarioProducto obtenerPorId(Integer id) {
        return inventarioProductoRepositorio.findById(id).orElse(null);
    }

    public InventarioProducto guardar(InventarioProducto inventarioProducto) {
        return inventarioProductoRepositorio.save(inventarioProducto);
    }

    public InventarioProducto actualizar(InventarioProducto inventarioProducto) {
        return inventarioProductoRepositorio.save(inventarioProducto);
    }

    public void eliminar(Integer id) {
        inventarioProductoRepositorio.deleteById(id);
    }
}
