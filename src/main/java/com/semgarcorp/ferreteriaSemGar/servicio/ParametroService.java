package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import com.semgarcorp.ferreteriaSemGar.repositorio.ParametroRepository;
import org.springframework.stereotype.Service;

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

    /**
     * Obtiene un parámetro por su ID.
     *
     * @param id El ID del parámetro.
     * @return El parámetro si existe, o null si no se encuentra.
     */
    public Parametro obtenerPorId(Integer id) {
        return parametroRepositorio.findById(id).orElse(null);
    }

    /**
     * Obtiene un parámetro por su clave.
     *
     * @param clave La clave del parámetro (ej: "IGV", "DESCUENTO_VENDEDOR").
     * @return El parámetro si existe, o null si no se encuentra.
     */
    public Parametro obtenerPorClave(String clave) {
        return parametroRepositorio.findByClave(clave).orElse(null);
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
        return parametroRepositorio.save(parametro);
    }

    /**
     * Elimina un parámetro por su ID.
     *
     * @param id El ID del parámetro a eliminar.
     */
    public void eliminar(Integer id) {
        parametroRepositorio.deleteById(id);
    }
}