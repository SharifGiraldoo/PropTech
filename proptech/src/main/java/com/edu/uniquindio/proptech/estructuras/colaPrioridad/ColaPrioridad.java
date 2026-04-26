package com.edu.uniquindio.proptech.estructuras.colaPrioridad;

import java.util.Comparator;

/**
 * Interfaz que define el comportamiento de una cola de prioridad generica.
 * Permite gestionar elementos segun un criterio de prioridad definido externamente.
 *
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public interface ColaPrioridad<T> {

    /**
     * Inserta un elemento segun su prioridad.
     * @param dato elemento a insertar
     */
    void encolar(T dato);

    /**
     * Elimina y retorna el elemento con mayor prioridad.
     * @return elemento eliminado
     */
    T desencolar();

    /**
     * Retorna el elemento con mayor prioridad sin eliminarlo.
     * @return elemento con mayor prioridad
     */
    T frente();

    /**
     * Verifica si la estructura esta vacia.
     * @return true si esta vacia
     */
    boolean estaVacia();

    /**
     * Retorna el tamanio de la estructura.
     * @return numero de elementos
     */
    int tamanio();
}