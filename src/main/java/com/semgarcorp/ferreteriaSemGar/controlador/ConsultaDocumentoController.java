package com.semgarcorp.ferreteriaSemGar.controlador;

import com.semgarcorp.ferreteriaSemGar.dto.ConsultaDocumentoDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.Cliente;
import com.semgarcorp.ferreteriaSemGar.servicio.ConsultaSunatReniecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta")
public class ConsultaDocumentoController {

    private final ConsultaSunatReniecService consultaService;

    public ConsultaDocumentoController(ConsultaSunatReniecService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<Cliente> consultarDocumento(@RequestBody ConsultaDocumentoDTO dto) {
        Cliente cliente = consultaService.consultarDocumento(
                dto.getNumeroDocumento(),
                dto.getTipoDocumento()
        );
        return ResponseEntity.ok(cliente);
    }
}