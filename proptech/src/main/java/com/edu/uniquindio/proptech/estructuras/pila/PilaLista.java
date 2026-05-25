package com.edu.uniquindio.proptech.estructuras.pila;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;

/**
 * Pila LIFO implementada sobre ListaSimple.
 * Usada para historial de acciones (deshacer cambios).
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class PilaLista<T> implements Pila<T> {

    private final ListaSimple<T> lista;

    public PilaLista() {
        this.lista = new ListaSimple<>();
    }

    @Override
    public void push(T elemento) {
        lista.agregarInicio(elemento);
    }

    @Override
    public T pop() {
        if (estaVacia()) throw new java.util.EmptyStackException();
        T tope = lista.obtener(0);
        lista.eliminar(tope);
        return tope;
    }

    @Override
    public T peek() {
        if (estaVacia()) return null;
        return lista.obtener(0);
    }

    @Override
    public boolean estaVacia() { return lista.estaVacia(); }

    @Override
    public int tamanio() { return lista.tamanio(); }

    public ListaSimple<T> getLista() { return lista; }
}
