package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccesoService {

    private final AccesoRepository accesoRepositorio;

    // Constructor con inyección del repositorio
    public AccesoService(AccesoRepository accesoRepositorio) {
        this.accesoRepositorio = accesoRepositorio;
    }

    // Listar todos los usuarios
    public List<Acceso> listar() {
        return accesoRepositorio.findAll();
    }

    // Obtener un usuario por ID
    public Acceso obtenerPorId(Integer id) {
        return accesoRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo usuario o actualiza uno existente
    public Acceso guardar(Acceso acceso) {
        return accesoRepositorio.save(acceso);
    }

    public Acceso actualizar(Acceso acceso) {
        return accesoRepositorio.save(acceso);
    }

    // Eliminar un usuario por ID
    public void eliminar(Integer id) {
        accesoRepositorio.deleteById(id);
    }
}