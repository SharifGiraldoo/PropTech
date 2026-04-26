package com.edu.uniquindio.proptech.modelo.usuario;

import com.edu.uniquindio.proptech.estructuras.lista.Lista;
import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import lombok.Data;

@Data
public class Asesor {
    String id;
    String nombre;
    String contacto;
    String zonaAsignada;

    Lista<Inmueble> inmueblesAsignados;
    Lista<Visita> visitasAgendadas;

    int cierresRealizados;

    public Asesor(String nombre, String id, String contacto, String zonaAsignada) {
        this.nombre = nombre;
        this.id = id;
        this.contacto = contacto;
        this.zonaAsignada = zonaAsignada;
        this.inmueblesAsignados = new ListaSimple<>();
        this.visitasAgendadas = new ListaSimple<>();
        this.cierresRealizados = 0;
    }
}
