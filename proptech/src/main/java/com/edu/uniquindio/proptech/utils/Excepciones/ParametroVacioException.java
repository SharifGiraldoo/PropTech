package com.edu.uniquindio.proptech.utils.Excepciones;

/**
 * Excepcion lanzada cuando el parametro esta vacio.
 */
public class ParametroVacioException extends RuntimeException {
    public ParametroVacioException(String message) {
        super(message);
    }
}
