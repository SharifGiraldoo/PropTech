package com.edu.uniquindio.proptech.modelo.inmueble;

import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import lombok.Data;

@Data
public class Inmueble {
    String codigo;
    String direccion;
    String ciudad;
    String zona;
    String tipo;
    String finalidad;
    double precio;
    double area;
    int habitaciones;
    int banios;
    String estado;
    boolean disponible;
    Asesor asesorResponsable;

    public Inmueble(String codigo, String direccion, String ciudad, String zona, String tipo, String finalidad, double precio, double area, int habitaciones, int banios, String estado, boolean disponible, Asesor asesorResponsable) {
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
        this.disponible = true;
        this.asesorResponsable = asesorResponsable;
    }
}
