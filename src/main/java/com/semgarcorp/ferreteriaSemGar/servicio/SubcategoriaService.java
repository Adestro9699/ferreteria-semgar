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

    public List<Subcategoria> listar() {
        return subcategoriaRepositorio.findAll();
    }

    public Subcategoria obtenerPorId(Integer id) {
        return subcategoriaRepositorio.findById(id).orElse(null);
    }

    public Subcategoria guardar(Subcategoria subcategoria) {
        return subcategoriaRepositorio.save(subcategoria);
    }

    public Subcategoria actualizar(Subcategoria subcategoria) {
        return subcategoriaRepositorio.save(subcategoria);
    }

    public void eliminar(Integer id) {
        subcategoriaRepositorio.deleteById(id);
    }
}