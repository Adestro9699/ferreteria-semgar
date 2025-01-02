package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.TransaccionBancaria;
import com.semgarcorp.ferreteriaSemGar.servicio.TransaccionBancariaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transaccionesbancarias")
public class TransaccionBancariaController {

    private final TransaccionBancariaService transaccionBancariaService;

    public TransaccionBancariaController(TransaccionBancariaService transaccionBancariaService) {
        this.transaccionBancariaService = transaccionBancariaService;
    }

    // Obtener la lista de todas las transacciones bancarias
    @GetMapping
    public List<TransaccionBancaria> listar() {
        return transaccionBancariaService.listar();
    }

    // Obtener una transacción bancaria por su ID
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionBancaria> obtenerPorId(@PathVariable Integer id) {
        TransaccionBancaria transaccionBancaria = transaccionBancariaService.obtenerPorId(id);
        if (transaccionBancaria != null) {
            return ResponseEntity.ok(transaccionBancaria);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Crear una nueva transacción bancaria
    @PostMapping
    public ResponseEntity<TransaccionBancaria> guardar(@RequestBody TransaccionBancaria transaccionBancaria) {
        TransaccionBancaria nuevaTransaccionBancaria = transaccionBancariaService.guardar(transaccionBancaria);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaTransaccionBancaria.getIdTransaccion()).toUri();

        return ResponseEntity.created(location).body(nuevaTransaccionBancaria);
    }

    // Actualizar una transacción bancaria existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TransaccionBancaria> actualizar(@PathVariable Integer id, @RequestBody TransaccionBancaria transaccionBancaria) {
        TransaccionBancaria transaccionBancariaExistente = transaccionBancariaService.obtenerPorId(id);
        if (transaccionBancariaExistente != null) {
            transaccionBancaria.setIdTransaccion(id);
            TransaccionBancaria transaccionBancariaActualizada = transaccionBancariaService.actualizar(transaccionBancaria);
            return ResponseEntity.ok(transaccionBancariaActualizada);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Eliminar una transacción bancaria por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        TransaccionBancaria transaccionBancariaExistente = transaccionBancariaService.obtenerPorId(id);
        if (transaccionBancariaExistente != null) {
            transaccionBancariaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
