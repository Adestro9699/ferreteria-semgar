package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Proveedor;
import com.semgarcorp.ferreteriaSemGar.repositorio.ProveedorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepositorio;

    public ProveedorService(ProveedorRepository proveedorRepositorio) {
        this.proveedorRepositorio = proveedorRepositorio;
    }

    public List<Proveedor> listar() {
        return proveedorRepositorio.findAll();
    }

    public Proveedor obtenerPorId(Integer id) {
        return proveedorRepositorio.findById(id).orElse(null);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepositorio.save(proveedor);
    }

    public Proveedor actualizar(Proveedor proveedor) {
        return proveedorRepositorio.save(proveedor);  // Esto persiste los cambios del almacen existente
    }

    public void eliminar(Integer id) {
        proveedorRepositorio.deleteById(id);
    }
}
