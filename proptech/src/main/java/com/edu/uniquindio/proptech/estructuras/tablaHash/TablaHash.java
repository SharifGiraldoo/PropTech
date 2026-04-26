package com.edu.uniquindio.proptech.estructuras.tablaHash;

/**
 * Interfaz que define el comportamiento de una tabla hash generica.
 * Permite almacenar pares clave-valor para acceso rapido, conteo y agrupacion.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public interface TablaHash<K, V> {

    /**
     * Inserta o actualiza un valor asociado a una clave.
     * @param clave clave del elemento
     * @param valor valor a almacenar
     */
    void put(K clave, V valor);

    /**
     * Obtiene el valor asociado a una clave.
     * @param clave clave a buscar
     * @return valor asociado
     */
    V get(K clave);

    /**
     * Elimina un elemento por su clave.
     * @param clave clave del elemento
     * @return valor eliminado
     */
    V remove(K clave);

    /**
     * Verifica si existe una clave.
     * @param clave clave a verificar
     * @return true si existe
     */
    boolean contieneClave(K clave);

    /**
     * Retorna el tamanio de la tabla.
     * @return numero de elementos
     */
    int tamanio();

    /**
     * Verifica si esta vacia.
     * @return true si esta vacia
     */
    boolean estaVacia();
}
