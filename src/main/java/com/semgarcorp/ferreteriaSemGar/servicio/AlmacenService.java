package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.AlmacenRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepositorio;

    public AlmacenService(AlmacenRepository almacenRepositorio) {
        this.almacenRepositorio = almacenRepositorio;
    }

    public List<Almacen> listar() {
        return almacenRepositorio.findAll();
    }

    public Almacen obtenerPorId(Integer id) {
        return almacenRepositorio.findById(id).orElse(null);
    }

    public Almacen guardar(Almacen almacen) {
        return almacenRepositorio.save(almacen);
    }

    public Almacen actualizar(Almacen almacen) {
        return almacenRepositorio.save(almacen);  // Esto persiste los cambios del almacen existente
    }

    public void eliminar(Integer id) {
        almacenRepositorio.deleteById(id);
    }
}
