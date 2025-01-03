package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.repositorio.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepositorio;

    public CategoriaService(CategoriaRepository categoriaRepositorio) {
        this.categoriaRepositorio = categoriaRepositorio;
    }

    public List<Categoria> listar() {
        return categoriaRepositorio.findAll();
    }

    public Categoria obtenerPorId(Integer id) {
        return categoriaRepositorio.findById(id).orElse(null);
    }

    public Categoria guardar(Categoria categoria) {
        return categoriaRepositorio.save(categoria);
    }

    public Categoria actualizar(Categoria categoria) {
        return categoriaRepositorio.save(categoria);  // Esto persiste los cambios del almacen existente
    }

    public void eliminar(Integer id) {
        categoriaRepositorio.deleteById(id);
    }
}