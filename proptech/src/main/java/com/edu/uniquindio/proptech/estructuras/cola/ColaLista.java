package com.edu.uniquindio.proptech.estructuras.cola;

import com.edu.uniquindio.proptech.estructuras.lista.Nodo;

/**
 * Cola FIFO implementada con nodos enlazados.
 * Usada para programación de visitas.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class ColaLista<T> implements Cola<T> {

    private Nodo<T> frente;
    private Nodo<T> fin;
    private int tamanio;

    public ColaLista() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    @Override
    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) throw new java.util.NoSuchElementException("Cola vacía");
        T dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        tamanio--;
        return dato;
    }

    @Override
    public T frente() {
        if (estaVacia()) return null;
        return frente.dato;
    }

    @Override
    public boolean estaVacia() { return tamanio == 0; }

    @Override
    public int tamanio() { return tamanio; }

    /** Copia los primeros n elementos sin modificar la cola. */
    public java.util.List<T> peekN(int n) {
        java.util.List<T> result = new java.util.ArrayList<>();
        Nodo<T> actual = frente;
        int i = 0;
        while (actual != null && i < n) {
            result.add(actual.dato);
            actual = actual.siguiente;
            i++;
        }
        return result;
    }
}
