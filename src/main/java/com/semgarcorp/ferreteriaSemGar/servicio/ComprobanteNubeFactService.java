package com.semgarcorp.ferreteriaSemGar.servicio;

import com.semgarcorp.ferreteriaSemGar.dto.ComprobanteNubeFactDTO;
import com.semgarcorp.ferreteriaSemGar.modelo.ComprobanteNubeFact;
import com.semgarcorp.ferreteriaSemGar.repositorio.ComprobanteNubeFactRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ComprobanteNubeFactService {

    private final ComprobanteNubeFactRepository comprobanteNubeFactRepository;

    public ComprobanteNubeFactService(ComprobanteNubeFactRepository comprobanteNubeFactRepository) {
        this.comprobanteNubeFactRepository = comprobanteNubeFactRepository;
    }

    public ComprobanteNubeFactDTO obtenerComprobantePorVenta(Integer idVenta) {
        ComprobanteNubeFact comprobante = comprobanteNubeFactRepository.findByVentaIdVenta(idVenta);
        
        if (comprobante == null) {
            throw new EntityNotFoundException("No se encontró el comprobante para la venta con ID: " + idVenta);
        }

        return convertirADTO(comprobante);
    }

    private ComprobanteNubeFactDTO convertirADTO(ComprobanteNubeFact comprobante) {
        ComprobanteNubeFactDTO dto = new ComprobanteNubeFactDTO();
        dto.setEnlace(comprobante.getEnlace());
        dto.setCadenaParaCodigoQr(comprobante.getCadenaParaCodigoQr());
        dto.setCodigoDeBarras(comprobante.getCodigoDeBarras());
        dto.setEnlaceDelPdf(comprobante.getEnlaceDelPdf());
        dto.setEnlaceDelXml(comprobante.getEnlaceDelXml());
        dto.setAceptadaPorSunat(comprobante.getAceptadaPorSunat());
        dto.setSunatDescription(comprobante.getSunatDescription());
        return dto;
    }
} 