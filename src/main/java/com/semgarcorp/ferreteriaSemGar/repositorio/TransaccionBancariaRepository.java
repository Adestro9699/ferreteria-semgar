package com.semgarcorp.ferreteriaSemGar.repositorio;


import com.semgarcorp.ferreteriaSemGar.modelo.TransaccionBancaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionBancariaRepository extends JpaRepository<TransaccionBancaria, Integer> {
}
