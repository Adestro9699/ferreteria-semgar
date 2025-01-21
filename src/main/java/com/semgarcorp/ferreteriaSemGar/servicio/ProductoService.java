package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
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


    //metodo para poder buscar nombres de productos
    //complementar el metodo con el frontend
    public List<Producto> buscarProductosPorNombre(String nombreProducto) {
        // Verificar si el nombre es nulo o está vacío
        if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
            // Si el nombre es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }

        try {
            // Buscar productos que contengan el nombre ingresado (sin importar mayúsculas/minúsculas)
            return productoRepositorio.findByNombreProductoContainingIgnoreCase(nombreProducto.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    //metodo para buscar productos por marca
    public List<Producto> buscarProductosPorMarca(String marca) {
        // Verificar si la marca es nula o está vacía
        if (marca == null || marca.trim().isEmpty()) {
            // Si la marca es nula o vacía, devolver una lista vacía
            return Collections.emptyList();
        }

        try {
            // Buscar productos cuya marca contenga la cadena ingresada (sin importar mayúsculas/minúsculas)
            return productoRepositorio.findByMarcaContainingIgnoreCase(marca.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    //metodo para buscar productos por categoría
    public List<Producto> buscarProductosPorCategoria(String nombreCategoria) {
        // Verificar si el nombre de la categoría es nulo o está vacío
        if (nombreCategoria == null || nombreCategoria.trim().isEmpty()) {
            // Si es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }

        try {
            // Buscar productos cuya categoría contenga el nombre ingresado (ignorar mayúsculas/minúsculas)
            return productoRepositorio.findByCategoriaNombreContainingIgnoreCase(nombreCategoria.trim());
        } catch (Exception e) {
            // Manejo de excepciones en caso de error
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }
}
