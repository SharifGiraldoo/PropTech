package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Data
public class Visita {
    Cliente cliente;
    Inmueble inmueble;
    Asesor asesor;
    LocalDateTime fechayHora;
    String estado;
    String observaciones;

    public Visita(Cliente cliente, Inmueble inmueble, Asesor asesor, LocalDateTime fechayHora, String estado, String observaciones) {
        this.cliente = cliente;
        this.inmueble = inmueble;
        this.asesor = asesor;
        this.fechayHora = fechayHora;
        this.estado = estado;
        this.observaciones = observaciones;
    }
}
