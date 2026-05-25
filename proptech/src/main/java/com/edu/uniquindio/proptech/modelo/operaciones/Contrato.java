package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import java.time.LocalDate;

public class Contrato {
    public enum TipoContrato { ARRIENDO, VENTA, RENOVACION }
    public enum EstadoContrato { ACTIVO, VENCIDO, CANCELADO, RENOVADO }

    private String id;
    private Inmueble inmueble;
    private Cliente cliente;
    private TipoContrato tipo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double valorMensual;
    private EstadoContrato estado;

    public Contrato(String id, Inmueble inmueble, Cliente cliente, TipoContrato tipo,
                    LocalDate fechaInicio, LocalDate fechaFin, double valorMensual) {
        this.id = id; this.inmueble = inmueble; this.cliente = cliente;
        this.tipo = tipo; this.fechaInicio = fechaInicio; this.fechaFin = fechaFin;
        this.valorMensual = valorMensual;
        this.estado = EstadoContrato.ACTIVO;
    }

    public String getId() { return id; }
    public Inmueble getInmueble() { return inmueble; }
    public Cliente getCliente() { return cliente; }
    public TipoContrato getTipo() { return tipo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getValorMensual() { return valorMensual; }
    public EstadoContrato getEstado() { return estado; }
    public void setEstado(EstadoContrato v) { this.estado = v; }

    @Override
    public String toString() {
        return "Contrato[" + id + " - " + tipo + " - " + (inmueble != null ? inmueble.getCodigo() : "?") + " - " + estado + "]";
    }
}
