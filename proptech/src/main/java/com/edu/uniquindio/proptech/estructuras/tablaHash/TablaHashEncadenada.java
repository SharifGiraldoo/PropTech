package com.edu.uniquindio.proptech.estructuras.tablaHash;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Tabla hash con encadenamiento (separate chaining).
 * Complejidad promedio: O(1) inserción, búsqueda y eliminación.
 * Usada para acceso rápido a clientes, inmuebles y asesores.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class TablaHashEncadenada<K, V> implements TablaHash<K, V> {

    private static final int CAPACIDAD_DEFAULT = 16;
    private static final double FACTOR_CARGA = 0.75;

    private LinkedList<Entrada<K, V>>[] tabla;
    private int tamanio;
    private int capacidad;

    @SuppressWarnings("unchecked")
    public TablaHashEncadenada() {
        this.capacidad = CAPACIDAD_DEFAULT;
        this.tabla = new LinkedList[capacidad];
        this.tamanio = 0;
    }

    @SuppressWarnings("unchecked")
    public TablaHashEncadenada(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new LinkedList[capacidad];
        this.tamanio = 0;
    }

    private int hash(K clave) {
        return Math.abs(clave.hashCode() % capacidad);
    }

    @Override
    public void put(K clave, V valor) {
        int idx = hash(clave);
        if (tabla[idx] == null) tabla[idx] = new LinkedList<>();
        for (Entrada<K, V> e : tabla[idx]) {
            if (e.clave.equals(clave)) {
                e.valor = valor;
                return;
            }
        }
        tabla[idx].add(new Entrada<>(clave, valor));
        tamanio++;
        if ((double) tamanio / capacidad > FACTOR_CARGA) rehash();
    }

    @Override
    public V get(K clave) {
        int idx = hash(clave);
        if (tabla[idx] == null) return null;
        for (Entrada<K, V> e : tabla[idx]) {
            if (e.clave.equals(clave)) return e.valor;
        }
        return null;
    }

    /** Igual que get pero no lanza excepción si no existe. */
    public V getSafe(K clave) {
        return get(clave);
    }

    @Override
    public boolean eliminar(K clave) {
        int idx = hash(clave);
        if (tabla[idx] == null) return false;
        for (Entrada<K, V> e : tabla[idx]) {
            if (e.clave.equals(clave)) {
                tabla[idx].remove(e);
                tamanio--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contiene(K clave) {
        return get(clave) != null;
    }

    @Override
    public int tamanio() { return tamanio; }

    @Override
    public List<V> valores() {
        List<V> result = new ArrayList<>();
        for (LinkedList<Entrada<K, V>> bucket : tabla) {
            if (bucket != null) {
                for (Entrada<K, V> e : bucket) result.add(e.valor);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        capacidad *= 2;
        LinkedList<Entrada<K, V>>[] nueva = new LinkedList[capacidad];
        for (LinkedList<Entrada<K, V>> bucket : tabla) {
            if (bucket != null) {
                for (Entrada<K, V> e : bucket) {
                    int idx = Math.abs(e.clave.hashCode() % capacidad);
                    if (nueva[idx] == null) nueva[idx] = new LinkedList<>();
                    nueva[idx].add(e);
                }
            }
        }
        tabla = nueva;
    }

    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada(K c, V v) { this.clave = c; this.valor = v; }
    }
}
