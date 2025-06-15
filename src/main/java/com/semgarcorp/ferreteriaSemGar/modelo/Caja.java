package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCaja; //identificador único de la caja

    @NotNull(message = "El nombre de caja no puede estar vacío")
    @Column(unique = true, length = 100) // Asegura que no haya duplicados
    private String nombreCaja; //nombre de la caja, unique para que mas de una caja no tengan nombres similares

    @Column (length = 255)
    private String descripcion;

    @NotNull(message = "El estado no puede estar vacío")
    @Enumerated(EnumType.STRING)
    private EstadoCaja estado; //ABIERTA, CERRADA, EN_CIERRE

    @ManyToOne(fetch = FetchType.LAZY) // Permite valores nulos
    @JoinColumn(name = "id_usuario", nullable = true) // Asegura que la columna en la base de datos permita nulos
    private Usuario responsable; // Usuario a cargo

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura; //fecha en la que se abre la caja

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre; //fecha en la que se cierra la caja

    @JoinColumn(name = "saldo_inicial")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoInicial; //saldo con el que inicia a operar una caja

    @Version
    private Long version; //para manejar problemas de concurrencia

    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovimientoCaja> movimientos = new ArrayList<>(); //relacionamos los movimientos a la caja activa

    public Caja() {
    }

    public Caja(Integer idCaja, String nombreCaja, String descripcion, EstadoCaja estado, Usuario responsable,
                LocalDateTime fechaApertura, LocalDateTime fechaCierre, BigDecimal saldoInicial,
                List<MovimientoCaja> movimientos) {
        this.idCaja = idCaja;
        this.nombreCaja = nombreCaja;
        this.descripcion = descripcion;
        this.estado = estado;
        this.responsable = responsable;
        this.fechaApertura = fechaApertura;
        this.fechaCierre = fechaCierre;
        this.saldoInicial = saldoInicial;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoCaja getEstado() {
        return estado;
    }

    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    public Usuario getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuario responsable) {
        this.responsable = responsable;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public List<MovimientoCaja> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoCaja> movimientos) {
        this.movimientos = movimientos;
    }

    //es un atributo dinámico que combina el estado de la caja y el nombre del usuario responsable de la caja
    //para poder mostrar en una caja abierta Caja en uso por: +nombreUsuario
    @Transient // No se persiste en BD, se calcula dinámicamente
    public String getEstadoUso() {
        return this.estado == EstadoCaja.ABIERTA
                ? "Caja en uso por: " + this.responsable.getNombreUsuario()
                : "Caja cerrada";
    }
}
