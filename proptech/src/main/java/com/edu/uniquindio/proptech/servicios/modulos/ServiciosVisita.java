package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.operaciones.EstadoVisita;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.servicios.interfaces.IVisita;

import java.time.LocalDateTime;

public class ServiciosVisita implements IVisita {
    @Override
    public void confirmar(Visita visita) {
        visita.setEstado(EstadoVisita.CONFIRMADA);
    }

    @Override
    public void cancelar(Visita visita) {
        visita.setEstado(EstadoVisita.CANCELADA);

    }

    @Override
    public void reprogramar(Visita visita, LocalDateTime nuevaFechayHora) {
        visita.setFechayHora(nuevaFechayHora);
        visita.setEstado(EstadoVisita.REPORGRAMADA);

    }
}
