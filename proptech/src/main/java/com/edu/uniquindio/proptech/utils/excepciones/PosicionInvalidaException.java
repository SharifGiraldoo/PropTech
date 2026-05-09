package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando se accede a una posición fuera del rango
 * válido en una estructura de datos.
 * <p>
 * Ejemplo: acceder al índice {@code -1} o a un índice mayor al tamaño
 * de la lista.
 * </p>
 *
 * @author PropTech
 * @version 1.0
 */
public class PosicionInvalidaException extends RuntimeException {
    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción de la posición inválida
     */
    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}