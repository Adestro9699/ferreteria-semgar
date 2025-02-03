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
        return productoRepositorio.save(producto);
    }

    public void eliminar(Integer id) {
        productoRepositorio.deleteById(id);
    }

    public void eliminarProductosPorIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("La lista de IDs no puede estar vacía.");
        }

        // Metodo de JpaRepository para eliminar por IDs
        productoRepositorio.deleteAllById(ids);
    }

    //metodo para buscar productos por nombre
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

    //metodo para buscar productos por estado
    public List<Producto> buscarProductosPorEstado(String estado) {
        // Verificar si el estado es nulo o está vacío
        if (estado == null || estado.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Convertir el String a enum EstadoProducto (ignorando mayúsculas/minúsculas)
            Producto.EstadoProducto estadoProducto = Producto.EstadoProducto.valueOf(estado.toUpperCase());

            // Buscar productos por estado
            return productoRepositorio.findByEstadoProducto(estadoProducto);
        } catch (IllegalArgumentException e) {
            // Manejar el caso en que el estado no sea válido
            return Collections.emptyList();
        } catch (Exception e) {
            // Manejar otros errores
            return Collections.emptyList();
        }
    }

    //metodo para buscar productos por material
    public List<Producto> buscarProductosPorMaterial(String material) {
        // Verificar si el material es nulo o está vacío
        if (material == null || material.trim().isEmpty()) {
            // Si el material es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }

        try {
            // Buscar productos por material (ignorando mayúsculas/minúsculas)
            return productoRepositorio.findByMaterialContainingIgnoreCase(material.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // metodo para buscar productos por codigo de barra
    public List<Producto> buscarProductosPorCodigoBarra(String codigoBarra) {
        // Verificar si el código de barra es nulo o está vacío
        if (codigoBarra == null || codigoBarra.trim().isEmpty()) {
            // Si el código de barra es nulo o vacío, devolver una lista vacía
            return Collections.emptyList();
        }

        try {
            // Buscar productos por código de barra
            return productoRepositorio.findByCodigoBarra(codigoBarra.trim());
        } catch (Exception e) {
            // Manejo de excepciones si ocurre algún problema en la consulta
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // metodo para buscar productos por proveedor
    public List<Producto> buscarProductosPorNombreProveedor(String nombreProveedor) {
        // Verificar si el nombre del proveedor es nulo o está vacío
        if (nombreProveedor == null || nombreProveedor.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Buscar productos por nombre del proveedor
            return productoRepositorio.findByProveedorNombreContainingIgnoreCase(nombreProveedor.trim());
        } catch (Exception e) {
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    // metodo para buscar productos por nombre de subcategoria
    public List<Producto> buscarProductosPorNombreSubcategoria(String nombreSubcategoria) {
        // Verificar si el nombre de la subcategoría es nulo o está vacío
        if (nombreSubcategoria == null || nombreSubcategoria.trim().isEmpty()) {
            return Collections.emptyList();
        }

        try {
            // Buscar productos por nombre de la subcategoría
            return productoRepositorio.findBySubcategoriaNombreContainingIgnoreCase(nombreSubcategoria.trim());
        } catch (Exception e) {
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    public List<Producto> buscarProductosPorCodigoSKU(String codigoSKU) {
        // Verificar si el código SKU es nulo o está vacío
        if (codigoSKU == null || codigoSKU.trim().isEmpty()) {
            return Collections.emptyList(); // Retorna una lista vacía si el código SKU está vacío
        }

        try {
            // Buscar productos por código SKU
            return productoRepositorio.findByCodigoSKUContainingIgnoreCase(codigoSKU.trim());
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver una lista vacía
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }
}
