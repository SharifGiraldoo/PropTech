package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import lombok.Data;

@Data
public class Visita {
    Cliente cliente;
    Inmueble inmueble;
    Asesor asesor;
    String fecha;
    String hora;
    String estado;
    String observaciones;

    public Visita(Cliente cliente, Inmueble inmueble, Asesor asesor, String fecha, String hora, String estado, String observaciones) {
        this.cliente = cliente;
        this.inmueble = inmueble;
        this.asesor = asesor;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.observaciones = observaciones;
    }
}
