package com.semgarcorp.ferreteriaSemGar.modelo;

import com.semgarcorp.ferreteriaSemGar.servicio.CotizacionProductoInventarioService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        // Crear una instancia del servicio
        CotizacionProductoInventarioService service = new CotizacionProductoInventarioService(null); // Asegúrate de inyectar el repositorio

        // Crear productos preestablecidos para la venta
        List<CotizacionProductoInventario> productos = new ArrayList<>();

        // Preestablecer los 4 productos con precios específicos
        for (int i = 1; i <= 4; i++) {
            CotizacionProductoInventario cpi = new CotizacionProductoInventario();


            InventarioProducto inventarioProducto = getInventarioProducto(i);
            cpi.setInventarioProducto(inventarioProducto);

            // Ingresar datos por consola para cantidad y descuento
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingrese la cantidad para el producto " + i + ": ");
            BigDecimal cantidad = scanner.nextBigDecimal();  // Cambié a BigDecimal
            System.out.print("Ingrese el descuento para el producto " + i + " (entre 0 y 100): ");
            BigDecimal descuento = scanner.nextBigDecimal();

            // Asignar los valores ingresados a los atributos de la cotización
            cpi.setCantidad(cantidad);  // Ahora es un BigDecimal
            cpi.setDescuento(descuento);

            // Agregar el producto a la lista de productos
            productos.add(cpi);
        }

        // Calcular y mostrar los subtotales para cada producto
        for (CotizacionProductoInventario cpi : productos) {
            // Calcular el subtotal para cada producto
            BigDecimal subtotal = service.calcularSubtotal(cpi);
            System.out.println("Subtotal para el producto con precio " + cpi.getInventarioProducto().getProducto().getPrecio() + ": " + subtotal);
        }

        // Calcular el total de la venta
        BigDecimal total = service.calcularTotal(productos);
        System.out.println("El total de la venta es: " + total);
    }

    private static InventarioProducto getInventarioProducto(int i) {
        InventarioProducto inventarioProducto = new InventarioProducto();
        Producto producto = new Producto();

        // Asignar precios manuales
        BigDecimal precio = switch (i) {
            case 1 -> BigDecimal.valueOf(150); // Precio para el producto 1
            case 2 -> BigDecimal.valueOf(200); // Precio para el producto 2
            case 3 -> BigDecimal.valueOf(120); // Precio para el producto 3
            case 4 -> BigDecimal.valueOf(180); // Precio para el producto 4
            default -> BigDecimal.valueOf(100); // Valor predeterminado (esto no debería ejecutarse)
        };
        producto.setPrecio(precio);

        inventarioProducto.setProducto(producto);
        return inventarioProducto;
    }
}
