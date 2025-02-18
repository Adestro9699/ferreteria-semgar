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

    // Obtener la lista de todos los parámetros
    @GetMapping
    public ResponseEntity<List<Parametro>> listar() {
        List<Parametro> parametros = parametroService.listar();
        return ResponseEntity.ok(parametros);
    }

    // Obtener un parámetro por su clave
    @GetMapping("/{clave}")
    public ResponseEntity<BigDecimal> obtenerValorPorClave(@PathVariable String clave) {
        try {
            BigDecimal valor = parametroService.obtenerValorPorClave(clave);
            return ResponseEntity.ok(valor); // Devuelve el valor como BigDecimal
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Parámetro no encontrado
        }
    }

    // Crear un nuevo parámetro
    @PostMapping
    public ResponseEntity<Parametro> guardar(@RequestBody Parametro parametro) {
        Parametro nuevoParametro = parametroService.guardar(parametro);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{clave}")
                .buildAndExpand(nuevoParametro.getClave()).toUri();

        return ResponseEntity.created(location).body(nuevoParametro);
    }

    // Actualizar un parámetro existente (PUT)
    @PutMapping("/{clave}")
    public ResponseEntity<Parametro> actualizar(@PathVariable String clave, @RequestBody Parametro parametro) {
        try {
            // Verificar si el parámetro existe (el servicio lanzará una excepción si no existe)
            parametroService.obtenerValorPorClave(clave);

            // Asegurar que la clave se mantenga para la actualización
            parametro.setClave(clave);

            // Actualizar el parámetro
            Parametro parametroActualizado = parametroService.actualizar(parametro);
            return ResponseEntity.ok(parametroActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Eliminar un parámetro por su clave
    @DeleteMapping("/{clave}")
    public ResponseEntity<Void> eliminar(@PathVariable String clave) {
        try {
            // Verificar si el parámetro existe (el servicio lanzará una excepción si no existe)
            parametroService.obtenerValorPorClave(clave);

            // Eliminar el parámetro
            parametroService.eliminar(clave);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Manejador de excepciones para RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}