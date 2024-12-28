package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Nomina;
import com.semgarcorp.ferreteriaSemGar.servicio.NominaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/nominas")
public class NominaController {

    private final NominaService nominaService;

    public NominaController(NominaService nominaService) {
        this.nominaService = nominaService;
    }

    // Obtener la lista de todas las nóminas
    @GetMapping
    public List<Nomina> listar() {
        return nominaService.listar();
    }

    // Obtener una nómina por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Nomina> obtenerPorId(@PathVariable Long id) {
        Nomina nomina = nominaService.obtenerPorId(id);
        if (nomina != null) {
            return new ResponseEntity<>(nomina, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Crear una nueva nómina
    @PostMapping
    public ResponseEntity<Nomina> guardar(@RequestBody Nomina nomina) {
        // Guardar la nómina usando el servicio
        Nomina nuevaNomina = nominaService.guardar(nomina);

        // Crear la URI del recurso recién creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaNomina.getIdNomina()).toUri();

        // Devolver la respuesta con la URI en la cabecera Location
        return ResponseEntity.created(location).body(nuevaNomina);
    }

    // Actualizar una nómina existente (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Nomina> actualizar(@PathVariable Long id, @RequestBody Nomina nomina) {
        Nomina nominaExistente = nominaService.obtenerPorId(id);
        if (nominaExistente != null) {
            nomina.setIdNomina(id); // Aseguramos que el ID se mantenga para la actualización
            return new ResponseEntity<>(nominaService.actualizar(nomina), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar una nómina por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Nomina nominaExistente = nominaService.obtenerPorId(id);
        if (nominaExistente != null) {
            nominaService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
