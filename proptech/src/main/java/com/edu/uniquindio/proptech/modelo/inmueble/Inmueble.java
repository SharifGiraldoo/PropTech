package com.edu.uniquindio.proptech.modelo.inmueble;

import com.edu.uniquindio.proptech.modelo.usuario.Asesor;

/**
 * Representa un inmueble en la plataforma.
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class Inmueble {
    private String codigo;
    private String direccion;
    private String ciudad;
    private String zona;
    private TipoInmueble tipo;
    private String finalidad; // VENTA | ARRIENDO
    private double precio;
    private double area;
    private int habitaciones;
    private int banios;
    private EstadoInmueble estado;
    private Asesor asesor;
    private int visitas;
    private String fotoUrl; // ruta relativa a fotos/inmuebles/ o vacío

    public Inmueble(String codigo, String direccion, String ciudad, String zona,
                    TipoInmueble tipo, String finalidad, double precio, double area,
                    int habitaciones, int banios, EstadoInmueble estado, Asesor asesor) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.zona = zona;
        this.tipo = tipo;
        this.finalidad = finalidad;
        this.precio = precio;
        this.area = area;
        this.habitaciones = habitaciones;
        this.banios = banios;
        this.estado = estado;
        this.asesor = asesor;
        this.visitas = 0;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String v) { this.codigo = v; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String v) { this.direccion = v; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String v) { this.ciudad = v; }
    public String getZona() { return zona; }
    public void setZona(String v) { this.zona = v; }
    public TipoInmueble getTipo() { return tipo; }
    public void setTipo(TipoInmueble v) { this.tipo = v; }
    public String getFinalidad() { return finalidad; }
    public void setFinalidad(String v) { this.finalidad = v; }
    public double getPrecio() { return precio; }
    public void setPrecio(double v) { this.precio = v; }
    public double getArea() { return area; }
    public void setArea(double v) { this.area = v; }
    public int getHabitaciones() { return habitaciones; }
    public void setHabitaciones(int v) { this.habitaciones = v; }
    public int getBanios() { return banios; }
    public void setBanios(int v) { this.banios = v; }
    public EstadoInmueble getEstado() { return estado; }
    public void setEstado(EstadoInmueble v) { this.estado = v; }
    public Asesor getAsesor() { return asesor; }
    public void setAsesor(Asesor v) { this.asesor = v; }
    public int getVisitas() { return visitas; }
    public void setVisitas(int v) { this.visitas = v; }
    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String v) { this.fotoUrl = v; }

    // ── Métodos del diagrama de clases ──────────────────────────────────────
    /** Cambia el estado del inmueble */
    public void cambiarEstado(String nuevoEstado) {
        try {
            this.estado = EstadoInmueble.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            // estado no reconocido — no cambia
        }
    }

    /** Actualiza el precio del inmueble */
    public void actualizarPrecio(double nuevoPrecio) {
        if (nuevoPrecio > 0) this.precio = nuevoPrecio;
    }

    @Override
    public String toString() {
        return "Inmueble[" + codigo + " - " + tipo + " - " + zona + " - $" + precio + " - " + estado + "]";
    }
}
