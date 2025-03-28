package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Categoria;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import com.semgarcorp.ferreteriaSemGar.modelo.Utilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilidadRepository extends JpaRepository<Utilidad, Integer> {

    Optional<Utilidad> findByProducto(Producto producto);

    Optional<Utilidad> findByCategoria(Categoria categoria);
}