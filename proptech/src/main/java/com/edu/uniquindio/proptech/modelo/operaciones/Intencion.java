package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Intención de compra o arriendo registrada por un cliente.
 * Permite construir el perfil de interés del cliente (req. 4.5 PDF).
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Intencion {

    public enum TipoIntencion { COMPRA, ARRIENDO }
    public enum EstadoIntencion { ACTIVA, CONCRETADA, DESCARTADA }

    private String id;
    private Cliente cliente;
    private Inmueble inmueble;
    private TipoIntencion tipo;
    private double oferta;
    private String observaciones;
    private EstadoIntencion estado;
    private LocalDateTime fecha;

    public Intencion(String id, Cliente cliente, Inmueble inmueble,
                     TipoIntencion tipo, double oferta, String observaciones) {
        this.id           = id;
        this.cliente      = cliente;
        this.inmueble     = inmueble;
        this.tipo         = tipo;
        this.oferta       = oferta;
        this.observaciones = observaciones == null ? "" : observaciones;
        this.estado       = EstadoIntencion.ACTIVA;
        this.fecha        = LocalDateTime.now();
    }

    public String getId()            { return id; }
    public Cliente getCliente()      { return cliente; }
    public Inmueble getInmueble()    { return inmueble; }
    public TipoIntencion getTipo()   { return tipo; }
    public double getOferta()        { return oferta; }
    public String getObservaciones() { return observaciones; }
    public EstadoIntencion getEstado() { return estado; }
    public void setEstado(EstadoIntencion e) { this.estado = e; }
    public LocalDateTime getFecha()  { return fecha; }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Intencion[" + id + " | " + tipo + " | " +
               (inmueble != null ? inmueble.getCodigo() : "?") + " | " +
               estado + " | " + fecha.format(f) + "]";
    }
}
