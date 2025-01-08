package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.modelo.CotizacionProductoInventario;
import com.semgarcorp.ferreteriaSemGar.repositorio.CotizacionProductoInventarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CotizacionProductoInventarioService {

    private final CotizacionProductoInventarioRepository cotizacionProductoInventarioRepositorio;

    public CotizacionProductoInventarioService(CotizacionProductoInventarioRepository cotizacionProductoInventarioRepositorio) {
        this.cotizacionProductoInventarioRepositorio = cotizacionProductoInventarioRepositorio;
    }

    public List<CotizacionProductoInventario> listar() {
        return cotizacionProductoInventarioRepositorio.findAll();
    }

    public CotizacionProductoInventario obtenerPorId(Integer id) {
        return cotizacionProductoInventarioRepositorio.findById(id).orElse(null);
    }

    public List<CotizacionProductoInventario> guardar(List<CotizacionProductoInventario> productos) {
        // Aquí podrías realizar alguna validación adicional si es necesario, por ejemplo:
        // Verificar que los productos tengan todos los campos necesarios, o realizar cálculos previos.

        // Guardar todos los productos a la vez usando el repositorio
        List<CotizacionProductoInventario> nuevosProductos = cotizacionProductoInventarioRepositorio.saveAll(productos);

        // Aquí puedes realizar operaciones adicionales si es necesario (por ejemplo, actualizaciones de estado, cálculos, etc.)

        return nuevosProductos; // Retornar la lista de productos recién guardados
    }

    public List<CotizacionProductoInventario> actualizar(List<CotizacionProductoInventario> productos) {
        BigDecimal total = calcularTotal(productos); // Usamos la lista de productos
        for (CotizacionProductoInventario cpi : productos) {
            // Recalcular y asignar el subtotal de cada producto
            BigDecimal subtotal = calcularSubtotal(cpi);
            cpi.setSubtotal(subtotal);  // Asignar subtotal actualizado a cada producto
        }

        // Actualizar todos los productos
        return cotizacionProductoInventarioRepositorio.saveAll(productos);  // Actualizar todos los productos
    }


    public void eliminar(Integer id) {
        cotizacionProductoInventarioRepositorio.deleteById(id);
    }

    // Metodo para validar descuento
    public void validarDescuento(BigDecimal descuento) {
        if (descuento == null) {
            throw new IllegalArgumentException("El descuento no puede ser nulo.");
        }
        if (descuento.compareTo(BigDecimal.ZERO) < 0 || descuento.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("El descuento debe estar entre 0 y 100.");
        }
    }

    // Metodo para validar precio y cantidad
    private void validarPrecioYCantidad(BigDecimal precioUnitario, BigDecimal cantidad) {
        if (precioUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
    }

    //Metodo para calcular subtotal (precio * cantidad - dscto)
    public BigDecimal calcularSubtotal(CotizacionProductoInventario cpi) {
        // Obtener los valores de cantidad y descuento del objeto CotizacionProductoInventario
        BigDecimal cantidad = cpi.getCantidad();
        BigDecimal descuento = cpi.getDescuento();

        // Obtener el precio unitario del producto asociado a la cotización
        BigDecimal precioUnitario = cpi.getInventarioProducto().getProducto().getPrecio();

        // Validar que el precio y la cantidad sean válidos
        validarPrecioYCantidad(precioUnitario, cantidad);

        // Validar el descuento
        validarDescuento(descuento);

        // Calcular el precio total sin descuento
        BigDecimal precioTotal = precioUnitario.multiply(cantidad);

        // Calcular el descuento en soles (descuento en porcentaje sobre el precio total)
        BigDecimal descuentoEnSoles = precioTotal.multiply(descuento).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

        // Calcular el subtotal restando el descuento
        BigDecimal subtotal = precioTotal.subtract(descuentoEnSoles);

        // Devolver el subtotal con dos decimales de precisión
        return subtotal.setScale(2, RoundingMode.HALF_UP);
    }

    //Metodo para calcular total (suma de subtotales)
    public BigDecimal calcularTotal(List<CotizacionProductoInventario> productos) {
        BigDecimal total = BigDecimal.ZERO;

        for (CotizacionProductoInventario cpi : productos) {
            total = total.add(calcularSubtotal(cpi)); // Sumar los subtotales de cada producto
        }

        return total.setScale(2, RoundingMode.HALF_UP); // Redondear a 2 decimales
    }
}
