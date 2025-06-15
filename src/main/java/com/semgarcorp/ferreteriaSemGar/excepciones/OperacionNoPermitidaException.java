package com.semgarcorp.ferreteriaSemGar.excepciones;

public class OperacionNoPermitidaException extends RuntimeException {
    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
} 