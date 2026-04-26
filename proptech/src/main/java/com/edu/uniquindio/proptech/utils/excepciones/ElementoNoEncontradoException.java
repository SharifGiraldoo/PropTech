package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepcion lanzada cuando un elemento no es encontrado.
 */
public class ElementoNoEncontradoException extends RuntimeException {
    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
