package com.semgarcorp.ferreteriaSemGar.modelo;

public enum Moneda {

    SOLES("1", "PEN"),
    DOLARES("2", "USD"),
    EUROS("3", "EUR"),
    LIBRA_ESTERLINA("4", "GBP");

    private final String codigoNubefact;
    private final String codigoIso;

    Moneda(String codigoNubefact, String codigoIso) {
        this.codigoNubefact = codigoNubefact;
        this.codigoIso = codigoIso;
    }

    public String getCodigoNubefact() {
        return codigoNubefact;
    }

    public String getCodigoIso() {
        return codigoIso;
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
