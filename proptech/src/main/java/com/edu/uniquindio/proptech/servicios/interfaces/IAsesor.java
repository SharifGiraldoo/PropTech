package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;

public interface IAsesor {

    void asignarInmueble(Inmueble inmueble);
    void registrarVisita(Visita visita);
}
