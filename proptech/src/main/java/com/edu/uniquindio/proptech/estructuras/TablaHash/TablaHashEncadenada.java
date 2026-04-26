package com.edu.uniquindio.proptech.estructuras.TablaHash;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.utils.excepciones.ElementoNoEncontradoException;

/**
 * Implementacion de una tabla hash generica usando encadenamiento.
 * Utiliza listas simplemente enlazadas para manejar colisiones.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class TablaHashEncadenada<K, V> implements TablaHash<K, V> {

    /**
     * Clase interna que representa un par clave-valor.
     */
    private class Entry {
        K clave;
        V valor;

        Entry(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
        }
    }

    private ListaSimple<Entry>[] buckets;
    private int capacidad;
    private int tamanio;

    @SuppressWarnings("unchecked")
    public TablaHashEncadenada(int capacidad) {
        this.capacidad = capacidad;
        this.buckets = new ListaSimple[capacidad];
        this.tamanio = 0;
    }

    /**
     * Calcula el indice en el arreglo para una clave.
     * @param clave clave
     * @return indice
     */
    private int hash(K clave) {
        return Math.abs(clave.hashCode()) % capacidad;
    }

    /**
     * Inserta o actualiza un valor asociado a una clave.
     */
    @Override
    public void put(K clave, V valor) {
        int indice = hash(clave);

        if (buckets[indice] == null) {
            buckets[indice] = new ListaSimple<>();
        }

        ListaSimple<Entry> lista = buckets[indice];

        for (int i = 0; i < lista.tamanio(); i++) {
            Entry entry = lista.obtener(i);
            if (entry.clave.equals(clave)) {
                entry.valor = valor;
                return;
            }
        }

        lista.agregarFinal(new Entry(clave, valor));
        tamanio++;
    }

    /**
     * Obtiene el valor asociado a una clave.
     */
    @Override
    public V get(K clave) {
        int indice = hash(clave);

        ListaSimple<Entry> lista = buckets[indice];
        if (lista == null)
            throw new ElementoNoEncontradoException("Clave no encontrada");

        for (int i = 0; i < lista.tamanio(); i++) {
            Entry entry = lista.obtener(i);
            if (entry.clave.equals(clave)) {
                return entry.valor;
            }
        }

        throw new ElementoNoEncontradoException("Clave no encontrada");
    }

    /**
     * Elimina un elemento por su clave.
     */
    @Override
    public V remove(K clave) {
        int indice = hash(clave);

        ListaSimple<Entry> lista = buckets[indice];
        if (lista == null)
            throw new ElementoNoEncontradoException("Clave no encontrada");

        for (int i = 0; i < lista.tamanio(); i++) {
            Entry entry = lista.obtener(i);
            if (entry.clave.equals(clave)) {
                lista.eliminar(entry);
                tamanio--;
                return entry.valor;
            }
        }

        throw new ElementoNoEncontradoException("Clave no encontrada");
    }

    /**
     * Verifica si existe una clave.
     */
    @Override
    public boolean contieneClave(K clave) {
        int indice = hash(clave);

        ListaSimple<Entry> lista = buckets[indice];
        if (lista == null) return false;

        for (int i = 0; i < lista.tamanio(); i++) {
            if (lista.obtener(i).clave.equals(clave)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retorna el tamanio de la tabla.
     */
    @Override
    public int tamanio() {
        return tamanio;
    }

    /**
     * Verifica si esta vacia.
     */
    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }
}