package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.modelo.Sucursal;
import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.UsuarioRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.AlmacenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Servicio para obtener el contexto del usuario autenticado.
 * Proporciona métodos para obtener la sucursal y almacén del usuario logueado.
 */
@Service
public class UsuarioContextService {

    private final UsuarioRepository usuarioRepository;
    private final AlmacenRepository almacenRepository;

    public UsuarioContextService(UsuarioRepository usuarioRepository,
                                AlmacenRepository almacenRepository) {
        this.usuarioRepository = usuarioRepository;
        this.almacenRepository = almacenRepository;
    }

    /**
     * Obtiene el usuario autenticado actual.
     * 
     * @return El usuario autenticado
     * @throws UsernameNotFoundException si no se encuentra el usuario
     */
    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        String nombreUsuario = authentication.getName();
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + nombreUsuario));
    }

    /**
     * Obtiene el trabajador asociado al usuario autenticado.
     * 
     * @return El trabajador del usuario autenticado
     * @throws UsernameNotFoundException si no se encuentra el trabajador
     */
    public Trabajador obtenerTrabajadorAutenticado() {
        Usuario usuario = obtenerUsuarioAutenticado();
        if (usuario.getTrabajador() == null) {
            throw new UsernameNotFoundException("El usuario no tiene un trabajador asociado");
        }
        return usuario.getTrabajador();
    }

    /**
     * Obtiene la sucursal del usuario autenticado.
     * 
     * @return La sucursal del usuario autenticado
     * @throws UsernameNotFoundException si no se encuentra la sucursal
     */
    public Sucursal obtenerSucursalUsuario() {
        Trabajador trabajador = obtenerTrabajadorAutenticado();
        if (trabajador.getSucursal() == null) {
            throw new UsernameNotFoundException("El trabajador no tiene una sucursal asignada");
        }
        return trabajador.getSucursal();
    }

    /**
     * Obtiene el almacén de la sucursal del usuario autenticado.
     * 
     * @return El almacén de la sucursal del usuario
     * @throws UsernameNotFoundException si no se encuentra el almacén
     */
    public Almacen obtenerAlmacenUsuario() {
        Sucursal sucursal = obtenerSucursalUsuario();
        
        // Buscar cualquier almacén de la sucursal del usuario
        var almacenes = almacenRepository.findBySucursalIdSucursal(sucursal.getIdSucursal());
        if (!almacenes.isEmpty()) {
            return almacenes.get(0);
        }
        
        throw new UsernameNotFoundException("No se encontró un almacén para la sucursal: " + sucursal.getNombre());
    }

    /**
     * Obtiene el ID de la sucursal del usuario autenticado.
     * 
     * @return El ID de la sucursal
     */
    public Integer obtenerIdSucursalUsuario() {
        return obtenerSucursalUsuario().getIdSucursal();
    }

    /**
     * Obtiene el ID del almacén del usuario autenticado.
     * 
     * @return El ID del almacén
     */
    public Integer obtenerIdAlmacenUsuario() {
        return obtenerAlmacenUsuario().getIdAlmacen();
    }

    /**
     * Obtiene el nombre de la sucursal del usuario autenticado.
     * 
     * @return El nombre de la sucursal
     */
    public String obtenerNombreSucursalUsuario() {
        return obtenerSucursalUsuario().getNombre();
    }

    /**
     * Obtiene el nombre del almacén del usuario autenticado.
     * 
     * @return El nombre del almacén
     */
    public String obtenerNombreAlmacenUsuario() {
        return obtenerAlmacenUsuario().getNombre();
    }

    /**
     * Verifica si el usuario autenticado pertenece a una sucursal específica.
     * 
     * @param idSucursal El ID de la sucursal a verificar
     * @return true si el usuario pertenece a la sucursal, false en caso contrario
     */
    public boolean usuarioPerteneceASucursal(Integer idSucursal) {
        try {
            Sucursal sucursalUsuario = obtenerSucursalUsuario();
            return sucursalUsuario.getIdSucursal().equals(idSucursal);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el usuario autenticado pertenece a un almacén específico.
     * 
     * @param idAlmacen El ID del almacén a verificar
     * @return true si el usuario pertenece al almacén, false en caso contrario
     */
    public boolean usuarioPerteneceAAlmacen(Integer idAlmacen) {
        try {
            Almacen almacenUsuario = obtenerAlmacenUsuario();
            return almacenUsuario.getIdAlmacen().equals(idAlmacen);
        } catch (Exception e) {
            return false;
        }
    }
} 