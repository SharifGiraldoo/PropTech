package com.edu.uniquindio.proptech.utils.excepciones;

/**
 * Excepción lanzada cuando un parámetro obligatorio es {@code null} o vacío.
 * <p>
 * Se usa en métodos de registro y búsqueda donde los argumentos
 * no pueden ser nulos ni cadenas en blanco.
 * </p>
 *
 * @author PropTech
 * @version 1.0
 */
public class ParametroVacioException extends RuntimeException {
    /**
     * Construye la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del parámetro inválido
     */
    public ParametroVacioException(String mensaje) {
        super(mensaje);
    }
}