package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando una {@code Visita} tiene datos inválidos,
 * como fecha pasada o asesor no disponible.
 */
public class VisitaInvalidaException extends RuntimeException {
    public VisitaInvalidaException(String mensaje) { super(mensaje); }
}