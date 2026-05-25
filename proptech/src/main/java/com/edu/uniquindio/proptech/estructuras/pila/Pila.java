package com.edu.uniquindio.proptech.estructuras.pila;

public interface Pila<T> {
    void push(T elemento);
    T pop();
    T peek();
    boolean estaVacia();
    int tamanio();
}
