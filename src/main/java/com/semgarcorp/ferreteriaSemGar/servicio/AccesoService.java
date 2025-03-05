package com.semgarcorp.ferreteriaSemGar.servicio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccesoService {

    private final AccesoRepository accesoRepositorio;

    // Constructor con inyección del repositorio
    public AccesoService(AccesoRepository accesoRepositorio) {
        this.accesoRepositorio = accesoRepositorio;
    }

    // Listar todos los accesos
    public List<Acceso> listar() {
        return accesoRepositorio.findAll();
    }

    // Obtener un acceso por ID
    public Acceso obtenerPorId(Integer id) {
        return accesoRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo acceso o actualizar uno existente
    public Acceso guardar(Acceso acceso) {
        return accesoRepositorio.save(acceso);
    }

    // Actualizar un acceso existente
    public Acceso actualizar(Acceso acceso) {
        return accesoRepositorio.save(acceso);
    }

    // Eliminar un acceso por ID
    public void eliminar(Integer id) {
        accesoRepositorio.deleteById(id);
    }

    /**
     * Actualizar los permisos de un acceso específico.
     *
     * @param idAcceso      El ID del acceso a actualizar.
     * @param nuevosPermisos Un mapa con los nuevos permisos (clave: funcionalidad, valor: booleano).
     * @return El acceso actualizado.
     */
    public Acceso actualizarPermisosYRol(Integer idAcceso, String nuevoRol, Map<String, Boolean> nuevosPermisos) {
        // Buscar el acceso por su ID
        Acceso acceso = accesoRepositorio.findById(idAcceso)
                .orElseThrow(() -> new RuntimeException("Acceso no encontrado"));

        // Actualizar el rol
        acceso.setRol(nuevoRol);

        // Convertir los permisos a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String permisosJson = objectMapper.writeValueAsString(nuevosPermisos);
            acceso.setPermisosJson(permisosJson); // Actualizar el campo `permisosJson`
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al procesar los permisos", e);
        }

        // Guardar el acceso actualizado en la base de datos
        return accesoRepositorio.save(acceso);
    }
}