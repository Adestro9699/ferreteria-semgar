package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.repositorio.CajaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepositorio;

    // Constructor con inyección del repositorio
    public CajaService(CajaRepository cajaRepositorio) {
        this.cajaRepositorio = cajaRepositorio;
    }

    // Listar todos los registros de caja
    public List<Caja> listar() {
        return cajaRepositorio.findAll();
    }

    // Obtener un registro de caja por ID
    public Caja obtenerPorId(Integer id) {
        return cajaRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo registro de caja o actualiza uno existente
    public Caja guardar(Caja caja) {
        // Calcular saldoFinal antes de guardar
        BigDecimal saldoFinal = calcularSaldoFinal(caja);
        caja.setSaldoFinal(saldoFinal);
        return cajaRepositorio.save(caja);
    }

    // Actualizar un registro de caja existente
    public Caja actualizar(Caja caja) {
        // Calcular saldoFinal antes de actualizar
        BigDecimal saldoFinal = calcularSaldoFinal(caja);
        caja.setSaldoFinal(saldoFinal);
        return cajaRepositorio.save(caja);
    }

    // Eliminar un registro de caja por ID
    public void eliminar(Integer id) {
        cajaRepositorio.deleteById(id);
    }

    // Metodo para calcular el saldoFinal
    private BigDecimal calcularSaldoFinal(Caja caja) {
        return caja.getSaldoInicial().add(caja.getEntradas()).subtract(caja.getSalidas());
    }
}
