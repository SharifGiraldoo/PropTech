package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando se intenta registrar un {@code Cliente}
 * que ya existe en el sistema.
 */
public class ClienteYaRegistradoException extends RuntimeException {
    public ClienteYaRegistradoException(String mensaje) { super(mensaje); }
}