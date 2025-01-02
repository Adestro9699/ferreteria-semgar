package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Nomina;
import com.semgarcorp.ferreteriaSemGar.repositorio.NominaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NominaService {

    private final NominaRepository nominaRepositorio;

    // Constructor de la clase, inyectando el repositorio
    public NominaService(NominaRepository nominaRepositorio) {
        this.nominaRepositorio = nominaRepositorio;
    }

    // Listar todos los registros de nómina
    public List<Nomina> listar() {
        return nominaRepositorio.findAll();
    }

    // Obtener un registro de nómina por ID
    public Nomina obtenerPorId(Integer id) {
        return nominaRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo registro o actualiza uno existente
    public Nomina guardar(Nomina nomina) {
        return nominaRepositorio.save(nomina);
    }

    public Nomina actualizar(Nomina nomina) {
        return nominaRepositorio.save(nomina);
    }

    // Eliminar un registro de nómina por ID
    public void eliminar(Integer id) {
        nominaRepositorio.deleteById(id);
    }
}
