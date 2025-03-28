package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Compra;
import com.semgarcorp.ferreteriaSemGar.repositorio.CompraRepository;
import com.semgarcorp.ferreteriaSemGar.repositorio.DetalleCompraRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepositorio;
    private final DetalleCompraRepository detalleCompraRepositorio;

    // Constructor
    public CompraService(CompraRepository compraRepositorio, DetalleCompraRepository detalleCompraRepositorio) {
        this.compraRepositorio = compraRepositorio;
        this.detalleCompraRepositorio = detalleCompraRepositorio;
    }

    public List<Compra> listar() {
        return compraRepositorio.findAll();
    }

    public Compra obtenerPorId(Integer id) {
        return compraRepositorio.findById(id).orElse(null);
    }

    public Compra guardar(Compra compra) {
        return compraRepositorio.save(compra);
    }

    public Compra actualizar(Compra compra) {
        return compraRepositorio.save(compra);  // Esto persiste los cambios de compra existente
    }

    public void eliminar(Integer id) {
        compraRepositorio.deleteById(id);
    }

    /**
     * Método para recalcular el total de la compra basado en los subtotales de sus detalles.
     */
    @Transactional
    public void recalcularTotalCompra(Integer idCompra) {
        // Obtener la compra por ID
        Compra compra = compraRepositorio.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        // Calcular la suma de los subtotales de los detalles de compra
        BigDecimal totalCompra = detalleCompraRepositorio.calcularTotalPorCompra(idCompra)
                .orElse(BigDecimal.ZERO);

        // Actualizar el total de la compra
        compra.setTotalCompra(totalCompra);
        compraRepositorio.save(compra);
    }
}