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

    // Constructor
    public ParametroService(ParametroRepository parametroRepositorio) {
        this.parametroRepositorio = parametroRepositorio;
    }

    public BigDecimal obtenerValorPorClave(String clave) {
        Parametro parametro = parametroRepositorio.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("No se encontró el parámetro con clave: " + clave));
        return new BigDecimal(parametro.getValor()); // Convertir el valor a BigDecimal
    }

    public BigDecimal obtenerValorIGV() {
        // Buscar el parámetro IGV en la base de datos
        Parametro parametroIGV = parametroRepositorio.findByClave("IGV")
                .orElseThrow(() -> new IllegalStateException("No se encontró el parámetro IGV en la base de datos"));

        // Obtener el valor del IGV como String
        String valorIGV = parametroIGV.getValor();

        try {
            // Convertir el valor a BigDecimal
            BigDecimal igv = new BigDecimal(valorIGV);

            // Validar que el IGV sea un valor positivo
            if (igv.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("El valor del IGV debe ser mayor que 0");
            }

            // Convertir el IGV a formato decimal (por ejemplo, 18 -> 0.18)
            return igv.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El valor del IGV no es un número válido: " + valorIGV);
        }
    }

    /**
     * Método para obtener el valor de la utilidad (ganancia) desde la tabla de parámetros.
     * El valor debe estar almacenado en formato decimal (por ejemplo, 30 -> 0.30).
     */
    public BigDecimal obtenerValorUtilidad() {
        // Buscar el parámetro UTILIDAD en la base de datos
        Parametro parametroUtilidad = parametroRepositorio.findByClave("UTILIDAD")
                .orElseThrow(() -> new IllegalStateException("No se encontró el parámetro UTILIDAD en la base de datos"));

        // Obtener el valor de la utilidad como String
        String valorUtilidad = parametroUtilidad.getValor();

        try {
            // Convertir el valor a BigDecimal
            BigDecimal utilidad = new BigDecimal(valorUtilidad);

            // Validar que la utilidad sea un valor positivo
            if (utilidad.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("El valor de la utilidad debe ser mayor que 0");
            }

            // Convertir la utilidad a formato decimal (por ejemplo, 30 -> 0.30)
            return utilidad.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El valor de la utilidad no es un número válido: " + valorUtilidad);
        }
    }

    public List<Parametro> listar() {
        return parametroRepositorio.findAll();
    }

    public Parametro guardar(Parametro parametro) {
        return parametroRepositorio.save(parametro);
    }

    public Parametro actualizar(Parametro parametro) {
        // Verifica si el parámetro ya existe
        Parametro parametroExistente = parametroRepositorio.findByClave(parametro.getClave()).orElse(null);
        if (parametroExistente != null) {
            // Actualiza el valor del parámetro existente
            parametroExistente.setValor(parametro.getValor());
            return parametroRepositorio.save(parametroExistente);
        }
        return null; // Si no existe, no se actualiza
    }

    public void eliminar(String clave) {
        parametroRepositorio.deleteByClave(clave);
    }
}