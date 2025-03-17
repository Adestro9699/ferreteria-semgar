package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Subcategoria;
import com.semgarcorp.ferreteriaSemGar.repositorio.SubcategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepositorio;

    public SubcategoriaService(SubcategoriaRepository subcategoriaRepositorio) {
        this.subcategoriaRepositorio = subcategoriaRepositorio;
    }

    // Obtener todas las subcategorías
    public List<Subcategoria> listar() {
        return subcategoriaRepositorio.findAll();
    }

    // Obtener subcategorías por ID de categoría
    public List<Subcategoria> listarPorCategoria(Integer idCategoria) {
        return subcategoriaRepositorio.findByCategoria_IdCategoria(idCategoria);
    }

    // Obtener una subcategoría por su ID
    public Subcategoria obtenerPorId(Integer id) {
        return subcategoriaRepositorio.findById(id).orElse(null);
    }

    // Guardar una nueva subcategoría
    public Subcategoria guardar(Subcategoria subcategoria) {
        return subcategoriaRepositorio.save(subcategoria);
    }

    // Actualizar una subcategoría existente
    public Subcategoria actualizar(Subcategoria subcategoria) {
        return subcategoriaRepositorio.save(subcategoria);
    }

    // Eliminar una subcategoría por su ID
    public void eliminar(Integer id) {
        subcategoriaRepositorio.deleteById(id);
    }
}