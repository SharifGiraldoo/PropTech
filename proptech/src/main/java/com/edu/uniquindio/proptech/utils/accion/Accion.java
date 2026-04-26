package com.edu.uniquindio.proptech.utils.accion;

/**
 * Representa una accion realizada en el sistema.
 * Almacena el estado anterior y el nuevo para permitir deshacer cambios.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public class Accion<T> {

    private TipoAccion tipo;
    private T estadoAnterior;
    private T estadoNuevo;

    public Accion(TipoAccion tipo, T estadoAnterior, T estadoNuevo) {
        this.tipo = tipo;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
    }

    /**
     * Obtiene el tipo de accion.
     * @return tipo de accion
     */
    public TipoAccion getTipo() {
        return tipo;
    }

    /**
     * Obtiene el estado anterior.
     * @return estado previo al cambio
     */
    public T getEstadoAnterior() {
        return estadoAnterior;
    }

    /**
     * Obtiene el estado nuevo.
     * @return estado posterior al cambio
     */
    public T getEstadoNuevo() {
        return estadoNuevo;
    }
}
