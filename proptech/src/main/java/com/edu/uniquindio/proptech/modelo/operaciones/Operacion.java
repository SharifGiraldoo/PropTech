package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import java.time.LocalDate;

public class Operacion {
    private String id;
    private Inmueble inmueble;
    private Cliente cliente;
    private Asesor asesor;
    private TipoOperacion tipo;
    private LocalDate fecha;
    private double valor;
    private double comision;
    private EstadoOperacion estado;

    public Operacion(String id, Inmueble inmueble, Cliente cliente, Asesor asesor,
                     TipoOperacion tipo, LocalDate fecha, double valor,
                     double comision, EstadoOperacion estado) {
        this.id = id; this.inmueble = inmueble; this.cliente = cliente; this.asesor = asesor;
        this.tipo = tipo; this.fecha = fecha; this.valor = valor;
        this.comision = comision; this.estado = estado;
    }

    public String getId() { return id; }
    public void setId(String v) { this.id = v; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble v) { this.inmueble = v; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente v) { this.cliente = v; }
    public Asesor getAsesor() { return asesor; }
    public void setAsesor(Asesor v) { this.asesor = v; }
    public TipoOperacion getTipo() { return tipo; }
    public void setTipo(TipoOperacion v) { this.tipo = v; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate v) { this.fecha = v; }
    public double getValor() { return valor; }
    public void setValor(double v) { this.valor = v; }
    public double getComision() { return comision; }
    public void setComision(double v) { this.comision = v; }
    public EstadoOperacion getEstado() { return estado; }
    public void setEstado(EstadoOperacion v) { this.estado = v; }

    // ── Métodos del diagrama de clases ──────────────────────────────────────
    /** Calcula el monto real de la comisión sobre el valor de la operación */
    public double calcularComision() {
        return valor * (comision / 100.0);
    }

    /** Cambia el estado de la operación */
    public void cambiarEstado(String nuevoEstado) {
        try {
            this.estado = EstadoOperacion.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            // estado no reconocido — no cambia
        }
    }

    public double montoComision() { return calcularComision(); }

    @Override
    public String toString() {
        return id + " [" + tipo + " - " + (inmueble != null ? inmueble.getCodigo() : "?") + " - $" + valor + " - " + estado + "]";
    }
}
