package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando un elemento buscado no existe en el sistema.
 * <p>
 * Se usa cuando se intenta obtener un {@code Cliente}, {@code Inmueble}
 * o {@code Asesor} que no está registrado.
 * </p>
 *
 * @author PropTech
 * @version 1.0
 */
public class ElementoNoEncontradoException extends RuntimeException {
    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del elemento no encontrado
     */
    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}