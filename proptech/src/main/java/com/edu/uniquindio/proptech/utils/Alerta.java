package com.edu.uniquindio.proptech.utils;

import lombok.Data;

@Data
public class Alerta {

    String tipo;
    String mensaje;
    String nivel;
    String fecha;

    public Alerta(String tipo, String mensaje, String nivel, String fecha) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.nivel = nivel;
        this.fecha = fecha;
    }
}
