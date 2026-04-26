package com.edu.uniquindio.proptech.utils.Excepciones;

/**
 * Excepcion lanzada cuando la posicion es invalida.
 */
public class PosicionInvalidaException extends RuntimeException {
    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
