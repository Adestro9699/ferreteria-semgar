package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Buscar un cliente por su número de documento
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);

    // Buscar un cliente por su razón social
    Optional<Cliente> findByRazonSocialContainingIgnoreCase(String razonSocial);

    // Buscar clientes por nombre (búsqueda parcial)
    List<Cliente> findByNombresContainingIgnoreCase(String nombres);

    // Buscar clientes por apellido (búsqueda parcial)
    List<Cliente> findByApellidosContaining(String apellidos);
}
