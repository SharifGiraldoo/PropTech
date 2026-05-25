package com.edu.uniquindio.proptech.modelo.usuario;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;

/**
 * Asesor inmobiliario responsable de clientes e inmuebles.
 * Mantiene listas de inmuebles asignados y visitas atendidas.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Asesor extends Usuario {

    private String zonaAsignada;
    private final ListaSimple<Inmueble> inmueblesAsignados;
    private final ListaSimple<Visita>   visitasAtendidas;
    private int cierresRealizados;

    public Asesor(String id, String nombre, String correo, String telefono,
                  String zonaAsignada, String contrasenia) {
        super(id, nombre, correo, telefono, contrasenia);
        this.zonaAsignada = zonaAsignada;
        this.inmueblesAsignados = new ListaSimple<>();
        this.visitasAtendidas = new ListaSimple<>();
        this.cierresRealizados = 0;
    }

    @Override
    public String getRol() { return "asesor"; }

    public String getZonaAsignada() { return zonaAsignada; }
    public void setZonaAsignada(String v) { this.zonaAsignada = v; }
    public ListaSimple<Inmueble> getInmueblesAsignados() { return inmueblesAsignados; }
    public ListaSimple<Visita>   getVisitasAtendidas()   { return visitasAtendidas; }
    public int getCierresRealizados() { return cierresRealizados; }
    public void setCierresRealizados(int v) { this.cierresRealizados = v; }
    public void incrementarCierres() { this.cierresRealizados++; }

    public double getEfectividad() {
        int visitas = visitasAtendidas.tamanio();
        if (visitas == 0) return 0.0;
        return Math.round((double) cierresRealizados / visitas * 100.0 * 10) / 10.0;
    }

    // ── Métodos del diagrama de clases ──────────────────────────────────────
    /** Asigna un inmueble a este asesor */
    public void asignarInmueble(Inmueble inmueble) {
        if (inmueble != null && !inmueblesAsignados.contiene(inmueble)) {
            inmueblesAsignados.agregarFinal(inmueble);
            inmueble.setAsesor(this);
        }
    }

    /** Registra una visita agendada para este asesor */
    public void registrarVisita(Visita visita) {
        if (visita != null) visitasAtendidas.agregarFinal(visita);
    }
}
