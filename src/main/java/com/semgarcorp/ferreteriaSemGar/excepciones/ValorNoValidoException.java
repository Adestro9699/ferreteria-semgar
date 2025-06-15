package com.semgarcorp.ferreteriaSemGar.excepciones;

public class ValorNoValidoException extends RuntimeException {
    public ValorNoValidoException(String mensaje) {
        super(mensaje);
    }
} 