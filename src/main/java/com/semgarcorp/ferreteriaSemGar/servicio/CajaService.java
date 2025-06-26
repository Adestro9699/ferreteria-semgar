package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.CajaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.MovimientoCajaDTO;
import com.semgarcorp.ferreteriaSemGar.dto.ReporteCierreCajaDTO;
import com.semgarcorp.ferreteriaSemGar.excepciones.*;
import com.semgarcorp.ferreteriaSemGar.modelo.*;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CajaService {

    private final CajaRepository cajaRepositorio;
    private final CierreCajaRepository cierreCajaRepositorio;
    private final UsuarioRepository usuarioRepositorio;
    private final MovimientoCajaRepository movimientoCajaRepositorio;

    public CajaService(CajaRepository cajaRepositorio, CierreCajaRepository cierreCajaRepositorio,
                       UsuarioRepository usuarioRepositorio, MovimientoCajaRepository movimientoCajaRepositorio) {
        this.cajaRepositorio = cajaRepositorio;
        this.cierreCajaRepositorio = cierreCajaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.movimientoCajaRepositorio = movimientoCajaRepositorio;
    }

    // Métodos básicos de CRUD
    public List<Caja> listarTodasLasCajas() {
        return cajaRepositorio.findAll();
    }

    public Caja obtenerPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la caja no puede ser nulo");
        }
        return cajaRepositorio.findById(id)
                .orElse(null);
    }

    @Transactional
    public Caja crearCaja(CajaDTO cajaDTO) {
        validarCajaDTO(cajaDTO);

        if (cajaRepositorio.existsByNombreCaja(cajaDTO.getNombreCaja())) {
            throw new NombreCajaDuplicadoException("Ya existe una caja con el nombre: " + cajaDTO.getNombreCaja());
        }

        Caja caja = new Caja();
        caja.setNombreCaja(cajaDTO.getNombreCaja());
        caja.setDescripcion(cajaDTO.getDescripcion());
        caja.setEstado(EstadoCaja.CERRADA);
        caja.setSaldoInicial(BigDecimal.ZERO);
        caja.setFechaApertura(null);
        caja.setFechaCierre(null);
        caja.setResponsable(null);

        return cajaRepositorio.save(caja);
    }

    @Transactional
    public Caja actualizarCaja(Integer id, CajaDTO cajaDTO) {
        validarCajaDTO(cajaDTO);

        Caja cajaExistente = obtenerCajaPorId(id);

        if (!cajaExistente.getNombreCaja().equals(cajaDTO.getNombreCaja())) {
            if (cajaRepositorio.existsByNombreCaja(cajaDTO.getNombreCaja())) {
                throw new NombreCajaDuplicadoException("Ya existe una caja con el nombre: " + cajaDTO.getNombreCaja());
            }
        }

        cajaExistente.setNombreCaja(cajaDTO.getNombreCaja());
        cajaExistente.setDescripcion(cajaDTO.getDescripcion());

        return cajaRepositorio.save(cajaExistente);
    }

    @Transactional
    public void eliminarCaja(Integer id) {
        Caja caja = obtenerCajaPorId(id);

        if (caja.getEstado() == EstadoCaja.ABIERTA) {
            throw new OperacionNoPermitidaException("No se puede eliminar una caja abierta");
        }

        if (!caja.getMovimientos().isEmpty()) {
            throw new OperacionNoPermitidaException("No se puede eliminar una caja con movimientos registrados");
        }

        cajaRepositorio.delete(caja);
    }

    // Métodos de operación de caja
    @Transactional
    public Caja abrirCaja(CajaDTO cajaDTO) {
        try {
            // Validaciones del DTO
            if (cajaDTO.getIdCaja() == null) {
                throw new CampoRequeridoException("El ID de la caja es requerido");
            }
            if (cajaDTO.getIdUsuario() == null) {
                throw new CampoRequeridoException("El ID del usuario es requerido");
            }
            if (cajaDTO.getSaldoInicial() == null || cajaDTO.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
                throw new ValorNoValidoException("El saldo inicial no puede ser negativo o nulo");
            }

            // Obtener y validar la caja
            Caja caja = obtenerCajaPorId(cajaDTO.getIdCaja());
            if (caja.getEstado() != EstadoCaja.CERRADA) {
                throw new OperacionNoPermitidaException("La caja no está cerrada");
            }

            // Obtener y validar el usuario
            Usuario usuario = usuarioRepositorio.findById(cajaDTO.getIdUsuario())
                    .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

            // Verificar si el usuario ya tiene una caja abierta
            if (cajaRepositorio.existsByResponsableAndEstado(usuario, EstadoCaja.ABIERTA)) {
                throw new OperacionNoPermitidaException("El usuario ya tiene una caja abierta");
            }

            // Actualizar la caja
            caja.setEstado(EstadoCaja.ABIERTA);
            caja.setResponsable(usuario);
            caja.setFechaApertura(LocalDateTime.now());
            caja.setSaldoInicial(cajaDTO.getSaldoInicial());
            caja.setFechaCierre(null);

            return cajaRepositorio.save(caja);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrenciaException("La caja fue modificada por otro usuario. Por favor, intente nuevamente.");
        }
    }

    @Transactional
    public Caja cerrarCaja(CajaDTO cajaDTO) {
        try {
            // Validaciones del DTO
            if (cajaDTO.getIdCaja() == null) {
                throw new CampoRequeridoException("El ID de la caja es requerido");
            }

            // Obtener y validar la caja
            Caja caja = obtenerCajaPorId(cajaDTO.getIdCaja());
            if (caja.getEstado() != EstadoCaja.ABIERTA) {
                throw new OperacionNoPermitidaException("La caja no está abierta");
            }

            // Calcular totales de movimientos
            BigDecimal totalEntradas = movimientoCajaRepositorio.calcularTotalPorTipo(cajaDTO.getIdCaja(), TipoMovimiento.ENTRADA);
            BigDecimal totalSalidas = movimientoCajaRepositorio.calcularTotalPorTipo(cajaDTO.getIdCaja(), TipoMovimiento.SALIDA);
            BigDecimal saldoFinal = caja.getSaldoInicial().add(totalEntradas).subtract(totalSalidas);

            // Crear registro de cierre
            CierreCaja cierre = new CierreCaja();
            cierre.setFechaApertura(caja.getFechaApertura());
            cierre.setFechaCierre(LocalDateTime.now());
            cierre.setSaldoInicial(caja.getSaldoInicial());
            cierre.setTotalEntradas(totalEntradas);
            cierre.setTotalSalidas(totalSalidas);
            cierre.setSaldoFinal(saldoFinal);
            cierre.setObservaciones(cajaDTO.getObservaciones());
            cierre.setUsuario(caja.getResponsable());
            cierre.setCaja(caja);

            // Actualizar estado de la caja
            caja.setEstado(EstadoCaja.CERRADA);
            caja.setFechaCierre(LocalDateTime.now());
            caja.setResponsable(null);

            // Guardar todo en una única transacción
            cierreCajaRepositorio.save(cierre);
            return cajaRepositorio.save(caja);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrenciaException("La caja fue modificada por otro usuario. Por favor, intente nuevamente.");
        }
    }

    // Métodos de movimientos
    @Transactional
    public MovimientoCaja registrarMovimiento(Integer idCaja, MovimientoCajaDTO movimientoDTO) {
        try {
            // Validar que la caja exista y esté abierta
            Caja caja = obtenerCajaPorId(idCaja);
            if (caja.getEstado() != EstadoCaja.ABIERTA) {
                throw new CajaCerradaException("No se pueden registrar movimientos en una caja cerrada");
            }

            MovimientoCaja movimiento = new MovimientoCaja();
            movimiento.setCaja(caja);
            movimiento.setMonto(movimientoDTO.getMonto());
            movimiento.setTipo(movimientoDTO.getTipo());
            movimiento.setObservaciones(movimientoDTO.getObservaciones());
            movimiento.setFecha(LocalDateTime.now());

            return movimientoCajaRepositorio.save(movimiento);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrenciaException("La caja fue modificada por otro usuario. Por favor, intente nuevamente.");
        }
    }

    @Transactional
    public MovimientoCaja registrarEntradaPorVenta(Integer idCaja, BigDecimal monto) {
        // Validar que la caja exista y esté abierta
        Caja caja = obtenerCajaPorId(idCaja);
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new CajaCerradaException("No se pueden registrar movimientos en una caja cerrada");
        }

        // Crear el DTO del movimiento
        MovimientoCajaDTO movimientoDTO = new MovimientoCajaDTO();
        movimientoDTO.setMonto(monto);
        movimientoDTO.setTipo(TipoMovimiento.ENTRADA);
        movimientoDTO.setObservaciones("Entrada por venta en efectivo");

        // Registrar el movimiento
        return registrarMovimiento(idCaja, movimientoDTO);
    }

    public List<MovimientoCaja> listarMovimientosPorCaja(Integer idCaja) {
        return movimientoCajaRepositorio.findByCajaId(idCaja);
    }

    // Métodos de consulta
    public BigDecimal calcularSaldoActual(Integer idCaja) {
        Caja caja = obtenerCajaPorId(idCaja);
        System.out.println("Calculando saldo actual para caja " + idCaja);
        System.out.println("Saldo inicial: " + caja.getSaldoInicial());

        if (caja.getEstado() == EstadoCaja.CERRADA) {
            Optional<CierreCaja> ultimoCierre = cierreCajaRepositorio.findFirstByCajaOrderByFechaCierreDesc(caja);
            BigDecimal saldoFinal = ultimoCierre.map(CierreCaja::getSaldoFinal).orElse(BigDecimal.ZERO);
            System.out.println("Caja cerrada, retornando saldo final del último cierre: " + saldoFinal);
            return saldoFinal;
        }

        BigDecimal totalEntradas = movimientoCajaRepositorio.calcularTotalPorTipo(idCaja, TipoMovimiento.ENTRADA);
        System.out.println("Total entradas: " + totalEntradas);
        
        BigDecimal totalSalidas = movimientoCajaRepositorio.calcularTotalPorTipo(idCaja, TipoMovimiento.SALIDA);
        System.out.println("Total salidas: " + totalSalidas);

        BigDecimal saldoActual = caja.getSaldoInicial().add(totalEntradas).subtract(totalSalidas);
        System.out.println("Saldo actual calculado: " + saldoActual);

        return saldoActual;
    }

    public List<CierreCaja> obtenerHistorialCierres(Integer idCaja) {
        Caja caja = obtenerCajaPorId(idCaja);
        return cierreCajaRepositorio.findByCajaOrderByFechaCierreDesc(caja);
    }

    // Métodos de validación privados
    private void validarCajaDTO(CajaDTO cajaDTO) {
        if (cajaDTO == null) {
            throw new IllegalArgumentException("El DTO de caja no puede ser nulo");
        }
        if (cajaDTO.getNombreCaja() == null || cajaDTO.getNombreCaja().trim().isEmpty()) {
            throw new CampoRequeridoException("El nombre de la caja es requerido");
        }
    }

    private void validarMovimientoDTO(MovimientoCajaDTO movimientoDTO) {
        if (movimientoDTO == null) {
            throw new IllegalArgumentException("El DTO de movimiento no puede ser nulo");
        }
        if (movimientoDTO.getMonto() == null || movimientoDTO.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorNoValidoException("El monto debe ser mayor que cero");
        }
        if (movimientoDTO.getTipo() == null) {
            throw new CampoRequeridoException("El tipo de movimiento es requerido");
        }
    }

    // Métodos básicos CRUD
    public List<Caja> listar() {
        return cajaRepositorio.findAll();
    }

    public Caja actualizar(Caja caja) {
        // Validar que la caja exista
        obtenerCajaPorId(caja.getIdCaja());
        return cajaRepositorio.save(caja);
    }

    public void eliminar(Integer id) {
        Caja caja = obtenerCajaPorId(id);
        if (caja.getEstado() == EstadoCaja.ABIERTA) {
            throw new OperacionNoPermitidaException("No se puede eliminar una caja abierta");
        }
        cajaRepositorio.deleteById(id);
    }

    public CajaDTO convertirACajaDTO(Caja caja) {
        CajaDTO dto = new CajaDTO();
        dto.setIdCaja(caja.getIdCaja());
        dto.setNombreCaja(caja.getNombreCaja());
        dto.setDescripcion(caja.getDescripcion());
        dto.setEstado(caja.getEstado());
        dto.setSaldoInicial(caja.getSaldoInicial());
        dto.setFechaApertura(caja.getFechaApertura());
        dto.setFechaClausura(caja.getFechaCierre());
        if (caja.getResponsable() != null) {
            dto.setIdUsuario(caja.getResponsable().getIdUsuario());
        }
        return dto;
    }

    @Transactional
    public Caja registrarEntradaManual(Integer idCaja, MovimientoCajaDTO movimientoDTO) {
        try {
        // Validar que la caja exista y esté abierta
        Caja caja = obtenerCajaPorId(idCaja);
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new CajaCerradaException("No se pueden registrar movimientos en una caja cerrada");
        }

        // Validar el monto
        if (movimientoDTO.getMonto() == null || movimientoDTO.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorNoValidoException("El monto debe ser mayor que cero");
        }

        // Crear el movimiento
        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setMonto(movimientoDTO.getMonto());
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setObservaciones(movimientoDTO.getObservaciones());
        movimiento.setFecha(LocalDateTime.now());

        // Guardar el movimiento
        movimientoCajaRepositorio.save(movimiento);
        return caja;
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrenciaException("La caja fue modificada por otro usuario. Por favor, intente nuevamente.");
        }
    }

    @Transactional
    public Caja registrarSalidaManual(Integer idCaja, MovimientoCajaDTO movimientoDTO) {
        try {
        // Validar que la caja exista y esté abierta
        Caja caja = obtenerCajaPorId(idCaja);
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new CajaCerradaException("No se pueden registrar movimientos en una caja cerrada");
        }

        // Validar el monto
        if (movimientoDTO.getMonto() == null || movimientoDTO.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorNoValidoException("El monto debe ser mayor que cero");
        }

        // Verificar saldo suficiente
        BigDecimal saldoActual = calcularSaldoActual(idCaja);
            System.out.println("Intentando realizar salida de: " + movimientoDTO.getMonto());
            System.out.println("Saldo actual antes de la salida: " + saldoActual);
            
        if (saldoActual.compareTo(movimientoDTO.getMonto()) < 0) {
                System.out.println("Salida rechazada: Saldo insuficiente");
                throw new SaldoInsuficienteException("Saldo insuficiente para realizar esta operación. Saldo actual: " + saldoActual);
        }

        // Crear el movimiento
        MovimientoCaja movimiento = new MovimientoCaja();
        movimiento.setCaja(caja);
        movimiento.setMonto(movimientoDTO.getMonto());
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setObservaciones(movimientoDTO.getObservaciones());
        movimiento.setFecha(LocalDateTime.now());

        // Guardar el movimiento
        movimientoCajaRepositorio.save(movimiento);
        return caja;
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrenciaException("La caja fue modificada por otro usuario. Por favor, intente nuevamente.");
        }
    }

    // Método interno para obtener caja por ID con excepción
    private Caja obtenerCajaPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la caja no puede ser nulo");
        }
        return cajaRepositorio.findById(id)
                .orElseThrow(() -> new CajaNoEncontradaException("Caja con ID " + id + " no encontrada"));
    }

    // Nueva excepción para manejar problemas de concurrencia
    public class ConcurrenciaException extends RuntimeException {
        public ConcurrenciaException(String mensaje) {
            super(mensaje);
        }
    }

    public ReporteCierreCajaDTO generarReporteCierreCaja(Integer idCaja) {
        Caja caja = obtenerCajaPorId(idCaja);
        if (caja.getEstado() != EstadoCaja.CERRADA) {
            throw new OperacionNoPermitidaException("Solo se puede generar reporte de cajas cerradas");
        }

        // Obtener el último cierre de caja
        CierreCaja ultimoCierre = cierreCajaRepositorio.findFirstByCajaOrderByFechaCierreDesc(caja)
                .orElseThrow(() -> new OperacionNoPermitidaException("No se encontró registro de cierre para esta caja"));

        // Obtener todos los movimientos del día
        List<MovimientoCaja> movimientos = movimientoCajaRepositorio.findByCajaId(idCaja);

        // Crear el DTO del reporte
        ReporteCierreCajaDTO reporte = new ReporteCierreCajaDTO();
        reporte.setNombreCaja(caja.getNombreCaja());
        
        // Usar el primer apellido y primer nombre del trabajador
        if (ultimoCierre.getUsuario() == null || ultimoCierre.getUsuario().getTrabajador() == null) {
            throw new OperacionNoPermitidaException("No se encontró el trabajador responsable del cierre de caja");
        }
        
        Trabajador trabajador = ultimoCierre.getUsuario().getTrabajador();
        
        // Obtener el primer apellido y el primer nombre
        String[] apellidos = trabajador.getApellidoTrabajador().split(" ");
        String[] nombres = trabajador.getNombreTrabajador().split(" ");
        
        String primerApellido = apellidos[0];
        String primerNombre = nombres[0];
        
        String nombreFormateado = primerApellido + " " + primerNombre;
        reporte.setResponsable(nombreFormateado);
        
        reporte.setFechaApertura(ultimoCierre.getFechaApertura());
        reporte.setFechaCierre(ultimoCierre.getFechaCierre());
        reporte.setSaldoInicial(ultimoCierre.getSaldoInicial());
        
        // Inicializar totales
        BigDecimal totalVentasEfectivo = BigDecimal.ZERO;
        BigDecimal totalIngresosManuales = BigDecimal.ZERO;
        BigDecimal totalEgresosManuales = BigDecimal.ZERO;

        // Procesar movimientos
        for (MovimientoCaja movimiento : movimientos) {
            String descripcion = movimiento.getObservaciones() != null ? 
                               movimiento.getObservaciones() : 
                               (movimiento.getTipo() == TipoMovimiento.ENTRADA ? "INGRESO MANUAL" : "EGRESO MANUAL");

            ReporteCierreCajaDTO.MovimientoReporteDTO movimientoDTO = 
                new ReporteCierreCajaDTO.MovimientoReporteDTO(descripcion, movimiento.getMonto());

            if (movimiento.getTipo() == TipoMovimiento.ENTRADA) {
                if (descripcion.equals("Entrada por venta en efectivo")) {
                    reporte.getVentasEfectivoList().add(movimientoDTO);
                    totalVentasEfectivo = totalVentasEfectivo.add(movimiento.getMonto());
                } else {
                    reporte.getIngresosManualesList().add(movimientoDTO);
                    totalIngresosManuales = totalIngresosManuales.add(movimiento.getMonto());
                }
            } else {
                reporte.getEgresosManualesList().add(movimientoDTO);
                totalEgresosManuales = totalEgresosManuales.add(movimiento.getMonto());
            }
        }

        // Establecer totales
        reporte.setVentasEfectivo(totalVentasEfectivo);
        reporte.setIngresosManuales(totalIngresosManuales);
        reporte.setEgresosManuales(totalEgresosManuales);
        
        // Calcular totales generales
        BigDecimal totalEntradas = totalVentasEfectivo.add(totalIngresosManuales);
        reporte.setTotalEntradas(totalEntradas);
        reporte.setTotalSalidas(totalEgresosManuales);
        reporte.setSaldoFinal(ultimoCierre.getSaldoFinal());

        return reporte;
    }
}