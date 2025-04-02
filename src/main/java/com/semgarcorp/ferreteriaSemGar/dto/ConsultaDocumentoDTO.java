package com.semgarcorp.ferreteriaSemGar.dto;

public class ConsultaDocumentoDTO {
    private String numeroDocumento;
    private String tipoDocumento; // "DNI" o "RUC"

    public ConsultaDocumentoDTO() {
    }

    // Constructor
    public ConsultaDocumentoDTO(String numeroDocumento, String tipoDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
    }

    // Getters y Setters
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}