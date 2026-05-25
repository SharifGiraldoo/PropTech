package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Visita {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private Cliente cliente;
    private Inmueble inmueble;
    private Asesor asesor;
    private LocalDateTime fechayHora;
    private EstadoVisita estado;
    private String observaciones;

    public Visita(Cliente cliente, Inmueble inmueble, Asesor asesor,
                  LocalDateTime fechayHora, EstadoVisita estado, String observaciones) {
        this.cliente = cliente; this.inmueble = inmueble; this.asesor = asesor;
        this.fechayHora = fechayHora; this.estado = estado;
        this.observaciones = observaciones == null ? "" : observaciones;
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente v) { this.cliente = v; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble v) { this.inmueble = v; }
    public Asesor getAsesor() { return asesor; }
    public void setAsesor(Asesor v) { this.asesor = v; }
    public LocalDateTime getFechayHora() { return fechayHora; }
    public void setFechayHora(LocalDateTime v) { this.fechayHora = v; }
    public EstadoVisita getEstado() { return estado; }
    public void setEstado(EstadoVisita v) { this.estado = v; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String v) { this.observaciones = v; }

    // ── Métodos del diagrama de clases ──────────────────────────────────────
    /** Confirma la visita (PENDIENTE → CONFIRMADA) */
    public void confirmar() {
        if (this.estado == EstadoVisita.PENDIENTE) {
            this.estado = EstadoVisita.CONFIRMADA;
        }
    }

    /** Cancela la visita */
    public void cancelar() {
        this.estado = EstadoVisita.CANCELADA;
    }

    /** Reprograma la visita a una nueva fecha/hora */
    public void reprogramar(LocalDateTime nuevaFechayHora) {
        this.fechayHora = nuevaFechayHora;
        this.estado = EstadoVisita.REPROGRAMADA;
    }

    @Override
    public String toString() {
        String f = fechayHora != null ? fechayHora.format(FMT) : "?";
        return "Visita[" + (cliente != null ? cliente.getNombre() : "?") +
               " → " + (inmueble != null ? inmueble.getCodigo() : "?") +
               " | " + f + " | " + estado + "]";
    }
}
