package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepcion lanzada cuando la lista esta vacia.
 */
public class ListaVaciaException extends RuntimeException {
    public ListaVaciaException(String mensaje) {
        super(mensaje);
    }
}
