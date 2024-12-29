package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoPago;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoPagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoPagoService {

    private final TipoPagoRepository tipoPagoRepositorio;

    // Constructor para inyectar el repositorio
    public TipoPagoService(TipoPagoRepository tipoPagoRepositorio) {
        this.tipoPagoRepositorio = tipoPagoRepositorio;
    }

    // Listar todos los tipos de pago
    public List<TipoPago> listar() {
        return tipoPagoRepositorio.findAll();
    }

    // Obtener un tipo de pago por su ID
    public TipoPago obtenerPorId(Integer id) {
        Optional<TipoPago> tipoPago = tipoPagoRepositorio.findById(id);
        return tipoPago.orElse(null);  // Retorna null si no se encuentra el tipo de pago
    }

    // Guardar un nuevo tipo de pago o actualiza uno existente
    public TipoPago guardar(TipoPago tipoPago) {
        return tipoPagoRepositorio.save(tipoPago);
    }

    // Eliminar un tipo de pago por su ID
    public void eliminar(Integer id) {
        tipoPagoRepositorio.deleteById(id);
    }

    public TipoPago actualizar(TipoPago tipoPago) {
        return tipoPagoRepositorio.save(tipoPago);
    }
}
