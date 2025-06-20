package com.semgarcorp.ferreteriaSemGar.dto;

public class ComprobanteNubeFactDTO {
    private String enlace;
    private String cadenaParaCodigoQr;
    private String codigoDeBarras;
    private String enlaceDelPdf;
    private String enlaceDelXml;
    private Boolean aceptadaPorSunat;
    private String sunatDescription;

    // Getters y Setters
    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getCadenaParaCodigoQr() {
        return cadenaParaCodigoQr;
    }

    public void setCadenaParaCodigoQr(String cadenaParaCodigoQr) {
        this.cadenaParaCodigoQr = cadenaParaCodigoQr;
    }

    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public String getEnlaceDelPdf() {
        return enlaceDelPdf;
    }

    public void setEnlaceDelPdf(String enlaceDelPdf) {
        this.enlaceDelPdf = enlaceDelPdf;
    }

    public String getEnlaceDelXml() {
        return enlaceDelXml;
    }

    public void setEnlaceDelXml(String enlaceDelXml) {
        this.enlaceDelXml = enlaceDelXml;
    }

    public Boolean getAceptadaPorSunat() {
        return aceptadaPorSunat;
    }

    public void setAceptadaPorSunat(Boolean aceptadaPorSunat) {
        this.aceptadaPorSunat = aceptadaPorSunat;
    }

    public String getSunatDescription() {
        return sunatDescription;
    }

    public void setSunatDescription(String sunatDescription) {
        this.sunatDescription = sunatDescription;
    }
} 