package com.edu.uniquindio.proptech.modelo.usuario;

import com.edu.uniquindio.proptech.estructuras.lista.Lista;
import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import lombok.Data;

@Data
public class Asesor extends Usuario {
    String id;
    String nombre;
    String correo;
    String telefono;
    String zonaAsignada;
    String contrasenia;

    Lista<Inmueble> inmueblesAsignados;
    Lista<Visita> visitasAgendadas;

    int cierresRealizados;

    public Asesor(String nombre, String id, String correo, String telefono, String zonaAsignada, String contrasenia) {
        super(nombre, id, correo, telefono, contrasenia);
        this.zonaAsignada = zonaAsignada;
        this.inmueblesAsignados = new ListaSimple<>();
        this.visitasAgendadas = new ListaSimple<>();
        this.cierresRealizados = 0;
    }
}
