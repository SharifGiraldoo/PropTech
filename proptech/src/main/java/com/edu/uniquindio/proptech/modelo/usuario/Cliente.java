package com.edu.uniquindio.proptech.modelo.usuario;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.inmueble.TipoInmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;

/**
 * Cliente interesado en comprar o arrendar inmuebles.
 * Mantiene historial de interacciones y favoritos (ListaSimple).
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Cliente extends Usuario {

    public enum TipoCliente { COMPRADOR, ARRENDATARIO, INVERSIONISTA }
    public enum EstadoBusqueda { BUSCANDO, PAUSADO, CERRADO }

    private TipoCliente tipoCliente;
    private double presupuesto;
    private String zonasInteres;
    private TipoInmueble tipoDeseado;
    private int minHabitaciones;
    private EstadoBusqueda estadoBusqueda;

    // Estructuras de datos del cliente
    private final ListaSimple<Inmueble> favoritos;
    private final ListaSimple<Inmueble> historialConsultas;
    private final ListaSimple<Visita>   historialVisitas;

    public Cliente(String id, String nombre, String correo, String telefono, String contrasenia,
                   TipoCliente tipoCliente, double presupuesto, String zonasInteres,
                   TipoInmueble tipoDeseado, int minHabitaciones) {
        super(id, nombre, correo, telefono, contrasenia);
        this.tipoCliente = tipoCliente;
        this.presupuesto = presupuesto;
        this.zonasInteres = zonasInteres;
        this.tipoDeseado = tipoDeseado;
        this.minHabitaciones = minHabitaciones;
        this.estadoBusqueda = EstadoBusqueda.BUSCANDO;
        this.favoritos = new ListaSimple<>();
        this.historialConsultas = new ListaSimple<>();
        this.historialVisitas = new ListaSimple<>();
    }

    @Override
    public String getRol() { return "cliente"; }

    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente v) { this.tipoCliente = v; }
    public double getPresupuesto() { return presupuesto; }
    public void setPresupuesto(double v) { this.presupuesto = v; }
    public String getZonasInteres() { return zonasInteres; }
    public void setZonasInteres(String v) { this.zonasInteres = v; }
    public TipoInmueble getTipoDeseado() { return tipoDeseado; }
    public void setTipoDeseado(TipoInmueble v) { this.tipoDeseado = v; }
    public int getMinHabitaciones() { return minHabitaciones; }
    public void setMinHabitaciones(int v) { this.minHabitaciones = v; }
    public EstadoBusqueda getEstadoBusqueda() { return estadoBusqueda; }
    public void setEstadoBusqueda(EstadoBusqueda v) { this.estadoBusqueda = v; }

    public ListaSimple<Inmueble> getFavoritos() { return favoritos; }
    public ListaSimple<Inmueble> getHistorialConsultas() { return historialConsultas; }
    public ListaSimple<Visita>   getHistorialVisitas() { return historialVisitas; }

    public void agregarFavorito(Inmueble i) { if (!favoritos.contiene(i)) favoritos.agregarFinal(i); }
    public void eliminarFavorito(Inmueble i) { favoritos.eliminar(i); }
    /** @deprecated use eliminarFavorito */
    public void quitarFavorito(Inmueble i) { eliminarFavorito(i); }
    public void registrarConsulta(Inmueble i) { historialConsultas.agregarInicio(i); }
    /** Agenda una visita y la registra en el historial */
    public void agendarVisita(Visita v) {
        historialVisitas.agregarFinal(v);
    }
    public void registrarVisita(Visita v) { agendarVisita(v); }
}
