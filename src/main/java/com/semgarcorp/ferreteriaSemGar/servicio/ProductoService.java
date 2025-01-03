package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;

import java.util.List;

public class ProductoService {

    private final ProductoRepository productoRepositorio;

    //Constructor
    public ProductoService(ProductoRepository productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> listar() {
        return productoRepositorio.findAll();
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public Producto guardar(Producto producto) {
        return productoRepositorio.save(producto);
    }

    public Producto actualizar(Producto producto) {
        return productoRepositorio.save(producto);  // Esto persiste los cambios del producto existente
    }

    public void eliminar(Integer id) {
        productoRepositorio.deleteById(id);
    }
}
