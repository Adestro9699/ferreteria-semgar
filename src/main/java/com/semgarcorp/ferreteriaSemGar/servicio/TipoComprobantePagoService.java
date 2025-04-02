package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.TipoComprobantePago;
import com.semgarcorp.ferreteriaSemGar.repositorio.TipoComprobantePagoRepository;
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

    public void eliminar(Integer id) {
        tipoComprobantePagoRepositorio.deleteById(id);
    }

    public Integer getCodigoNubefact(String nombreTipo) {
        return tipoComprobantePagoRepositorio.findByNombre(nombreTipo)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de comprobante no existe"))
                .getCodigoNubefact();
    }
}
