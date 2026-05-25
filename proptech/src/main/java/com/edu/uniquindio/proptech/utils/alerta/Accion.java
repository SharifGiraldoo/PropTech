package com.edu.uniquindio.proptech.utils.alerta;

import java.time.LocalDateTime;

public class Accion<T> {
    public enum TipoAccion {
        CREACION_CLIENTE, CREACION_INMUEBLE, CREACION_ASESOR,
        AGENDAR_VISITA, CAMBIO_ESTADO, EDICION_INMUEBLE,
        REGISTRO_OPERACION, REGISTRO_CONTRATO, ELIMINAR_INMUEBLE
    }

    private TipoAccion tipo;
    private T valorAnterior;
    private T valorNuevo;
    private String descripcion;
    private LocalDateTime fecha;

    public Accion(TipoAccion tipo, T valorAnterior, T valorNuevo, String descripcion) {
        this.tipo = tipo;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public TipoAccion getTipo() { return tipo; }
    public T getValorAnterior() { return valorAnterior; }
    public T getValorNuevo() { return valorNuevo; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFecha() { return fecha; }

    @Override
    public String toString() {
        return tipo + ": " + descripcion;
    }
}
