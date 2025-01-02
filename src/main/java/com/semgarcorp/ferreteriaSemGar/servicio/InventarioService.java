package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Inventario;
import com.semgarcorp.ferreteriaSemGar.repositorio.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepositorio;

    //Constructor
    public InventarioService(InventarioRepository inventarioRepositorio) {
        this.inventarioRepositorio = inventarioRepositorio;
    }

    public List<Inventario> listar() {
        return inventarioRepositorio.findAll();
    }

    public Inventario obtenerPorId(Integer id) {
        return inventarioRepositorio.findById(id).orElse(null);
    }

    public Inventario guardar(Inventario inventario) {
        return inventarioRepositorio.save(inventario);
    }

    public Inventario actualizar(Inventario inventario) {
        return inventarioRepositorio.save(inventario);  // Esto persiste los cambios del inventario existente
    }

    public void eliminar(Integer id) {
        inventarioRepositorio.deleteById(id);
    }
}
