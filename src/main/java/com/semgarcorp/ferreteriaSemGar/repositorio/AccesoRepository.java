package com.semgarcorp.ferreteriaSemGar.repositorio;


import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccesoRepository extends JpaRepository<Acceso, Integer> {
}
