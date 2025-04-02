package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleCompra;
import com.semgarcorp.ferreteriaSemGar.modelo.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {

    Optional<DetalleCompra> findByProducto(Producto producto);

}