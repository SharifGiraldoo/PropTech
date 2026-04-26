package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;

public interface IAsesor {

    void asignarInmueble(Asesor asesor, Inmueble inmueble);
    void registrarVisita(Asesor asesor, Visita visita);
}
