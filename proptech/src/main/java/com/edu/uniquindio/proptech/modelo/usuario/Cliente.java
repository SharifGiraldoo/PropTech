package com.edu.uniquindio.proptech.modelo.usuario;


import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import lombok.Data;

@Data
public class Cliente {
    String id;
    String nombre;
    String correo;
    String telefono;
    String tipoCliente;
    double presupuesto;

    List<String> zonasInteres;
    String tipoInmuebleDeseado;
    int minHabitaciones;

    String estadoBusqueda;

    List<Inmueble> favoritos;
    List<Inmueble> historialConsultas;
    List<Visita> historialVisitas;

    public Cliente(String id, String nombre, String correo, String telefono, String tipoCliente, double presupuesto) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.presupuesto = presupuesto;
    }
}
