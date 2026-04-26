package com.edu.uniquindio.proptech.estructuras.lista;

/**
 * Interfaz que define el comportamiento de una lista generica.
 * Representa el TAD Lista con operaciones basicas como insercion, eliminacion y busqueda.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public interface Lista<T> {

    /**
     * Verifica si la lista esta vacia.
     * @return true si esta vacia, false en caso contrario
     */
    boolean estaVacia();

    /**
     * Obtiene el tamanio de la lista.
     * @return numero de elementos
     */
    int tamanio();

    /**
     * Agrega un elemento al inicio de la lista.
     * @param dato elemento a insertar
     */
    void agregarInicio(T dato);

    /**
     * Agrega un elemento al final de la lista.
     * @param dato elemento a insertar
     */
    void agregarFinal(T dato);

    /**
     * Agrega un elemento en una posicion especifica.
     * @param dato elemento a insertar
     * @param posicion indice donde se insertara
     */
    void agregarEnPosicion(T dato, int posicion);

    /**
     * Elimina el primer elemento de la lista.
     * @return elemento eliminado
     */
    T eliminarInicio();

    /**
     * Elimina el ultimo elemento de la lista.
     * @return elemento eliminado
     */
    T eliminarFinal();

    /**
     * Elimina un elemento especifico de la lista.
     * @param dato elemento a eliminar
     * @return elemento eliminado o null si no existe
     */
    T eliminar(T dato);

    /**
     * Obtiene un elemento por su posicion.
     * @param posicion indice del elemento
     * @return elemento encontrado
     */
    T obtener(int posicion);

    /**
     * Busca la posicion de un elemento.
     * @param dato elemento a buscar
     * @return indice del elemento o -1 si no existe
     */
    int buscar(T dato);

    /**
     * Verifica si un elemento existe en la lista.
     * @param dato elemento a verificar
     * @return true si existe, false en caso contrario
     */
    boolean contiene(T dato);

    /**
     * Elimina todos los elementos de la lista.
     */
    void limpiar();
}