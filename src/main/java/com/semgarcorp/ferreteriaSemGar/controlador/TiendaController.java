package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.modelo.Tienda;
import com.semgarcorp.ferreteriaSemGar.servicio.TiendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/tiendas")
public class TiendaController {

        private final TiendaService tiendaService;

        public TiendaController(TiendaService tiendaService) {
            this.tiendaService = tiendaService;
        }

        @GetMapping
        public List<Tienda> listar() {
            return tiendaService.listar();
        }

        @GetMapping("/{id}")
        public ResponseEntity<Tienda> obtenerPorId(@PathVariable Integer id) {
            Tienda tienda = tiendaService.obtenerPorId(id);
            if (tienda != null) {
                return ResponseEntity.ok(tienda); // Respuesta simplificada con 200 OK
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Respuesta con 404 Not Found
        }

        @PostMapping
        public ResponseEntity<Tienda> guardar(@RequestBody Tienda tienda) {
            Tienda nuevaTienda = tiendaService.guardar(tienda);

            // Crear la URI del recurso recién creado
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevaTienda.getIdTienda()).toUri();

            // Devolver la respuesta con la URI en la cabecera Location y el objeto creado en el cuerpo
            return ResponseEntity.created(location).body(nuevaTienda);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Tienda> actualizar(@PathVariable Integer id, @RequestBody Tienda tienda) {
            Tienda tiendaExistente = tiendaService.obtenerPorId(id);

            if (tiendaExistente != null) {
                tienda.setIdTienda(id);

                Tienda tiendaActualizado = tiendaService.actualizar(tienda);

                return ResponseEntity.ok(tiendaActualizado); // Usamos el metodo estático "ok" para la respuesta exitosa
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Usamos "status" para construir la respuesta 404
            }
        }

        // Eliminar un inventario por su ID
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
            Tienda tiendaExistente = tiendaService.obtenerPorId(id);
            if (tiendaExistente != null) {
                tiendaService.eliminar(id);
                return ResponseEntity.noContent().build(); // Respuesta sin contenido
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra, 404
        }
}
