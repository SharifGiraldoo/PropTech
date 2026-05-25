package com.edu.uniquindio.proptech.estructuras.cola;

public interface Cola<T> {
    void encolar(T elemento);
    T desencolar();
    T frente();
    boolean estaVacia();
    int tamanio();
}
