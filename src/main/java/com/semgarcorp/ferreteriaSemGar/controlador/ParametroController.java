package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Parametro;
import com.semgarcorp.ferreteriaSemGar.servicio.ParametroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/parametros")
public class ParametroController {

    private final ParametroService parametroService;

    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    @GetMapping
    public ResponseEntity<List<Parametro>> listar() {
        List<Parametro> parametros = parametroService.listar();
        return ResponseEntity.ok(parametros);
    }

    @GetMapping("/{clave}")
    public ResponseEntity<Parametro> obtenerParametroPorClave(@PathVariable String clave) {
        try {
            Parametro parametro = parametroService.obtenerParametroPorClave(clave);
            return ResponseEntity.ok(parametro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/valor/{clave}")
    public ResponseEntity<BigDecimal> obtenerValorPorClave(@PathVariable String clave) {
        try {
            BigDecimal valor = parametroService.obtenerValorPorClave(clave);
            return ResponseEntity.ok(valor);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<Parametro> guardar(@RequestBody Parametro parametro) {
        Parametro nuevoParametro = parametroService.guardar(parametro);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{clave}")
                .buildAndExpand(nuevoParametro.getClave())
                .toUri();

        return ResponseEntity.created(location).body(nuevoParametro);
    }

    @PutMapping("/{clave}")
    public ResponseEntity<Parametro> actualizar(
            @PathVariable String clave,
            @RequestBody Parametro parametroActualizado) {
        try {
            // Obtener el parámetro existente
            Parametro parametroExistente = parametroService.obtenerParametroPorClave(clave);

            // Actualizar todos los campos
            parametroExistente.setValor(parametroActualizado.getValor());
            parametroExistente.setDescripcion(parametroActualizado.getDescripcion());
            parametroExistente.setObservaciones(parametroActualizado.getObservaciones());

            // Guardar cambios
            Parametro parametroGuardado = parametroService.actualizar(parametroExistente);
            return ResponseEntity.ok(parametroGuardado);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        try {
            parametroService.eliminar(clave);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}