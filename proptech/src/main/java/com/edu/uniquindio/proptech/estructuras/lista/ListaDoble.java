package com.edu.uniquindio.proptech.estructuras.lista;

import com.edu.uniquindio.proptech.utils.Excepciones.ElementoNoEncontradoException;
import com.edu.uniquindio.proptech.utils.Excepciones.ListaVaciaException;
import com.edu.uniquindio.proptech.utils.Excepciones.PosicionInvalidaException;

/**
 * Implementacion de una lista doblemente enlazada generica.
 * Permite inserciones y eliminaciones eficientes en ambos sentidos.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class ListaDoble<T> implements Lista<T> {

    private class Nodo {
        T dato;
        Nodo siguiente;
        Nodo anterior;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int tamanio;

    public ListaDoble() {
        cabeza = null;
        cola = null;
        tamanio = 0;
    }

    /** @return true si esta vacia */
    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /** @return numero de elementos */
    @Override
    public int tamanio() {
        return tamanio;
    }

    /** @param dato elemento a insertar */
    @Override
    public void agregarInicio(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            cabeza = cola = nuevo;
        } else {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        }
        tamanio++;
    }

    /** @param dato elemento a insertar */
    @Override
    public void agregarFinal(T dato) {
        Nodo nuevo = new Nodo(dato);

        if (estaVacia()) {
            cabeza = cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        tamanio++;
    }

    /** @param dato @param posicion */
    @Override
    public void agregarEnPosicion(T dato, int posicion) {
        if (posicion < 0 || posicion > tamanio)
            throw new PosicionInvalidaException("Posicion invalida");

        if (posicion == 0) {
            agregarInicio(dato);
            return;
        }
        if (posicion == tamanio) {
            agregarFinal(dato);
            return;
        }

        Nodo nuevo = new Nodo(dato);
        Nodo actual = cabeza;

        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        nuevo.anterior = actual.anterior;
        nuevo.siguiente = actual;
        actual.anterior.siguiente = nuevo;
        actual.anterior = nuevo;

        tamanio++;
    }

    /** @return elemento eliminado */
    @Override
    public T eliminarInicio() {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        T dato = cabeza.dato;

        if (cabeza == cola) {
            cabeza = cola = null;
        } else {
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
        }

        tamanio--;
        return dato;
    }

    /** @return elemento eliminado */
    @Override
    public T eliminarFinal() {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        T dato = cola.dato;

        if (cabeza == cola) {
            cabeza = cola = null;
        } else {
            cola = cola.anterior;
            cola.siguiente = null;
        }

        tamanio--;
        return dato;
    }

    /** @param dato @return eliminado */
    @Override
    public T eliminar(T dato) {
        if (estaVacia())
            throw new ListaVaciaException("Lista vacia");

        Nodo actual = cabeza;

        while (actual != null && !actual.dato.equals(dato)) {
            actual = actual.siguiente;
        }

        if (actual == null)
            throw new ElementoNoEncontradoException("Elemento no encontrado");

        if (actual == cabeza) return eliminarInicio();
        if (actual == cola) return eliminarFinal();

        actual.anterior.siguiente = actual.siguiente;
        actual.siguiente.anterior = actual.anterior;

        tamanio--;
        return actual.dato;
    }

    /** @param posicion @return elemento */
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

    /** @param dato @return indice */
    @Override
    public int buscar(T dato) {
        Nodo actual = cabeza;
        int i = 0;

        while (actual != null) {
            if (actual.dato.equals(dato)) return i;
            actual = actual.siguiente;
            i++;
        }

        return -1;
    }

    /** @param dato @return true si existe */
    @Override
    public boolean contiene(T dato) {
        return buscar(dato) != -1;
    }

    /** limpia la lista */
    @Override
    public void limpiar() {
        cabeza = cola = null;
        tamanio = 0;
    }
}
