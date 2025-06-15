package com.semgarcorp.ferreteriaSemGar.excepciones;

public class CampoRequeridoException extends RuntimeException {
    public CampoRequeridoException(String mensaje) {
        super(mensaje);
    }
} 