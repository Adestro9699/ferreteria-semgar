package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.MovimientoCaja;
import com.semgarcorp.ferreteriaSemGar.repositorio.MovimientoCajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimientoCajaService {

    private final MovimientoCajaRepository movimientoCajaRepositorio;

    public MovimientoCajaService(MovimientoCajaRepository movimientoCajaRepositorio) {
        this.movimientoCajaRepositorio = movimientoCajaRepositorio;
    }

    public List<MovimientoCaja> listar() {
        return movimientoCajaRepositorio.findAll();
    }

    public MovimientoCaja obtenerPorId(Integer id) {
        return movimientoCajaRepositorio.findById(id).orElse(null);
    }

    public MovimientoCaja guardar(MovimientoCaja movimientoCaja) {
        return movimientoCajaRepositorio.save(movimientoCaja);
    }

    public MovimientoCaja actualizar(MovimientoCaja movimientoCaja) {
        return movimientoCajaRepositorio.save(movimientoCaja);
    }

    public void eliminar(Integer id) {
        movimientoCajaRepositorio.deleteById(id);
    }
}