package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.EstadoInmueble;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;

public interface IInmueble {

    void cambiarEstado(Inmueble inmueble, EstadoInmueble estado);
    void actualizarPrecio(Inmueble inmueble, double precio);
}
