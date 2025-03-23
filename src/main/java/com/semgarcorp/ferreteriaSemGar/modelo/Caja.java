package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idCaja;

    @NotNull(message = "El nombre de caja no puede estar vacío")
    @Column(unique = true) // Asegura que no haya duplicados
    private String nombreCaja; // Identificador único de la caja física

    private LocalDateTime fechaApertura;

    private LocalDateTime fechaClausura;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoInicial;

    @Column(precision = 10, scale = 2)
    private BigDecimal entradas;

    @Column(precision = 10, scale = 2)
    private BigDecimal salidas;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoFinal;

    @Column(length = 255)
    private String observaciones;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private EstadoCaja estado;

    @ManyToOne(optional = true) // Permite valores nulos
    @JoinColumn(name = "idUsuario", nullable = true) // Asegura que la columna en la base de datos permita nulos
    private Usuario usuario;

    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoCaja> movimientos;

    public Caja() {
    }

    public Caja(Integer idCaja, String nombreCaja, LocalDateTime fechaApertura, LocalDateTime fechaClausura,
                BigDecimal saldoInicial, BigDecimal entradas, BigDecimal salidas, BigDecimal saldoFinal,
                String observaciones, EstadoCaja estado, Usuario usuario, List<MovimientoCaja> movimientos) {
        this.idCaja = idCaja;
        this.nombreCaja = nombreCaja;
        this.fechaApertura = fechaApertura;
        this.fechaClausura = fechaClausura;
        this.saldoInicial = saldoInicial;
        this.entradas = entradas;
        this.salidas = salidas;
        this.saldoFinal = saldoFinal;
        this.observaciones = observaciones;
        this.estado = estado;
        this.usuario = usuario;
        this.movimientos = movimientos;
    }

    public Integer getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(Integer idCaja) {
        this.idCaja = idCaja;
    }

    public String getNombreCaja() {
        return nombreCaja;
    }

    public void setNombreCaja(String nombreCaja) {
        this.nombreCaja = nombreCaja;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaClausura() {
        return fechaClausura;
    }

    public void setFechaClausura(LocalDateTime fechaClausura) {
        this.fechaClausura = fechaClausura;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getEntradas() {
        return entradas;
    }

    public void setEntradas(BigDecimal entradas) {
        this.entradas = entradas;
    }

    public BigDecimal getSalidas() {
        return salidas;
    }

    public void setSalidas(BigDecimal salidas) {
        this.salidas = salidas;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<MovimientoCaja> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoCaja> movimientos) {
        this.movimientos = movimientos;
    }
}
