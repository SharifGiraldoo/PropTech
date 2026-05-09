package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando se intenta operar sobre una estructura
 * de datos vacía (lista, cola, pila, etc.).
 * <p>
 * Ejemplo: desencolar una {@code Cola} vacía o hacer pop en una {@code Pila} vacía.
 * </p>
 *
 * @author PropTech
 * @version 1.0
 */
public class ListaVaciaException extends RuntimeException {
    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción de la estructura vacía
     */
    public ListaVaciaException(String mensaje) {
        super(mensaje);
    }
}