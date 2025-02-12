package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Impuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImpuestoRepository extends JpaRepository<Impuesto,Integer> {

    /*// Metodo para buscar un Impuesto por nombre y estado
    // Retorna un Optional, ya que puede no existir un impuesto con ese nombre y estado
    Optional<Impuesto> findByNombreImpuestoAndEstado(String nombreImpuesto, Impuesto.EstadoActivo estado);

    // Metodo para buscar un Impuesto por porcentaje y estado
    // Retorna un Optional, ya que puede no existir un impuesto con ese porcentaje y estado
    Optional<Impuesto> findByPorcentajeAndEstado(BigDecimal porcentaje, Impuesto.EstadoActivo estado);*/

    //Metodo para buscar un Impuesto por estado
    List<Impuesto> findByEstado(Impuesto.EstadoActivo estado);
}
