package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Utilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUtilidad;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    private Categoria categoria; // Opcional: Utilidad por categoría

    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto; // Opcional: Utilidad por producto

    @Column(nullable = false)
    private Integer porcentajeUtilidad; // Almacenamos como entero (ejemplo: 30 para 30%)

    // Getters y Setters
    public Integer getIdUtilidad() {
        return idUtilidad;
    }

    public void setIdUtilidad(Integer idUtilidad) {
        this.idUtilidad = idUtilidad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getPorcentajeUtilidad() {
        return porcentajeUtilidad;
    }

    public void setPorcentajeUtilidad(Integer porcentajeUtilidad) {
        this.porcentajeUtilidad = porcentajeUtilidad;
    }

    // Método auxiliar para convertir el porcentaje entero a decimal
    public BigDecimal getPorcentajeUtilidadComoDecimal() {
        if (porcentajeUtilidad == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(porcentajeUtilidad).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
    }
}