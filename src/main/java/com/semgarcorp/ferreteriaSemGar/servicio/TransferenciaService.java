package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Transferencia;
import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;
import com.semgarcorp.ferreteriaSemGar.modelo.Trabajador;
import com.semgarcorp.ferreteriaSemGar.modelo.Almacen;
import com.semgarcorp.ferreteriaSemGar.repositorio.TransferenciaRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.AlmacenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Service
public class TransferenciaService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransferenciaService.class);
    
    private final TransferenciaRepository transferenciaRepositorio;
    private final DetalleTransferenciaService detalleTransferenciaService;
    private final ProductoAlmacenService productoAlmacenService;
    private final UsuarioContextService usuarioContextService;
    private final AlmacenRepository almacenRepository;
    
    public TransferenciaService(TransferenciaRepository transferenciaRepositorio,
                               DetalleTransferenciaService detalleTransferenciaService,
                               ProductoAlmacenService productoAlmacenService,
                               UsuarioContextService usuarioContextService,
                               AlmacenRepository almacenRepository) {
        this.transferenciaRepositorio = transferenciaRepositorio;
        this.detalleTransferenciaService = detalleTransferenciaService;
        this.productoAlmacenService = productoAlmacenService;
        this.usuarioContextService = usuarioContextService;
        this.almacenRepository = almacenRepository;
    }

    /**
     * Listar todas las transferencias
     */
    public List<Transferencia> listar() {
        return transferenciaRepositorio.findAll();
    }

    /**
     * Obtener una transferencia por su ID
     */
    public Transferencia obtenerPorId(Integer id) {
        return transferenciaRepositorio.findById(id).orElse(null);
    }

    /**
     * Guardar una nueva transferencia
     */
    @Transactional
    public Transferencia guardar(Transferencia transferencia) {
        logger.info("Iniciando guardado de transferencia");
        
        // Establecer valores por defecto
        if (transferencia.getFechaCreacion() == null) {
            transferencia.setFechaCreacion(LocalDate.now());
        }
        if (transferencia.getEstadoTransferencia() == null) {
            transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.PENDIENTE);
        }
        
        logger.info("Transferencia - Origen: {}, Destino: {}, Estado: {}", 
                   transferencia.getAlmacenOrigen() != null ? transferencia.getAlmacenOrigen().getIdAlmacen() : "null",
                   transferencia.getAlmacenDestino() != null ? transferencia.getAlmacenDestino().getIdAlmacen() : "null",
                   transferencia.getEstadoTransferencia());
        
        // Guardar los detalles temporalmente
        List<DetalleTransferencia> detallesTemporales = null;
        if (transferencia.getDetalles() != null && !transferencia.getDetalles().isEmpty()) {
            detallesTemporales = new ArrayList<>(transferencia.getDetalles());
            transferencia.setDetalles(new ArrayList<>()); // Limpiar para evitar cascade
        }
        
        // Guardar la transferencia primero para obtener el ID
        Transferencia transferenciaGuardada = transferenciaRepositorio.save(transferencia);
        logger.info("Transferencia guardada con ID: {}", transferenciaGuardada.getIdTransferencia());
        
        // Procesar los detalles después de tener el ID de la transferencia
        if (detallesTemporales != null && !detallesTemporales.isEmpty()) {
            for (DetalleTransferencia detalle : detallesTemporales) {
                detalle.setTransferencia(transferenciaGuardada);
                detalleTransferenciaService.guardar(detalle);
            }
            
            // Recargar la transferencia con los detalles
            transferenciaGuardada = transferenciaRepositorio.findById(transferenciaGuardada.getIdTransferencia()).orElse(transferenciaGuardada);
        }
        
        logger.info("Transferencia {} guardada exitosamente", transferenciaGuardada.getIdTransferencia());
        
        return transferenciaGuardada;
    }
    
    /**
     * Crear una nueva transferencia con el almacén origen automático del usuario autenticado
     * 
     * @param transferencia La transferencia a crear (sin almacén origen)
     * @return La transferencia creada con el almacén origen establecido
     */
    @Transactional
    public Transferencia crearTransferenciaConOrigenAutomatico(Transferencia transferencia) {
        logger.info("Creando transferencia con origen automático");
        
        try {
            // Obtener el almacén del usuario autenticado
            Almacen almacenOrigen = usuarioContextService.obtenerAlmacenUsuario();
            Trabajador trabajadorOrigen = usuarioContextService.obtenerTrabajadorAutenticado();
            
            logger.info("Almacén origen automático: {} (ID: {})", 
                       almacenOrigen.getNombre(), almacenOrigen.getIdAlmacen());
            logger.info("Trabajador origen: {} {} (ID: {})", 
                       trabajadorOrigen.getNombreTrabajador(), 
                       trabajadorOrigen.getApellidoTrabajador(), 
                       trabajadorOrigen.getIdTrabajador());
            
            // Establecer el almacén origen y trabajador origen automáticamente
            transferencia.setAlmacenOrigen(almacenOrigen);
            transferencia.setTrabajadorOrigen(trabajadorOrigen);
            
            // Establecer valores por defecto
            if (transferencia.getFechaCreacion() == null) {
                transferencia.setFechaCreacion(LocalDate.now());
            }
            if (transferencia.getEstadoTransferencia() == null) {
                transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.PENDIENTE);
            }
            
            // Guardar la transferencia
            return guardar(transferencia);
            
        } catch (Exception e) {
            logger.error("Error al crear transferencia con origen automático: {}", e.getMessage());
            throw new RuntimeException("Error al crear transferencia: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtener transferencias emitidas por el usuario autenticado (como almacén origen)
     * 
     * @return Lista de transferencias emitidas por el usuario
     */
    public List<Transferencia> obtenerTransferenciasEmitidasPorUsuario() {
        try {
            Integer idAlmacenUsuario = usuarioContextService.obtenerIdAlmacenUsuario();
            logger.info("Obteniendo transferencias emitidas por almacén: {}", idAlmacenUsuario);
            
            List<Transferencia> transferencias = transferenciaRepositorio.findByAlmacenOrigenIdAlmacen(idAlmacenUsuario);
            logger.info("Se encontraron {} transferencias emitidas", transferencias.size());
            
            return transferencias;
        } catch (Exception e) {
            logger.error("Error al obtener transferencias emitidas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener transferencias emitidas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Obtener transferencias recibidas por el usuario autenticado (como almacén destino)
     * 
     * @return Lista de transferencias recibidas por el usuario
     */
    public List<Transferencia> obtenerTransferenciasRecibidasPorUsuario() {
        try {
            Integer idAlmacenUsuario = usuarioContextService.obtenerIdAlmacenUsuario();
            logger.info("Obteniendo transferencias recibidas por almacén: {}", idAlmacenUsuario);
            
            List<Transferencia> transferencias = transferenciaRepositorio.findByAlmacenDestinoIdAlmacen(idAlmacenUsuario);
            logger.info("Se encontraron {} transferencias recibidas", transferencias.size());
            
            return transferencias;
        } catch (Exception e) {
            logger.error("Error al obtener transferencias recibidas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener transferencias recibidas: " + e.getMessage(), e);
        }
    }
    
    /**
     * Aceptar una transferencia (establecer trabajador destino y cambiar estado)
     * 
     * @param idTransferencia ID de la transferencia a aceptar
     * @return La transferencia actualizada
     */
    @Transactional
    public Transferencia aceptarTransferencia(Integer idTransferencia) {
        logger.info("Aceptando transferencia: {}", idTransferencia);
        
        Transferencia transferencia = obtenerPorId(idTransferencia);
        if (transferencia == null) {
            throw new IllegalArgumentException("Transferencia no encontrada");
        }
        
        // Verificar que la transferencia esté pendiente
        if (transferencia.getEstadoTransferencia() != Transferencia.EstadoTransferencia.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aceptar transferencias en estado PENDIENTE");
        }
        
        // Verificar que el usuario autenticado sea el almacén destino
        Integer idAlmacenUsuario = usuarioContextService.obtenerIdAlmacenUsuario();
        if (!transferencia.getAlmacenDestino().getIdAlmacen().equals(idAlmacenUsuario)) {
            throw new IllegalStateException("Solo el almacén destino puede aceptar la transferencia");
        }
        
        try {
            // Establecer el trabajador destino
            Trabajador trabajadorDestino = usuarioContextService.obtenerTrabajadorAutenticado();
            transferencia.setTrabajadorDestino(trabajadorDestino);
            
            // Cambiar estado a EN_PROCESO
            transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.EN_PROCESO);
            transferencia.setFechaModificacion(LocalDate.now());
            
            Transferencia transferenciaActualizada = transferenciaRepositorio.save(transferencia);
            logger.info("Transferencia {} aceptada exitosamente", idTransferencia);
            
            return transferenciaActualizada;
            
        } catch (Exception e) {
            logger.error("Error al aceptar transferencia: {}", e.getMessage());
            throw new RuntimeException("Error al aceptar transferencia: " + e.getMessage(), e);
        }
    }
    
    /**
     * Actualizar una transferencia existente
     */
    @Transactional
    public Transferencia actualizar(Transferencia transferencia) {
        transferencia.setFechaModificacion(LocalDate.now());
        return transferenciaRepositorio.save(transferencia);
    }

    /**
     * Eliminar una transferencia por su ID
     */
    @Transactional
    public void eliminar(Integer id) {
        transferenciaRepositorio.deleteById(id);
    }
    
    /**
     * Ejecutar una transferencia (cambiar estado a COMPLETADA y mover stock)
     */
    @Transactional
    public Transferencia ejecutarTransferencia(Integer idTransferencia) {
        logger.info("Iniciando ejecución de transferencia ID: {}", idTransferencia);
        
        Transferencia transferencia = obtenerPorId(idTransferencia);
        
        if (transferencia == null) {
            logger.error("Transferencia no encontrada con ID: {}", idTransferencia);
            throw new IllegalArgumentException("Transferencia no encontrada");
        }
        
        logger.info("Transferencia encontrada - Estado: {}, Origen: {}, Destino: {}", 
                   transferencia.getEstadoTransferencia(),
                   transferencia.getAlmacenOrigen() != null ? transferencia.getAlmacenOrigen().getIdAlmacen() : "null",
                   transferencia.getAlmacenDestino() != null ? transferencia.getAlmacenDestino().getIdAlmacen() : "null");
        
        if (transferencia.getEstadoTransferencia() != Transferencia.EstadoTransferencia.PENDIENTE) {
            logger.error("No se puede ejecutar transferencia en estado: {}", transferencia.getEstadoTransferencia());
            throw new IllegalStateException("Solo se pueden ejecutar transferencias en estado PENDIENTE");
        }
        
        // Obtener el usuario responsable (por ahora usar un valor por defecto)
        String usuarioResponsable = "Usuario Sistema";
        
        // Obtener detalles de la transferencia
        List<DetalleTransferencia> detalles = detalleTransferenciaService.buscarPorTransferencia(idTransferencia);
        
        if (detalles.isEmpty()) {
            logger.error("La transferencia {} no tiene detalles", idTransferencia);
            throw new IllegalStateException("La transferencia no tiene detalles");
        }
        
        logger.info("Procesando {} detalles de transferencia", detalles.size());
        
        // Procesar cada detalle: reducir stock en origen e incrementar en destino
        for (DetalleTransferencia detalle : detalles) {
            Integer idProducto = detalle.getProducto().getIdProducto();
            Integer cantidad = detalle.getCantidad();
            Integer idAlmacenOrigen = transferencia.getAlmacenOrigen().getIdAlmacen();
            Integer idAlmacenDestino = transferencia.getAlmacenDestino().getIdAlmacen();
            
            logger.info("Procesando detalle - Producto: {}, Cantidad: {}, Origen: {}, Destino: {}", 
                       idProducto, cantidad, idAlmacenOrigen, idAlmacenDestino);
            
            try {
                // Reducir stock en almacén origen
                logger.info("Reduciendo stock en almacén origen {} para producto {}", idAlmacenOrigen, idProducto);
                productoAlmacenService.reducirStock(idProducto, idAlmacenOrigen, cantidad, 
                    usuarioResponsable + " - Transferencia Origen");
                
                // Incrementar stock en almacén destino
                logger.info("Incrementando stock en almacén destino {} para producto {}", idAlmacenDestino, idProducto);
                productoAlmacenService.incrementarStock(idProducto, idAlmacenDestino, cantidad, 
                    usuarioResponsable + " - Transferencia Destino");
                
                logger.info("Detalle procesado exitosamente");
            } catch (Exception e) {
                logger.error("Error procesando detalle - Producto: {}, Error: {}", idProducto, e.getMessage());
                throw new RuntimeException("Error procesando transferencia: " + e.getMessage(), e);
            }
        }
        
        // Cambiar estado a COMPLETADA
        transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.COMPLETADA);
        transferencia.setFechaModificacion(LocalDate.now());
        
        Transferencia transferenciaCompletada = transferenciaRepositorio.save(transferencia);
        logger.info("Transferencia {} ejecutada exitosamente", idTransferencia);
        
        return transferenciaCompletada;
    }
    
    /**
     * Cancelar una transferencia
     */
    @Transactional
    public Transferencia cancelarTransferencia(Integer idTransferencia) {
        Transferencia transferencia = obtenerPorId(idTransferencia);
        
        if (transferencia == null) {
            throw new IllegalArgumentException("Transferencia no encontrada");
        }
        
        if (transferencia.getEstadoTransferencia() == Transferencia.EstadoTransferencia.COMPLETADA) {
            throw new IllegalStateException("No se puede cancelar una transferencia ya completada");
        }
        
        transferencia.setEstadoTransferencia(Transferencia.EstadoTransferencia.CANCELADA);
        transferencia.setFechaModificacion(LocalDate.now());
        
        return transferenciaRepositorio.save(transferencia);
    }
    
    /**
     * Buscar transferencias por estado
     */
    public List<Transferencia> buscarPorEstado(Transferencia.EstadoTransferencia estado) {
        return transferenciaRepositorio.findByEstadoTransferencia(estado);
    }
    
    /**
     * Buscar transferencias por almacén origen
     */
    public List<Transferencia> buscarPorAlmacenOrigen(Integer idAlmacen) {
        return transferenciaRepositorio.findByAlmacenOrigenIdAlmacen(idAlmacen);
    }
    
    /**
     * Buscar transferencias por almacén destino
     */
    public List<Transferencia> buscarPorAlmacenDestino(Integer idAlmacen) {
        return transferenciaRepositorio.findByAlmacenDestinoIdAlmacen(idAlmacen);
    }
    
    /**
     * Obtener el almacén de una sucursal específica
     * 
     * @param idSucursal ID de la sucursal
     * @return El almacén de la sucursal
     */
    public Almacen obtenerAlmacenDeSucursal(Integer idSucursal) {
        List<Almacen> almacenes = almacenRepository.findBySucursalIdSucursal(idSucursal);
        if (almacenes.isEmpty()) {
            throw new IllegalArgumentException("No se encontró un almacén para la sucursal: " + idSucursal);
        }
        
        // Priorizar el almacén principal si existe
        for (Almacen almacen : almacenes) {
            if (almacen.getEsPrincipal()) {
                return almacen;
            }
        }
        
        // Si no hay almacén principal, devolver el primero
        return almacenes.get(0);
    }
}
