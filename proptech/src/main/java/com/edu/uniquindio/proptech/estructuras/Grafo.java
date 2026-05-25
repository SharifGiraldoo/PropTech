package com.edu.uniquindio.proptech.estructuras;

import java.util.*;

/**
 * Grafo no dirigido con lista de adyacencia.
 * Usado para representar relaciones entre zonas inmobiliarias,
 * analizar movilidad comercial y detectar patrones de comportamiento.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Grafo {

    private final Map<String, List<Arista>> adyacencia;

    public Grafo() {
        this.adyacencia = new LinkedHashMap<>();
    }

    public void agregarNodo(String nodo) {
        if (nodo != null && !nodo.isBlank()) {
            adyacencia.putIfAbsent(nodo, new ArrayList<>());
        }
    }

    public void agregarArista(String origen, String destino) {
        agregarArista(origen, destino, 1);
    }

    public void agregarArista(String origen, String destino, int peso) {
        agregarNodo(origen);
        agregarNodo(destino);
        // Evitar duplicados
        boolean existe = adyacencia.get(origen).stream()
            .anyMatch(a -> a.destino.equals(destino));
        if (!existe) {
            adyacencia.get(origen).add(new Arista(destino, peso));
            adyacencia.get(destino).add(new Arista(origen, peso));
        }
    }

    public Set<String> getNodos() { return adyacencia.keySet(); }

    public List<Arista> getVecinos(String nodo) {
        return adyacencia.getOrDefault(nodo, new ArrayList<>());
    }

    public boolean existeNodo(String nodo) { return adyacencia.containsKey(nodo); }

    public int cantidadNodos() { return adyacencia.size(); }

    public static class Arista {
        public final String destino;
        public final int peso;
        public Arista(String d, int p) { this.destino = d; this.peso = p; }
    }
}
