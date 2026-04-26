package com.edu.uniquindio.proptech.estructuras.pila;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.utils.Excepciones.ListaVaciaException;

/**
 * Implementacion de una pila generica usando lista simplemente enlazada.
 * Permite gestionar acciones para deshacer cambios en el sistema.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class PilaLista<T> implements Pila<T> {

    private ListaSimple<T> lista;

    public PilaLista() {
        this.lista = new ListaSimple<>();
    }

    /**
     * Inserta un elemento en la pila.
     * @param dato elemento a insertar
     */
    @Override
    public void push(T dato) {
        lista.agregarInicio(dato);
    }

    /**
     * Elimina y retorna el elemento superior.
     * @return elemento eliminado
     * @throws ListaVaciaException si la pila esta vacia
     */
    @Override
    public T pop() {
        if (estaVacia())
            throw new ListaVaciaException("Pila vacia");
        return lista.eliminarInicio();
    }

    /**
     * Retorna el elemento superior sin eliminarlo.
     * @return elemento en la cima
     * @throws ListaVaciaException si la pila esta vacia
     */
    @Override
    public T peek() {
        if (estaVacia())
            throw new ListaVaciaException("Pila vacia");
        return lista.obtener(0);
    }

    /**
     * Verifica si la pila esta vacia.
     * @return true si esta vacia
     */
    @Override
    public boolean estaVacia() {
        return lista.estaVacia();
    }

    /**
     * Retorna el tamanio de la pila.
     * @return numero de elementos
     */
    @Override
    public int tamanio() {
        return lista.tamanio();
    }
}