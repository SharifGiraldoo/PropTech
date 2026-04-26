package com.edu.uniquindio.proptech.estructuras.cola;

/**
 * Interfaz que define el comportamiento de una cola generica.
 * Basada en el principio FIFO para gestion de visitas del sistema.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public interface Cola<T> {

    /**
     * Inserta un elemento en la cola.
     * @param dato elemento a insertar
     */
    void encolar(T dato);

    /**
     * Elimina y retorna el primer elemento.
     * @return elemento eliminado
     */
    T desencolar();

    /**
     * Retorna el primer elemento sin eliminarlo.
     * @return elemento en el frente
     */
    T frente();

    /**
     * Verifica si la cola esta vacia.
     * @return true si esta vacia
     */
    boolean estaVacia();

    /**
     * Retorna el tamanio de la cola.
     * @return numero de elementos
     */
    int tamanio();
}
