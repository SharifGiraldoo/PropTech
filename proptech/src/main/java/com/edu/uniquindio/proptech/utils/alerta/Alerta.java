package com.edu.uniquindio.proptech.utils.alerta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Alerta del sistema con nivel de prioridad.
 * Las alertas se gestionan mediante cola de prioridad (ColaPrioridadLista).
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Alerta implements Comparable<Alerta> {

    public enum NivelAlerta { BAJA, MEDIA, ALTA, CRITICA }

    private String id;
    private String mensaje;
    private NivelAlerta nivel;
    private LocalDateTime fecha;
    private boolean resuelta;
    private String tipo;

    public Alerta(String id, String mensaje, NivelAlerta nivel, String tipo) {
        this.id = id;
        this.mensaje = mensaje;
        this.nivel = nivel;
        this.tipo = tipo;
        this.fecha = LocalDateTime.now();
        this.resuelta = false;
    }

    public String getId() { return id; }
    public String getMensaje() { return mensaje; }
    public NivelAlerta getNivel() { return nivel; }
    public LocalDateTime getFecha() { return fecha; }
    public boolean isResuelta() { return resuelta; }
    public void setResuelta(boolean v) { this.resuelta = v; }
    public String getTipo() { return tipo; }

    public int getPrioridadNumerica() {
        return switch (nivel) {
            case CRITICA -> 4;
            case ALTA    -> 3;
            case MEDIA   -> 2;
            case BAJA    -> 1;
        };
    }

    @Override
    public int compareTo(Alerta otra) {
        return Integer.compare(this.getPrioridadNumerica(), otra.getPrioridadNumerica());
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + nivel + "] " + mensaje + " (" + fecha.format(fmt) + ")";
    }
}
