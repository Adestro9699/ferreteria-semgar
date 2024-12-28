package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Nomina;
import com.semgarcorp.ferreteriaSemGar.repositorio.NominaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NominaService {

    private final NominaRepository nominaRepository;

    // Constructor de la clase, inyectando el repositorio
    public NominaService(NominaRepository nominaRepository) {
        this.nominaRepository = nominaRepository;
    }

    // Listar todos los registros de nómina
    public List<Nomina> listar() {
        return nominaRepository.findAll();
    }

    // Obtener un registro de nómina por ID
    public Nomina obtenerPorId(Long id) {
        Optional<Nomina> nomina = nominaRepository.findById(id);
        return nomina.orElse(null); // Retorna null si no encuentra el registro
    }

    // Guardar un nuevo registro o actualiza uno existente
    public Nomina guardar(Nomina nomina) {
        return nominaRepository.save(nomina);
    }

    // Eliminar un registro de nómina por ID
    public void eliminar(Long id) {
        nominaRepository.deleteById(id);
    }

    public Nomina actualizar(Nomina nomina) {
        return nominaRepository.save(nomina);
    }


    // También puedes añadir otros métodos específicos, como:
    // - Obtener nómina por trabajador
    // - Consultar nóminas de un periodo específico
    // - Actualiza salario, etc.

    // Obtener nóminas por trabajador   x
    /*public List<Nomina> obtenerPorTrabajador(Long idTrabajador) {
        return nominaRepository.findByTrabajadorId(idTrabajador); // Asumiendo que existe este metodo en el repositorio
    }

    // Consultar nóminas por periodo de pago
    public List<Nomina> obtenerPorPeriodoPago(String periodoPago) {
        return nominaRepository.findByPeriodoPago(periodoPago); // Asumiendo que existe este metodo en el repositorio
    }*/
}
