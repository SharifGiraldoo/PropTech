package com.edu.uniquindio.proptech.utils.alerta;

import lombok.Data;

@Data
public class Alerta {

    String tipo;
    String mensaje;
    NivelAlerta nivel;
    String fecha;

    public Alerta(String tipo, String mensaje, NivelAlerta nivel, String fecha) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.nivel = nivel;
        this.fecha = fecha;
    }
}
