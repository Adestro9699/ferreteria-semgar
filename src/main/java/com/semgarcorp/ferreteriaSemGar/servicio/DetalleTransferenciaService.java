package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.DetalleTransferencia;
import com.semgarcorp.ferreteriaSemGar.repositorio.DetalleTransferenciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetalleTransferenciaService {
    
    private final DetalleTransferenciaRepository detalleTransferenciaRepository;
    private final ProductoService productoService;
    
    public DetalleTransferenciaService(DetalleTransferenciaRepository detalleTransferenciaRepository,
                                      ProductoService productoService) {
        this.detalleTransferenciaRepository = detalleTransferenciaRepository;
        this.productoService = productoService;
    }
    
    /**
     * Listar todos los detalles de transferencia
     */
    public List<DetalleTransferencia> listar() {
        return detalleTransferenciaRepository.findAll();
    }
    
    /**
     * Obtener un detalle por su ID
     */
    public DetalleTransferencia obtenerPorId(Integer id) {
        return detalleTransferenciaRepository.findById(id).orElse(null);
    }
    
    /**
     * Guardar un nuevo detalle de transferencia
     */
    @Transactional
    public DetalleTransferencia guardar(DetalleTransferencia detalleTransferencia) {
        // Si no se especifica precio unitario, tomar el precio actual del producto
        if (detalleTransferencia.getPrecioUnitario() == null && detalleTransferencia.getProducto() != null) {
            detalleTransferencia.setPrecioUnitario(detalleTransferencia.getProducto().getPrecio());
        }
        
        return detalleTransferenciaRepository.save(detalleTransferencia);
    }
    
    /**
     * Actualizar un detalle existente
     */
    @Transactional
    public DetalleTransferencia actualizar(DetalleTransferencia detalleTransferencia) {
        // Si no se especifica precio unitario, tomar el precio actual del producto
        if (detalleTransferencia.getPrecioUnitario() == null && detalleTransferencia.getProducto() != null) {
            detalleTransferencia.setPrecioUnitario(detalleTransferencia.getProducto().getPrecio());
        }
        
        return detalleTransferenciaRepository.save(detalleTransferencia);
    }
    
    /**
     * Eliminar un detalle por su ID
     */
    @Transactional
    public void eliminar(Integer id) {
        detalleTransferenciaRepository.deleteById(id);
    }
    
    /**
     * Buscar detalles por transferencia
     */
    public List<DetalleTransferencia> buscarPorTransferencia(Integer idTransferencia) {
        return detalleTransferenciaRepository.findByTransferenciaIdTransferencia(idTransferencia);
    }
    
    /**
     * Buscar detalles por producto
     */
    public List<DetalleTransferencia> buscarPorProducto(Integer idProducto) {
        return detalleTransferenciaRepository.findByProductoIdProducto(idProducto);
    }
} 