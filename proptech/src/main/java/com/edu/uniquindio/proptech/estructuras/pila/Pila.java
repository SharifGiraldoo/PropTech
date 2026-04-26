package com.edu.uniquindio.proptech.estructuras.pila;

/**
 * Interfaz que define el comportamiento de una pila generica.
 * Basada en el principio LIFO para manejo de acciones del sistema.
 *
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public interface Pila<T> {

    /**
     * Inserta un elemento en la pila.
     * @param dato elemento a insertar
     */
    void push(T dato);

    /**
     * Elimina y retorna el elemento superior.
     * @return elemento eliminado
     */
    T pop();

    /**
     * Retorna el elemento superior sin eliminarlo.
     * @return elemento en la cima
     */
    T peek();

    /**
     * Verifica si la pila esta vacia.
     * @return true si esta vacia
     */
    boolean estaVacia();

    /**
     * Retorna el tamanio de la pila.
     * @return numero de elementos
     */
    int tamanio();
}
