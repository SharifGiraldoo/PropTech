package com.edu.uniquindio.proptech.estructuras.colaPrioridad;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import java.util.Comparator;

/**
 * Cola de prioridad implementada con lista ordenada.
 * Inserción O(n), extracción O(1).
 * Usada para gestión de alertas según nivel de urgencia.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class ColaPrioridadLista<T> implements ColaPrioridad<T> {

    private final ListaSimple<T> lista;
    private final Comparator<T> comparador;

    public ColaPrioridadLista(Comparator<T> comparador) {
        this.lista = new ListaSimple<>();
        this.comparador = comparador;
    }

    @Override
    public void insertar(T elemento) {
        // Insertar en posición ordenada (mayor prioridad al frente)
        if (lista.estaVacia() || comparador.compare(elemento, lista.obtener(0)) >= 0) {
            lista.agregarInicio(elemento);
            return;
        }
        // Buscar posición correcta
        for (int i = 0; i < lista.tamanio() - 1; i++) {
            if (comparador.compare(elemento, lista.obtener(i + 1)) >= 0) {
                // Insertar después de i
                insertarEn(elemento, i + 1);
                return;
            }
        }
        lista.agregarFinal(elemento);
    }

    private void insertarEn(T elemento, int indice) {
        // Reconstruir lista con elemento en posición correcta
        java.util.List<T> tmp = lista.toJavaList();
        tmp.add(indice, elemento);
        while (!lista.estaVacia()) lista.eliminar(lista.obtener(0));
        for (T e : tmp) lista.agregarFinal(e);
    }

    @Override
    public T extraerMaximo() {
        if (estaVacia()) return null;
        T max = lista.obtener(0);
        lista.eliminar(max);
        return max;
    }

    @Override
    public T verMaximo() {
        if (estaVacia()) return null;
        return lista.obtener(0);
    }

    @Override
    public boolean estaVacia() { return lista.estaVacia(); }

    @Override
    public int tamanio() { return lista.tamanio(); }

    public ListaSimple<T> getLista() { return lista; }
}
