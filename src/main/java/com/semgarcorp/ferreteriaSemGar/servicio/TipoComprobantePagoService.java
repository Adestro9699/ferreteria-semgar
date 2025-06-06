package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoComprobantePago;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoComprobantePagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoComprobantePagoService {

    private final TipoComprobantePagoRepository tipoComprobantePagoRepositorio;

    public TipoComprobantePagoService(TipoComprobantePagoRepository tipoComprobantePagoRepository) {
        this.tipoComprobantePagoRepositorio = tipoComprobantePagoRepository;
    }

    public List<TipoComprobantePago> listar() {
        return tipoComprobantePagoRepositorio.findAll();
    }

    public TipoComprobantePago obtenerPorId(Integer id) {
        return tipoComprobantePagoRepositorio.findById(id).orElse(null);
    }

    public TipoComprobantePago guardar(TipoComprobantePago tipoComprobantePago) {
        return tipoComprobantePagoRepositorio.save(tipoComprobantePago);
    }

    public TipoComprobantePago actualizar(TipoComprobantePago tipoComprobantePago) {
        return tipoComprobantePagoRepositorio.save(tipoComprobantePago);
    }

    @Transactional
    public void eliminar(Integer id) {
        tipoComprobantePagoRepositorio.deleteById(id); // Propaga DataIntegrityViolationException
    }
}
