package com.semgarcorp.ferreteriaSemGar.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "comprobantes_nubefact")
public class ComprobanteNubeFact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idComprobanteNubeFact;

    @OneToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;

    @Column(name = "enlace")
    private String enlace;

    @Column(name = "cadena_qr", length = 500)
    private String cadenaParaCodigoQr;

    @Column(name = "codigo_barras", length = 500)
    private String codigoDeBarras;

    @Column(name = "enlace_pdf")
    private String enlaceDelPdf;

    @Column(name = "enlace_xml")
    private String enlaceDelXml;

    @Column(name = "enlace_cdr")
    private String enlaceDelCdr;

    @Column(name = "aceptada_por_sunat")
    private Boolean aceptadaPorSunat;

    @Column(name = "sunat_description")
    private String sunatDescription;

    @Column(name = "sunat_note")
    private String sunatNote;

    @Column(name = "sunat_responsecode")
    private String sunatResponsecode;

    @Column(name = "codigo_hash")
    private String codigoHash;

    @Column(name = "digest_value")
    private String digestValue;

    @Column(name = "key_nubefact")
    private String key;

    // Getters y Setters

    public Integer getIdComprobanteNubeFact() {
        return idComprobanteNubeFact;
    }

    public void setIdComprobanteNubeFact(Integer idComprobanteNubeFact) {
        this.idComprobanteNubeFact = idComprobanteNubeFact;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

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

    public String getEnlaceDelCdr() {
        return enlaceDelCdr;
    }

    public void setEnlaceDelCdr(String enlaceDelCdr) {
        this.enlaceDelCdr = enlaceDelCdr;
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

    public String getSunatNote() {
        return sunatNote;
    }

    public void setSunatNote(String sunatNote) {
        this.sunatNote = sunatNote;
    }

    public String getSunatResponsecode() {
        return sunatResponsecode;
    }

    public void setSunatResponsecode(String sunatResponsecode) {
        this.sunatResponsecode = sunatResponsecode;
    }

    public String getCodigoHash() {
        return codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public String getDigestValue() {
        return digestValue;
    }

    public void setDigestValue(String digestValue) {
        this.digestValue = digestValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
