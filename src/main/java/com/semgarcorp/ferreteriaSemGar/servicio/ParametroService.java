package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import com.semgarcorp.ferreteriaSemGar.repositorio.ParametroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ParametroService {

    private final ParametroRepository parametroRepositorio;

    public ParametroService(ParametroRepository parametroRepositorio) {
        this.parametroRepositorio = parametroRepositorio;
    }

    public Parametro obtenerParametroPorClave(String clave) {
        return parametroRepositorio.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("No se encontró el parámetro con clave: " + clave));
    }

    public BigDecimal obtenerValorPorClave(String clave) {
        Parametro parametro = obtenerParametroPorClave(clave);
        return new BigDecimal(parametro.getValor());
    }

    public BigDecimal obtenerValorIGV() {
        Parametro parametroIGV = obtenerParametroPorClave("IGV");
        String valorIGV = parametroIGV.getValor();

        try {
            BigDecimal igv = new BigDecimal(valorIGV);
            if (igv.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("El valor del IGV debe ser mayor que 0");
            }
            return igv.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El valor del IGV no es válido: " + valorIGV);
        }
    }

    public BigDecimal obtenerValorUtilidad() {
        Parametro parametroUtilidad = obtenerParametroPorClave("UTILIDAD");
        String valorUtilidad = parametroUtilidad.getValor();

        try {
            BigDecimal utilidad = new BigDecimal(valorUtilidad);
            if (utilidad.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("El valor de utilidad debe ser mayor que 0");
            }
            return utilidad.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El valor de utilidad no es válido: " + valorUtilidad);
        }
    }

    public List<Parametro> listar() {
        return parametroRepositorio.findAll();
    }

    public Parametro guardar(Parametro parametro) {
        return parametroRepositorio.save(parametro);
    }

    public Parametro actualizar(Parametro parametro) {
        return parametroRepositorio.save(parametro);
    }

    public void eliminar(String clave) {
        parametroRepositorio.deleteByClave(clave);
    }
}