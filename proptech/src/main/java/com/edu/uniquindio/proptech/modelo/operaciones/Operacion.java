package com.edu.uniquindio.proptech.modelo.operaciones;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

public class Operacion {
    String id;
    Inmueble inmueble;
    Cliente cliente;
    Asesor asesor;
    String tipo;
    LocalDate fecha;
    double valor;
    double comision;
    String estado;

    public Operacion(String id, Inmueble inmueble, Cliente cliente, Asesor asesor, String tipo, LocalDate fecha, double valor, double comision, String estado) {
        this.id = id;
        this.inmueble = inmueble;
        this.cliente = cliente;
        this.asesor = asesor;
        this.tipo = tipo;
        this.fecha = fecha;
        this.valor = valor;
        this.comision = comision;
        this.estado = estado;
    }
}
