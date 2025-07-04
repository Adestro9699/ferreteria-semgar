package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.AlmacenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenService(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    /**
     * Lista todos los almacenes disponibles.
     *
     * @return Una lista de todos los almacenes.
     */
    public List<Almacen> listar() {
        return almacenRepository.findAll();
    }

    /**
     * Obtiene un almacén por su ID.
     *
     * @param id El ID del almacén a buscar.
     * @return El almacén encontrado, o null si no existe.
     */
    public Almacen obtenerPorId(Integer id) {
        return almacenRepository.findById(id).orElse(null);
    }

    /**
     * Guarda un nuevo almacén en la base de datos.
     *
     * @param almacen El almacén a guardar.
     * @return El almacén guardado.
     */
    public Almacen guardar(Almacen almacen) {
        // Validaciones de lógica de negocio
        validarAlmacen(almacen);
        
        // Establecer fecha de creación si no está establecida
        if (almacen.getFechaCreacion() == null) {
            almacen.setFechaCreacion(LocalDate.now());
        }
        
        // Establecer estado activo por defecto si no está establecido
        if (almacen.getEstadoAlmacen() == null) {
            almacen.setEstadoAlmacen(Almacen.EstadoAlmacen.ACTIVO);
        }
        
        // Establecer esPrincipal por defecto si no está establecido
        if (almacen.getEsPrincipal() == null) {
            almacen.setEsPrincipal(false);
        }
        
        return almacenRepository.save(almacen);
    }

    /**
     * Actualiza un almacén existente en la base de datos.
     *
     * @param almacen El almacén a actualizar.
     * @return El almacén actualizado.
     */
    public Almacen actualizar(Almacen almacen) {
        // Validaciones de lógica de negocio
        validarAlmacen(almacen);
        
        // Establecer fecha de modificación
        almacen.setFechaModificacion(LocalDate.now());
        
        return almacenRepository.save(almacen);
    }

    /**
     * Elimina un almacén por su ID.
     *
     * @param id El ID del almacén a eliminar.
     */
    public void eliminar(Integer id) {
        almacenRepository.deleteById(id);
    }

    /**
     * Busca almacenes por tipo (principal o de sucursal).
     *
     * @param esPrincipal true para almacenes principales, false para almacenes de sucursal.
     * @return Lista de almacenes del tipo especificado.
     */
    public List<Almacen> buscarPorTipo(Boolean esPrincipal) {
        return almacenRepository.findByEsPrincipal(esPrincipal);
    }

    /**
     * Busca almacenes por estado.
     *
     * @param estadoAlmacen El estado del almacén a buscar.
     * @return Lista de almacenes con el estado especificado.
     */
    public List<Almacen> buscarPorEstado(Almacen.EstadoAlmacen estadoAlmacen) {
        return almacenRepository.findByEstadoAlmacen(estadoAlmacen);
    }

    /**
     * Busca almacenes por sucursal.
     *
     * @param idSucursal El ID de la sucursal.
     * @return Lista de almacenes de la sucursal especificada.
     */
    public List<Almacen> buscarPorSucursal(Integer idSucursal) {
        return almacenRepository.findBySucursalIdSucursal(idSucursal);
    }

    /**
     * Busca almacenes principales (esPrincipal = true).
     *
     * @return Lista de almacenes principales.
     */
    public List<Almacen> buscarAlmacenesPrincipales() {
        return almacenRepository.findByEsPrincipalTrue();
    }

    /**
     * Busca almacenes de sucursal (esPrincipal = false).
     *
     * @return Lista de almacenes de sucursal.
     */
    public List<Almacen> buscarAlmacenesSucursal() {
        return almacenRepository.findByEsPrincipalFalse();
    }

    /**
     * Busca almacenes por nombre.
     *
     * @param nombre El nombre del almacén a buscar.
     * @return Lista de almacenes que contienen el nombre especificado.
     */
    public List<Almacen> buscarPorNombre(String nombre) {
        return almacenRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene el almacén principal de una sucursal específica.
     *
     * @param idSucursal El ID de la sucursal.
     * @return El almacén principal de la sucursal, o null si no existe.
     */
    public Almacen obtenerAlmacenPrincipalDeSucursal(Integer idSucursal) {
        List<Almacen> almacenesPrincipales = almacenRepository.findBySucursalIdSucursalAndEsPrincipalTrue(idSucursal);
        return almacenesPrincipales.isEmpty() ? null : almacenesPrincipales.get(0);
    }

    /**
     * Verifica si una sucursal tiene un almacén principal.
     *
     * @param idSucursal El ID de la sucursal.
     * @return true si la sucursal tiene un almacén principal, false en caso contrario.
     */
    public boolean tieneAlmacenPrincipal(Integer idSucursal) {
        return obtenerAlmacenPrincipalDeSucursal(idSucursal) != null;
    }

    /**
     * Valida la lógica de negocio de un almacén.
     *
     * @param almacen El almacén a validar.
     * @throws IllegalArgumentException Si la validación falla.
     */
    private void validarAlmacen(Almacen almacen) {
        // Validar que SIEMPRE tenga sucursal (tanto principal como de sucursal)
        if (almacen.getSucursal() == null) {
            throw new IllegalArgumentException("Todo almacén debe tener una sucursal asignada");
        }
        
        // Validar que si es principal, no haya otro almacén principal en la misma sucursal
        if (Boolean.TRUE.equals(almacen.getEsPrincipal())) {
            List<Almacen> almacenesPrincipalesEnSucursal = almacenRepository.findBySucursalIdSucursalAndEsPrincipalTrue(almacen.getSucursal().getIdSucursal());
            
            // Si es un almacén existente, excluirlo de la validación
            if (almacen.getIdAlmacen() != null) {
                almacenesPrincipalesEnSucursal = almacenesPrincipalesEnSucursal.stream()
                    .filter(a -> !a.getIdAlmacen().equals(almacen.getIdAlmacen()))
                    .collect(Collectors.toList());
            }
            
            if (!almacenesPrincipalesEnSucursal.isEmpty()) {
                throw new IllegalArgumentException("Ya existe un almacén principal en la sucursal " + 
                    almacen.getSucursal().getNombre() + ". Solo puede haber un almacén principal por sucursal.");
            }
        }
    }
} 