package com.semgarcorp.ferreteriaSemGar.modelo;

public enum Moneda {

    SOLES("1", "PEN", "Soles"),
    DOLARES("2", "USD", "Dolares"),
    EUROS("3", "EUR", "Euros"),
    LIBRA_ESTERLINA("4", "GBP", "Libras Esterlinas");

    private final String codigoNubefact;
    private final String codigoIso;
    private final String nombre;

    Moneda(String codigoNubefact, String codigoIso, String nombre) {
        this.codigoNubefact = codigoNubefact;
        this.codigoIso = codigoIso;
        this.nombre = nombre;
    }

    public String getCodigoNubefact() {
        return codigoNubefact;
    }

    public String getCodigoIso() {
        return codigoIso;
    }

    public String getNombre() {
        return nombre;
    }

    public static Moneda fromCodigo(String codigo) {
        for (Moneda m : Moneda.values()) {
            if (m.getCodigoNubefact().equals(codigo)) {
                return m;
            }
        }
        throw new IllegalArgumentException("Código de moneda no válido: " + codigo);
    }
}
