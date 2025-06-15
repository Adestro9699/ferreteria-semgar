package com.semgarcorp.ferreteriaSemGar.excepciones;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
} 