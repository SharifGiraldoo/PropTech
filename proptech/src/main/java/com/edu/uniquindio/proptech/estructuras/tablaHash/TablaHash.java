package com.edu.uniquindio.proptech.estructuras.tablaHash;

public interface TablaHash<K, V> {
    void put(K clave, V valor);
    V get(K clave);
    boolean eliminar(K clave);
    boolean contiene(K clave);
    int tamanio();
    java.util.List<V> valores();
}
