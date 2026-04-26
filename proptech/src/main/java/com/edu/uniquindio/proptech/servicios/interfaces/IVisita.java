package com.edu.uniquindio.proptech.servicios.interfaces;

import java.time.LocalDateTime;

public interface IVisita {

    void confirmar();
    void cancelar();
    void reprogramar(LocalDateTime nuevaFechayHora);
}
