package com.edu.uniquindio.proptech.modelo.inmueble;

import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.inmueble.TipoInmueble;
import lombok.Data;

@Data
public class Inmueble {
    String codigo;
    String direccion;
    String ciudad;
    String zona;
    TipoInmueble tipoInmueble;
    String finalidad;
    double precio;
    double area;
    int habitaciones;
    int banios;
    EstadoInmueble estado;
    Asesor asesorResponsable;

    public Inmueble(String codigo, String direccion, String ciudad, String zona, TipoInmueble tipoInmueble, String finalidad, double precio, double area, int habitaciones, int banios, EstadoInmueble estado, Asesor asesorResponsable) {
        this.codigo = codigo;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.zona = zona;
        this.tipoInmueble = tipoInmueble;
        this.finalidad = finalidad;
        this.precio = precio;
        this.area = area;
        this.habitaciones = habitaciones;
        this.banios = banios;
        this.estado = estado;
        this.asesorResponsable = asesorResponsable;
    }
}
