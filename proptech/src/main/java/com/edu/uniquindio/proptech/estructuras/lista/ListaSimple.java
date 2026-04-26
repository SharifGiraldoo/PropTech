package com.edu.uniquindio.proptech.estructuras.lista;

import com.edu.uniquindio.proptech.utils.excepciones.ElementoNoEncontradoException;
import com.edu.uniquindio.proptech.utils.excepciones.ListaVaciaException;
import com.edu.uniquindio.proptech.utils.excepciones.PosicionInvalidaException;

/**
 * Implementacion de una lista simplemente enlazada generica.
 * Permite almacenar elementos de forma dinamica con operaciones basicas
 * como insercion, eliminacion y busqueda.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class ListaSimple<T> implements Lista<T> {

    /**
     * Nodo interno de la lista.
     */
    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;
    private int tamanio;

    public ListaSimple() {
        cabeza = null;
        tamanio = 0;
    }

    /** @return true si la lista esta vacia */
    @Override
    public boolean estaVacia() {
        return cabeza == null;
    }

    /** @return numero de elementos */
    @Override
    public int tamanio() {
        return tamanio;
    }

    /** @param dato elemento a insertar al inicio */
    @Override
    public void agregarInicio(T dato) {
        Nodo nuevo = new Nodo(dato);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;
    }

    /** @param dato elemento a insertar al final */
    @Override
    public void agregarFinal(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    /** @param dato elemento @param posicion indice */
    @Override
    public void agregarEnPosicion(T dato, int posicion) {
        if (posicion < 0 || posicion > tamanio)
            throw new PosicionInvalidaException("Posicion invalida");

        if (posicion == 0) {
            agregarInicio(dato);
            return;
        }

        Nodo nuevo = new Nodo(dato);
        Nodo actual = cabeza;

        for (int i = 0; i < posicion - 1; i++) {
            actual = actual.siguiente;
        }

        nuevo.siguiente = actual.siguiente;
        actual.siguiente = nuevo;
        tamanio++;
    }

    /** @return elemento eliminado */
    @Override
    public T eliminarInicio() {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        T dato = cabeza.dato;
        cabeza = cabeza.siguiente;
        tamanio--;
        return dato;
    }

    /** @return elemento eliminado */
    @Override
    public T eliminarFinal() {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        if (cabeza.siguiente == null) {
            T dato = cabeza.dato;
            cabeza = null;
            tamanio--;
            return dato;
        }

        Nodo actual = cabeza;
        while (actual.siguiente.siguiente != null) {
            actual = actual.siguiente;
        }

        T dato = actual.siguiente.dato;
        actual.siguiente = null;
        tamanio--;
        return dato;
    }

    /** @param dato a eliminar @return elemento eliminado */
    @Override
    public T eliminar(T dato) {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        if (cabeza.dato.equals(dato)) {
            return eliminarInicio();
        }

        Nodo actual = cabeza;

        while (actual.siguiente != null && !actual.siguiente.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente == null)
            throw new ElementoNoEncontradoException("Elemento no encontrado");

        T eliminado = actual.siguiente.dato;
        actual.siguiente = actual.siguiente.siguiente;
        tamanio--;
        return eliminado;
    }

    /** @param posicion indice @return elemento */
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio)
            throw new PosicionInvalidaException("Posicion invalida");

        Nodo actual = cabeza;

        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        return actual.dato;
    }

    /** @param dato a buscar @return indice o -1 */
    @Override
    public int buscar(T dato) {
        Nodo actual = cabeza;
        int indice = 0;

        while (actual != null) {
            if (actual.dato.equals(dato)) {
                return indice;
            }
            actual = actual.siguiente;
            indice++;
        }

        return -1;
    }

    /** @param dato a verificar @return true si existe */
    @Override
    public boolean contiene(T dato) {
        return buscar(dato) != -1;
    }

    /** elimina todos los elementos */
    @Override
    public void limpiar() {
        cabeza = null;
        tamanio = 0;
    }
}
