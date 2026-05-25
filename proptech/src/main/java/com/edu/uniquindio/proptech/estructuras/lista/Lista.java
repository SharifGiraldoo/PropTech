package com.edu.uniquindio.proptech.estructuras.lista;

public interface Lista<T> {
    void agregarInicio(T elemento);
    void agregarFinal(T elemento);
    T obtener(int indice);
    boolean eliminar(T elemento);
    boolean contiene(T elemento);
    int tamanio();
    boolean estaVacia();
}
