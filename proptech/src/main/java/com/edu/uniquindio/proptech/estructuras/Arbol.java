package com.edu.uniquindio.proptech.estructuras;

import java.util.ArrayList;
import java.util.List;

/**
 * Árbol Binario de Búsqueda (BST) genérico.
 * Usado para indexación de precios con búsqueda eficiente por rango.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Arbol<T extends Comparable<T>> {

    private NodoArbol<T> raiz;

    public Arbol() { this.raiz = null; }

    public void insertar(T valor) {
        raiz = insertarRec(raiz, valor);
    }

    private NodoArbol<T> insertarRec(NodoArbol<T> nodo, T valor) {
        if (nodo == null) return new NodoArbol<>(valor);
        int cmp = valor.compareTo(nodo.valor);
        if (cmp < 0) nodo.izquierdo = insertarRec(nodo.izquierdo, valor);
        else if (cmp > 0) nodo.derecho = insertarRec(nodo.derecho, valor);
        return nodo;
    }

    public void eliminar(T valor) {
        raiz = eliminarRec(raiz, valor);
    }

    private NodoArbol<T> eliminarRec(NodoArbol<T> nodo, T valor) {
        if (nodo == null) return null;
        int cmp = valor.compareTo(nodo.valor);
        if (cmp < 0) nodo.izquierdo = eliminarRec(nodo.izquierdo, valor);
        else if (cmp > 0) nodo.derecho = eliminarRec(nodo.derecho, valor);
        else {
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            NodoArbol<T> sucesor = minimo(nodo.derecho);
            nodo.valor = sucesor.valor;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.valor);
        }
        return nodo;
    }

    private NodoArbol<T> minimo(NodoArbol<T> nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    public boolean contiene(T valor) {
        return contienRec(raiz, valor);
    }

    private boolean contienRec(NodoArbol<T> nodo, T valor) {
        if (nodo == null) return false;
        int cmp = valor.compareTo(nodo.valor);
        if (cmp < 0) return contienRec(nodo.izquierdo, valor);
        if (cmp > 0) return contienRec(nodo.derecho, valor);
        return true;
    }

    /** Recorrido inorden (ordena los valores de menor a mayor). */
    public List<T> inorden() {
        List<T> lista = new ArrayList<>();
        inordenRec(raiz, lista);
        return lista;
    }

    private void inordenRec(NodoArbol<T> nodo, List<T> lista) {
        if (nodo == null) return;
        inordenRec(nodo.izquierdo, lista);
        lista.add(nodo.valor);
        inordenRec(nodo.derecho, lista);
    }

    /** Busca valores en el rango [min, max]. */
    public List<T> buscarRango(T min, T max) {
        List<T> lista = new ArrayList<>();
        buscarRangoRec(raiz, min, max, lista);
        return lista;
    }

    private void buscarRangoRec(NodoArbol<T> nodo, T min, T max, List<T> lista) {
        if (nodo == null) return;
        if (nodo.valor.compareTo(min) > 0) buscarRangoRec(nodo.izquierdo, min, max, lista);
        if (nodo.valor.compareTo(min) >= 0 && nodo.valor.compareTo(max) <= 0) lista.add(nodo.valor);
        if (nodo.valor.compareTo(max) < 0) buscarRangoRec(nodo.derecho, min, max, lista);
    }

    public int altura() { return alturaRec(raiz); }
    private int alturaRec(NodoArbol<T> n) {
        if (n == null) return 0;
        return 1 + Math.max(alturaRec(n.izquierdo), alturaRec(n.derecho));
    }

    public int tamanio() { return tamanioRec(raiz); }
    private int tamanioRec(NodoArbol<T> n) {
        if (n == null) return 0;
        return 1 + tamanioRec(n.izquierdo) + tamanioRec(n.derecho);
    }

    public NodoArbol<T> getRaiz() { return raiz; }

    public static class NodoArbol<T> {
        public T valor;
        public NodoArbol<T> izquierdo, derecho;
        public NodoArbol(T v) { this.valor = v; }
    }
}
