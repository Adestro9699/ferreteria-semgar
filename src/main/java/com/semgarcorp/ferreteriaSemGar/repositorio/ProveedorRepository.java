package com.semgarcorp.ferreteriaSemGar.repositorio;


import com.semgarcorp.ferreteriaSemGar.modelo.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}