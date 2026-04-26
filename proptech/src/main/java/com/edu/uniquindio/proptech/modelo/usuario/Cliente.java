package com.edu.uniquindio.proptech.modelo.usuario;


import com.edu.uniquindio.proptech.estructuras.lista.Lista;
import com.edu.uniquindio.proptech.estructuras.lista.ListaDoble;
import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
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

    Lista<String> zonasInteres;
    String tipoInmuebleDeseado;
    int minHabitaciones;

    String estadoBusqueda;

    Lista<Inmueble> favoritos;
    Lista<Inmueble> historialConsultas;
    Lista<Visita> historialVisitas;

    public Cliente(String id, String nombre, String correo, String telefono, String tipoCliente, double presupuesto) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.presupuesto = presupuesto;
        this.zonasInteres = new ListaDoble<String>();
        this.favoritos = new ListaDoble<>();
        this.historialConsultas = new ListaSimple<>();
        this.historialVisitas = new ListaSimple<>();

    }
}
