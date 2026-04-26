package com.edu.uniquindio.proptech.estructuras.colaPrioridad;

import com.edu.uniquindio.proptech.utils.excepciones.ListaVaciaException;

import java.util.Comparator;

/**
 * Implementacion de una cola de prioridad generica usando lista simplemente enlazada ordenada.
 * Permite gestionar elementos segun diferentes criterios (urgencia, demanda, prioridad, etc.).
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class ColaPrioridadLista<T> implements ColaPrioridad<T> {

    /**
     * Nodo interno de la estructura.
     */
    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo cabeza;
    private int tamanio;
    private Comparator<T> comparador;

    public ColaPrioridadLista(Comparator<T> comparador) {
        this.comparador = comparador;
        this.cabeza = null;
        this.tamanio = 0;
    }

    /**
     * Inserta un elemento en la posicion correspondiente segun prioridad.
     * @param dato elemento a insertar
     */
    @Override
    public void encolar(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            cabeza = nuevo;
        } else if (comparador.compare(dato, cabeza.dato) > 0) {
            nuevo.siguiente = cabeza;
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;

            while (actual.siguiente != null &&
                    comparador.compare(dato, actual.siguiente.dato) <= 0) {
                actual = actual.siguiente;
            }

            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }

        tamanio++;
    }

    /**
     * Elimina y retorna el elemento con mayor prioridad.
     * @return elemento eliminado
     * @throws ListaVaciaException si la estructura esta vacia
     */
    @Override
    public T desencolar() {
        if (estaVacia())
            throw new ListaVaciaException("Cola de prioridad vacia");

        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        tamanio--;
        return dato;
    }

    /**
     * Retorna el elemento con mayor prioridad sin eliminarlo.
     * @return elemento con mayor prioridad
     * @throws ListaVaciaException si la estructura esta vacia
     */
    @Override
    public T frente() {
        if (estaVacia())
            throw new ListaVaciaException("Cola de prioridad vacia");

        return cabeza.dato;
    }

    /**
     * Verifica si la estructura esta vacia.
     * @return true si esta vacia
     */
    @Override
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     * Retorna el tamanio de la estructura.
     * @return numero de elementos
     */
    @Override
    public int tamanio() {
        return tamanio;
    }
}
