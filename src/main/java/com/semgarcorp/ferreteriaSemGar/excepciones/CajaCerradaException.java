package com.semgarcorp.ferreteriaSemGar.excepciones;

public class CajaCerradaException extends RuntimeException {
    public CajaCerradaException(String mensaje) {
        super(mensaje);
    }
} 