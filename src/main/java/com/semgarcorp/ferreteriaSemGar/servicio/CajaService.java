package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.repositorio.CajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    private final CajaRepository cajaRepository;

    // Constructor con inyección del repositorio
    public CajaService(CajaRepository cajaRepository) {
        this.cajaRepository = cajaRepository;
    }

    // Listar todos los registros de caja
    public List<Caja> listar() {
        return cajaRepository.findAll();
    }

    // Obtener un registro de caja por ID
    public Caja obtenerPorId(Long id) {
        Optional<Caja> caja = cajaRepository.findById(id);
        return caja.orElse(null); // Retorna null si no encuentra el registro
    }

    // Guardar un nuevo registro de caja o actualiza uno existente
    public Caja guardar(Caja caja) {
        return cajaRepository.save(caja);
    }

    // Eliminar un registro de caja por ID
    public void eliminar(Long id) {
        cajaRepository.deleteById(id);
    }

    public Caja actualizar(Caja caja) {
        return cajaRepository.save(caja);
    }

    // Métodos adicionales, si son necesarios
    // Por ejemplo, buscar por estado o algún campo específico
    /*
    public List<Caja> obtenerPorEstado(String estado) {
        return cajaRepository.findByEstado(estado); // Asumiendo que existe este metodo en el repositorio
    }
    */
}
