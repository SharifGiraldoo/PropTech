package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando se intenta operar sobre un {@code Inmueble}
 * que no está disponible (ya vendido, arrendado o inactivo).
 */
public class InmuebleNoDisponibleException extends RuntimeException {
    public InmuebleNoDisponibleException(String mensaje) { super(mensaje); }
}