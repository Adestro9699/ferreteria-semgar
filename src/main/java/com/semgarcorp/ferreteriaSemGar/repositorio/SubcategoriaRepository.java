package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Subcategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {
    // Método para buscar subcategorías por el ID de la categoría
    List<Subcategoria> findByCategoria_IdCategoria(Integer idCategoria);
}