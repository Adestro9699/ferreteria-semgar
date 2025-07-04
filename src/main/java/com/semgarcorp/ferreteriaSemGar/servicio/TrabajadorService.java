package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.modelo.Acceso;
import com.semgarcorp.ferreteriaSemGar.repositorio.TrabajadorRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.AccesoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepositorio;
    private final UsuarioRepository usuarioRepository;
    private final AccesoRepository accesoRepository;

    public TrabajadorService(TrabajadorRepository trabajadorRepositorio,
                            UsuarioRepository usuarioRepository,
                            AccesoRepository accesoRepository) {
        this.trabajadorRepositorio = trabajadorRepositorio;
        this.usuarioRepository = usuarioRepository;
        this.accesoRepository = accesoRepository;
    }

    public List<Trabajador> listar() {
        return trabajadorRepositorio.findAll();
    }

    public Trabajador obtenerPorId(Integer id) {
        return trabajadorRepositorio.findById(id).orElse(null);
    }

    public Trabajador guardar(Trabajador trabajador) {
        return trabajadorRepositorio.save(trabajador);
    }

    public Trabajador actualizar(Trabajador trabajador) {
        return trabajadorRepositorio.save(trabajador);
    }

    /**
     * Elimina un trabajador y todos sus datos relacionados (usuario y acceso).
     * Esta operación se ejecuta en una transacción para garantizar la integridad de los datos.
     * 
     * @param id ID del trabajador a eliminar
     * @throws RuntimeException si el trabajador no existe o si hay errores durante la eliminación
     */
    @Transactional
    public void eliminar(Integer id) {
        eliminarConCascada(id, true);
    }

    /**
     * Elimina un trabajador con opción de eliminar o no los datos relacionados.
     * 
     * @param id ID del trabajador a eliminar
     * @param eliminarRelacionados true para eliminar usuario y acceso, false para eliminar solo trabajador
     * @throws RuntimeException si el trabajador no existe o si hay errores durante la eliminación
     */
    @Transactional
    public void eliminarConCascada(Integer id, boolean eliminarRelacionados) {
        // Verificar que el trabajador existe
        Trabajador trabajador = trabajadorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con ID: " + id));

        if (eliminarRelacionados) {
            // Buscar el usuario asociado al trabajador
            Optional<Usuario> usuarioOpt = usuarioRepository.findByTrabajador(trabajador);

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                
                // Buscar el acceso asociado al usuario
                Optional<Acceso> accesoOpt = accesoRepository.findByUsuario(usuario);

                // Eliminar en orden: Acceso -> Usuario -> Trabajador
                if (accesoOpt.isPresent()) {
                    Acceso acceso = accesoOpt.get();
                    System.out.println("Eliminando acceso ID: " + acceso.getIdAcceso() + " para usuario: " + usuario.getNombreUsuario());
                    accesoRepository.delete(acceso);
                }

                System.out.println("Eliminando usuario ID: " + usuario.getIdUsuario() + " - " + usuario.getNombreUsuario());
                usuarioRepository.delete(usuario);
            } else {
                System.out.println("No se encontró usuario asociado al trabajador ID: " + id);
            }
        } else {
            System.out.println("Eliminando solo trabajador (sin eliminar usuario/acceso) ID: " + id);
        }

        System.out.println("Eliminando trabajador ID: " + id + " - " + trabajador.getNombreTrabajador() + " " + trabajador.getApellidoTrabajador());
        trabajadorRepositorio.deleteById(id);
    }

    /**
     * Elimina solo el trabajador sin eliminar el usuario ni el acceso.
     * Útil cuando se quiere mantener el usuario pero eliminar el trabajador.
     * 
     * @param id ID del trabajador a eliminar
     * @throws RuntimeException si el trabajador no existe
     */
    @Transactional
    public void eliminarSoloTrabajador(Integer id) {
        eliminarConCascada(id, false);
    }

    public Optional<Trabajador> buscarPorId(Integer id) {
        return trabajadorRepositorio.findById(id);
    }
}
