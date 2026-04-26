package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;

public interface ICliente {

    void agregarFavoritos(Inmueble inmueble);
    void eliminarFavoritos(Inmueble inmueble);
    void registrarConsulta(Inmueble inmueble);
    void agendarVisita(Visita visita);
}
