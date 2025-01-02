package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.CierreCaja;
import com.semgarcorp.ferreteriaSemGar.repositorio.CierreCajaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CierreCajaService {

    private final CierreCajaRepository cierreCajaRepositorio;

    // Constructor con inyección del repositorio
    public CierreCajaService(CierreCajaRepository cierreCajaRepositorio) {
        this.cierreCajaRepositorio = cierreCajaRepositorio;
    }

    public List<CierreCaja> listar() {
        return cierreCajaRepositorio.findAll();
    }

    public CierreCaja obtenerPorId(Integer id) {
        return cierreCajaRepositorio.findById(id).orElse(null);
    }

    public CierreCaja guardar(CierreCaja acceso) {
        return cierreCajaRepositorio.save(acceso);
    }

    public CierreCaja actualizar(CierreCaja acceso) {
        return cierreCajaRepositorio.save(acceso);
    }

    public void eliminar(Integer id) {
        cierreCajaRepositorio.deleteById(id);
    }
}