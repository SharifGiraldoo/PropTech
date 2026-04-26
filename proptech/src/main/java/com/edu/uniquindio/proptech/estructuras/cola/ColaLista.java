package com.edu.uniquindio.proptech.estructuras.cola;

import com.edu.uniquindio.proptech.utils.excepciones.ListaVaciaException;

/**
 * Implementacion de una cola generica usando lista simplemente enlazada.
 * Permite gestionar visitas en orden de llegada.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class ColaLista<T> implements Cola<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int tamanio;

    public ColaLista() {
        frente = fin = null;
        tamanio = 0;
    }

    /**
     * Inserta un elemento en la cola.
     * @param dato elemento a insertar
     */
    @Override
    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    /**
     * Elimina y retorna el primer elemento.
     * @return elemento eliminado
     * @throws ListaVaciaException si la cola esta vacia
     */
    @Override
    public T desencolar() {
        if (estaVacia())
            throw new ListaVaciaException("Cola vacia");

        T dato = frente.dato;
        frente = frente.siguiente;

        if (frente == null) {
            fin = null;
        }

        tamanio--;
        return dato;
    }

    /**
     * Retorna el primer elemento sin eliminarlo.
     * @return elemento en el frente
     * @throws ListaVaciaException si la cola esta vacia
     */
    @Override
    public T frente() {
        if (estaVacia())
            throw new ListaVaciaException("Cola vacia");

        return frente.dato;
    }

    /**
     * Verifica si la cola esta vacia.
     * @return true si esta vacia
     */
    @Override
    public boolean estaVacia() {
        return frente == null;
    }

    /**
     * Retorna el tamanio de la cola.
     * @return numero de elementos
     */
    @Override
    public int tamanio() {
        return tamanio;
    }
}
