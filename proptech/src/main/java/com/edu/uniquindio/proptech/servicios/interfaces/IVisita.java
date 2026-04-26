package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.operaciones.Visita;

import java.time.LocalDateTime;

public interface IVisita {

    void confirmar(Visita visita);
    void cancelar(Visita visita);
    void reprogramar(Visita visita, LocalDateTime nuevaFechayHora);
}
