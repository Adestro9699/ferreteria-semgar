package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import com.semgarcorp.ferreteriaSemGar.repositorio.ParametroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ParametroService {

    private final ParametroRepository parametroRepositorio;

    // Constructor
    public ParametroService(ParametroRepository parametroRepositorio) {
        this.parametroRepositorio = parametroRepositorio;
    }

    /**
     * Obtiene todos los parámetros.
     *
     * @return Lista de parámetros.
     */
    public List<Parametro> listar() {
        return parametroRepositorio.findAll();
    }

    public BigDecimal obtenerValorPorClave(String clave) {
        Parametro parametro = parametroRepositorio.findByClave(clave)
                .orElseThrow(() -> new RuntimeException("No se encontró el parámetro con clave: " + clave));
        return new BigDecimal(parametro.getValor()); // Convertir el valor a BigDecimal
    }

    public BigDecimal obtenerValorIGV() {
        Parametro parametroIGV = parametroRepositorio.findByClave("IGV")
                .orElseThrow(() -> new RuntimeException("No se encontró el parámetro IGV"));
        return new BigDecimal(parametroIGV.getValor());
    }

    /**
     * Guarda un nuevo parámetro.
     *
     * @param parametro El parámetro a guardar.
     * @return El parámetro guardado.
     */
    public Parametro guardar(Parametro parametro) {
        return parametroRepositorio.save(parametro);
    }

    /**
     * Actualiza un parámetro existente.
     *
     * @param parametro El parámetro a actualizar.
     * @return El parámetro actualizado.
     */
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

    /**
     * Elimina un parámetro por su clave.
     *
     * @param clave La clave del parámetro a eliminar.
     */
    public void eliminar(String clave) {
        parametroRepositorio.deleteByClave(clave);
    }
}