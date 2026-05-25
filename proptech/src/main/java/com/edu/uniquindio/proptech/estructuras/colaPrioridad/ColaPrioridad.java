package com.edu.uniquindio.proptech.estructuras.colaPrioridad;

public interface ColaPrioridad<T> {
    void insertar(T elemento);
    T extraerMaximo();
    T verMaximo();
    boolean estaVacia();
    int tamanio();
}
