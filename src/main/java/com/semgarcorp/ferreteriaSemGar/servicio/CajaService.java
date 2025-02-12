package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.modelo.Cotizacion;
import com.semgarcorp.ferreteriaSemGar.modelo.TipoPago;
import com.semgarcorp.ferreteriaSemGar.modelo.Venta;
import com.semgarcorp.ferreteriaSemGar.repositorio.CajaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepositorio;

    // Constructor con inyección del repositorio
    public CajaService(CajaRepository cajaRepositorio) {
        this.cajaRepositorio = cajaRepositorio;
    }

    // Listar todos los registros de caja
    public List<Caja> listar() {
        return cajaRepositorio.findAll();
    }

    // Obtener un registro de caja por ID
    public Caja obtenerPorId(Integer id) {
        return cajaRepositorio.findById(id).orElse(null);
    }

    // Guardar un nuevo registro de caja o actualiza uno existente
    public Caja guardar(Caja caja) {
        // Calcular saldoFinal antes de guardar
        BigDecimal saldoFinal = calcularSaldoFinal(caja);
        caja.setSaldoFinal(saldoFinal);
        return cajaRepositorio.save(caja);
    }

    // Actualizar un registro de caja existente
    public Caja actualizar(Caja caja) {
        // Calcular saldoFinal antes de actualizar
        BigDecimal saldoFinal = calcularSaldoFinal(caja);
        caja.setSaldoFinal(saldoFinal);
        return cajaRepositorio.save(caja);
    }

    // Eliminar un registro de caja por ID
    public void eliminar(Integer id) {
        cajaRepositorio.deleteById(id);
    }

    /*public void registrarVentaEnEfectivo(Caja caja, Venta venta) {
        // Validar que la venta y la caja no sean nulas
        if (venta == null || caja == null) {
            throw new IllegalArgumentException("La venta o la caja no pueden ser nulas");
        }

        // Validar si es una venta asociada a una cotización
        if (venta.getCotizacionProductoInventario() != null) {
            Cotizacion cotizacion = venta.getCotizacionProductoInventario().getCotizacion();

            // Validar que la cotización y su tipo de pago no sean nulos
            if (cotizacion != null && cotizacion.getTipoPago() != null &&
                    "EFECTIVO".equalsIgnoreCase(cotizacion.getTipoPago().getNombreTipoPago())) {
                actualizarEntradasYCaja(caja, venta.getTotalVenta());
            }
        } else {
            // Caso de venta directa (asumimos pago en efectivo)
            actualizarEntradasYCaja(caja, venta.getTotalVenta());
        }
    }*/

    private void actualizarEntradasYCaja(Caja caja, BigDecimal montoVenta) {
        // Actualizar las entradas
        BigDecimal nuevoMontoEntradas = caja.getEntradas().add(montoVenta);
        caja.setEntradas(nuevoMontoEntradas);

        // Calcular y establecer el nuevo saldo final
        BigDecimal saldoFinal = calcularSaldoFinal(caja);
        caja.setSaldoFinal(saldoFinal);

        // Guardar la caja actualizada
        cajaRepositorio.save(caja);
    }

    // Metodo para calcular el saldoFinal
    private BigDecimal calcularSaldoFinal(Caja caja) {
        return caja.getSaldoInicial().add(caja.getEntradas()).subtract(caja.getSalidas());
    }
}
