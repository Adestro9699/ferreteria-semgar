package com.semgarcorp.ferreteriaSemGar.repositorio;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Integer> {
    
    /**
     * Buscar transferencias por estado
     */
    List<Transferencia> findByEstadoTransferencia(Transferencia.EstadoTransferencia estado);
    
    /**
     * Buscar transferencias por almacén origen
     */
    List<Transferencia> findByAlmacenOrigenIdAlmacen(Integer idAlmacen);
    
    /**
     * Buscar transferencias por almacén destino
     */
    List<Transferencia> findByAlmacenDestinoIdAlmacen(Integer idAlmacen);
}
