package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.InventarioTransferencia;
import com.semgarcorp.ferreteriaSemGar.repositorio.InventarioTransferenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioTransferenciaService {

    private final InventarioTransferenciaRepository inventarioTransferenciaRepositorio;

    public InventarioTransferenciaService(InventarioTransferenciaRepository inventarioTransferenciaRepositorio) {
        this.inventarioTransferenciaRepositorio = inventarioTransferenciaRepositorio;
    }

    public List<InventarioTransferencia> listar() {
        return inventarioTransferenciaRepositorio.findAll();
    }

    public InventarioTransferencia obtenerPorId(Integer id) {
        return inventarioTransferenciaRepositorio.findById(id).orElse(null);
    }

    public InventarioTransferencia guardar(InventarioTransferencia inventarioTransferencia) {
        return inventarioTransferenciaRepositorio.save(inventarioTransferencia);
    }

    public InventarioTransferencia actualizar(InventarioTransferencia inventarioTransferencia) {
        return inventarioTransferenciaRepositorio.save(inventarioTransferencia);  // Esto persiste los cambios del inventarioTransferencia existente
    }

    public void eliminar(Integer id) {
        inventarioTransferenciaRepositorio.deleteById(id);
    }
}
