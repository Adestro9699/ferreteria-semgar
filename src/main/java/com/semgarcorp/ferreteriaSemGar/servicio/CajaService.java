package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.CajaDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Caja;
import com.semgarcorp.ferreteriaSemGar.modelo.CierreCaja;
import com.semgarcorp.ferreteriaSemGar.modelo.EstadoCaja;
import com.semgarcorp.ferreteriaSemGar.modelo.Usuario;
import com.semgarcorp.ferreteriaSemGar.repositorio.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CajaService {

    private final CajaRepository cajaRepositorio;

    private final CierreCajaRepository cierreCajaRepositorio;

    private final UsuarioRepository usuarioRepositorio;

    public CajaService(CajaRepository cajaRepositorio, CierreCajaRepository cierreCajaRepositorio,
                       UsuarioRepository usuarioRepositorio) {
        this.cajaRepositorio = cajaRepositorio;
        this.cierreCajaRepositorio = cierreCajaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Caja> listar() {
        return cajaRepositorio.findAll();
    }

    public Caja obtenerPorId(Integer id) {
        return cajaRepositorio.findById(id).orElse(null);
    }

    public Caja crearCaja(CajaDTO cajaDTO) {
        // Validar que el DTO no sea nulo
        if (cajaDTO == null) {
            throw new IllegalArgumentException("El DTO de la caja no puede ser nulo.");
        }

        // Validar que el nombre de la caja no sea nulo o vacío
        if (cajaDTO.getNombreCaja() == null || cajaDTO.getNombreCaja().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la caja no puede estar vacío.");
        }

        // Validar que no exista una caja con el mismo nombre
        if (cajaRepositorio.existsByNombreCaja(cajaDTO.getNombreCaja())) {
            throw new IllegalArgumentException("Ya existe una caja con el nombre: " + cajaDTO.getNombreCaja());
        }

        // Crear una nueva caja
        Caja caja = new Caja();
        caja.setNombreCaja(cajaDTO.getNombreCaja());
        caja.setEstado(EstadoCaja.CERRADA); // Estado inicial: CERRADA
        caja.setSaldoInicial(BigDecimal.ZERO); // Saldo inicial en 0
        caja.setEntradas(BigDecimal.ZERO); // Entradas en 0
        caja.setSalidas(BigDecimal.ZERO); // Salidas en 0
        caja.setSaldoFinal(BigDecimal.ZERO); // Saldo final en 0
        caja.setUsuario(null); // No hay usuario asociado al crear la caja

        // Guardar la caja en la base de datos
        return cajaRepositorio.save(caja);
    }

    public Caja actualizar(Caja caja) {
        return cajaRepositorio.save(caja);
    }

    public void eliminar(Integer id) {
        cajaRepositorio.deleteById(id);
    }

    public Caja convertirACaja(CajaDTO cajaDTO) {
        if (cajaDTO == null) {
            throw new IllegalArgumentException("El DTO de la caja no puede ser nulo.");
        }

        Caja caja = new Caja();

        // Asignar campos básicos de Caja
        caja.setNombreCaja(cajaDTO.getNombreCaja() != null ? cajaDTO.getNombreCaja() : "");
        caja.setFechaApertura(cajaDTO.getFechaApertura() != null ? cajaDTO.getFechaApertura() : LocalDateTime.now());
        caja.setSaldoInicial(cajaDTO.getSaldoInicial() != null ? cajaDTO.getSaldoInicial() : BigDecimal.ZERO);
        caja.setEntradas(cajaDTO.getEntradas() != null ? cajaDTO.getEntradas() : BigDecimal.ZERO);
        caja.setSalidas(cajaDTO.getSalidas() != null ? cajaDTO.getSalidas() : BigDecimal.ZERO);
        caja.setSaldoFinal(cajaDTO.getSaldoFinal() != null ? cajaDTO.getSaldoFinal() : cajaDTO.getSaldoInicial());
        caja.setEstado(cajaDTO.getEstado() != null ? cajaDTO.getEstado() : EstadoCaja.ABIERTA); // Valor por defecto: ABIERTA
        caja.setUsuario(buscarEntidadPorId(usuarioRepositorio, cajaDTO.getIdUsuario(), "Usuario"));

        // Validar que el saldo inicial no sea negativo
        if (caja.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }

        return caja;
    }

    // Metodo auxiliar para buscar entidades y lanzar excepción si no se encuentran
    private <T> T buscarEntidadPorId(JpaRepository<T, Integer> repository, Integer id, String nombreEntidad) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(nombreEntidad + " con ID " + id + " no encontrada"));
    }

    public CajaDTO convertirACajaDTO(Caja caja) {
        if (caja == null) {
            throw new IllegalArgumentException("La entidad Caja no puede ser nula.");
        }

        CajaDTO cajaDTO = new CajaDTO();

        // Asignar campos básicos de CajaDTO
        cajaDTO.setIdCaja(caja.getIdCaja());
        cajaDTO.setNombreCaja(caja.getNombreCaja());
        cajaDTO.setFechaApertura(caja.getFechaApertura());
        cajaDTO.setFechaClausura(caja.getFechaClausura());
        cajaDTO.setSaldoInicial(caja.getSaldoInicial());
        cajaDTO.setEntradas(caja.getEntradas());
        cajaDTO.setSalidas(caja.getSalidas());
        cajaDTO.setSaldoFinal(caja.getSaldoFinal());
        cajaDTO.setEstado(caja.getEstado()); // Usa el enum EstadoCaja
        cajaDTO.setIdUsuario(caja.getUsuario() != null ? caja.getUsuario().getIdUsuario() : null); // Asignar ID del usuario
        cajaDTO.setObservaciones(caja.getObservaciones());

        return cajaDTO;
    }

    public Caja abrirCaja(CajaDTO cajaDTO) {
        // Validar que el DTO no sea nulo
        if (cajaDTO == null) {
            throw new IllegalArgumentException("El DTO de la caja no puede ser nulo.");
        }

        // Validar que el ID de la caja no sea nulo
        if (cajaDTO.getIdCaja() == null) {
            throw new IllegalArgumentException("El ID de la caja no puede ser nulo.");
        }

        // Buscar la caja por su ID
        Caja caja = cajaRepositorio.findById(cajaDTO.getIdCaja())
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada."));

        // Validar que la caja esté cerrada
        if (caja.getEstado() != EstadoCaja.CERRADA) {
            throw new IllegalStateException("La caja no está cerrada.");
        }

        // Validar que el saldo inicial no sea nulo o negativo
        if (cajaDTO.getSaldoInicial() == null || cajaDTO.getSaldoInicial().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser un valor positivo.");
        }

        // Validar que el ID del usuario no sea nulo
        if (cajaDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }

        // Buscar el usuario por su ID
        Usuario usuario = usuarioRepositorio.findById(cajaDTO.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        // Cambiar el estado a ABIERTA y asignar la fecha de apertura
        caja.setEstado(EstadoCaja.ABIERTA);
        caja.setFechaApertura(LocalDateTime.now());

        // Asignar el saldo inicial
        caja.setSaldoInicial(cajaDTO.getSaldoInicial());
        caja.setSaldoFinal(cajaDTO.getSaldoInicial()); // El saldo final inicial es igual al saldo inicial

        // Asignar el usuario que abre la caja
        caja.setUsuario(usuario);

        // Guardar los cambios en la base de datos
        return cajaRepositorio.save(caja);
    }

    public Caja cerrarCaja(CajaDTO cajaDTO) {
        // Validar que el DTO no sea nulo
        if (cajaDTO == null) {
            throw new IllegalArgumentException("El DTO de la caja no puede ser nulo.");
        }

        // Validar que el ID de la caja no sea nulo
        if (cajaDTO.getIdCaja() == null) {
            throw new IllegalArgumentException("El ID de la caja no puede ser nulo.");
        }

        // Buscar la caja por su ID
        Caja caja = cajaRepositorio.findById(cajaDTO.getIdCaja())
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada."));

        // Validar que la caja esté abierta
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta.");
        }

        // Validar que el ID del usuario no sea nulo
        if (cajaDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }

        // Cambiar el estado a CERRADA y asignar la fecha de cierre
        caja.setEstado(EstadoCaja.CERRADA);
        caja.setFechaClausura(LocalDateTime.now());

        // Calcular el saldo final
        BigDecimal saldoFinal = caja.getSaldoInicial()
                .add(caja.getEntradas())
                .subtract(caja.getSalidas());
        caja.setSaldoFinal(saldoFinal);

        // Asignar observaciones (si se proporcionan)
        caja.setObservaciones(cajaDTO.getObservaciones());

        // Guardar los cambios en la tabla Caja
        caja = cajaRepositorio.save(caja);

        // Crear y guardar el registro en la tabla CierreCaja
        CierreCaja cierreCaja = new CierreCaja();
        cierreCaja.setFechaApertura(caja.getFechaApertura()); // Fecha de apertura de la caja
        cierreCaja.setFechaCierre(caja.getFechaClausura()); // Fecha de cierre de la caja
        cierreCaja.setSaldoInicial(caja.getSaldoInicial()); // Saldo inicial de la caja
        cierreCaja.setTotalEntradas(caja.getEntradas()); // Total de entradas de la caja
        cierreCaja.setTotalSalidas(caja.getSalidas()); // Total de salidas de la caja
        cierreCaja.setSaldoFinal(saldoFinal); // Saldo final calculado
        cierreCaja.setObservaciones(cajaDTO.getObservaciones()); // Observaciones del DTO

        // Asignar el usuario y la caja
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(cajaDTO.getIdUsuario()); // Asignar el ID del usuario
        cierreCaja.setUsuario(usuario);

        cierreCaja.setCaja(caja); // Asignar la caja

        // Guardar el registro en la tabla CierreCaja
        cierreCajaRepositorio.save(cierreCaja);

        // Reiniciar los valores de la caja para el próximo ciclo
        caja.setFechaApertura(null); // Reiniciar fecha de apertura
        caja.setFechaClausura(null); // Reiniciar fecha de cierre
        caja.setSaldoInicial(BigDecimal.ZERO); // Reiniciar saldo inicial
        caja.setEntradas(BigDecimal.ZERO); // Reiniciar entradas
        caja.setSalidas(BigDecimal.ZERO); // Reiniciar salidas
        caja.setSaldoFinal(BigDecimal.ZERO); // Reiniciar saldo final
        caja.setObservaciones(null); // Reiniciar observaciones
        caja.setUsuario(null); // Reiniciar el ID del usuario (asignar a null)
        caja.setEstado(EstadoCaja.CERRADA); // Mantener el estado como CERRADA

        // Guardar los cambios en la tabla Caja
        caja = cajaRepositorio.save(caja);

        return caja;
    }

    public Caja registrarEntradaManual(Integer idCaja, BigDecimal monto, String observaciones) {
        // Validar que el monto no sea nulo o negativo
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la entrada debe ser un valor positivo.");
        }

        // Buscar la caja por su ID
        Caja caja = cajaRepositorio.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada."));

        // Validar que la caja esté abierta
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta.");
        }

        // Actualizar las entradas y el saldo final
        caja.setEntradas(caja.getEntradas().add(monto));
        caja.setSaldoFinal(caja.getSaldoFinal().add(monto));

        // Guardar observaciones (si se proporcionan)
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            caja.setObservaciones(observaciones);
        }

        // Guardar los cambios en la base de datos
        return cajaRepositorio.save(caja);
    }

    public Caja registrarSalidaManual(Integer idCaja, BigDecimal monto, String observaciones) {
        // Validar que el monto no sea nulo o negativo
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la salida debe ser un valor positivo.");
        }

        // Buscar la caja por su ID
        Caja caja = cajaRepositorio.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada."));

        // Validar que la caja esté abierta
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta.");
        }

        // Validar que haya suficiente saldo para la salida
        if (caja.getSaldoFinal().compareTo(monto) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar la salida.");
        }

        // Actualizar las salidas y el saldo final
        caja.setSalidas(caja.getSalidas().add(monto));
        caja.setSaldoFinal(caja.getSaldoFinal().subtract(monto));

        // Guardar observaciones (si se proporcionan)
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            caja.setObservaciones(observaciones);
        }

        // Guardar los cambios en la base de datos
        return cajaRepositorio.save(caja);
    }

    public Caja registrarEntradaPorVenta(Integer idCaja, BigDecimal montoVenta) {
        // Validar que el monto de la venta no sea nulo o negativo
        if (montoVenta == null || montoVenta.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de la venta debe ser un valor positivo.");
        }

        // Buscar la caja por su ID
        Caja caja = cajaRepositorio.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("Caja no encontrada."));

        // Validar que la caja esté abierta
        if (caja.getEstado() != EstadoCaja.ABIERTA) {
            throw new IllegalStateException("La caja no está abierta.");
        }

        // Actualizar las entradas y el saldo final
        caja.setEntradas(caja.getEntradas().add(montoVenta));
        caja.setSaldoFinal(caja.getSaldoFinal().add(montoVenta));

        // Guardar los cambios en la base de datos
        return cajaRepositorio.save(caja);
    }
}
