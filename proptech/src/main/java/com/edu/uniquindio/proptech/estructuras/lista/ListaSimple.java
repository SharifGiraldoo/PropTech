package com.edu.uniquindio.proptech.estructuras.lista;

import java.util.function.Consumer;

/**
 * Lista simplemente enlazada genérica.
 * Estructura de datos fundamental usada para historiales, favoritos,
 * contratos, operaciones y listas auxiliares.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class ListaSimple<T> implements Lista<T> {

    private Nodo<T> cabeza;
    private int tamanio;

    public ListaSimple() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    @Override
    public void agregarInicio(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;
    }

    @Override
    public void agregarFinal(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamanio++;
    }

    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamanio) throw new IndexOutOfBoundsException("Índice fuera de rango: " + indice);
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) actual = actual.siguiente;
        return actual.dato;
    }

    @Override
    public boolean eliminar(T elemento) {
        if (cabeza == null) return false;
        if (cabeza.dato.equals(elemento)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }
        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.dato.equals(elemento)) {
                actual.siguiente = actual.siguiente.siguiente;
                tamanio--;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.dato.equals(elemento)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public int tamanio() { return tamanio; }

    @Override
    public boolean estaVacia() { return tamanio == 0; }

    /** Itera sobre cada elemento. */
    public void forEach(Consumer<T> accion) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            accion.accept(actual.dato);
            actual = actual.siguiente;
        }
    }

    /** Convierte a java.util.List para compatibilidad. */
    public java.util.List<T> toJavaList() {
        java.util.List<T> lista = new java.util.ArrayList<>();
        Nodo<T> actual = cabeza;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Nodo<T> actual = cabeza;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) sb.append(", ");
            actual = actual.siguiente;
        }
        sb.append("]");
        return sb.toString();
    }
}
