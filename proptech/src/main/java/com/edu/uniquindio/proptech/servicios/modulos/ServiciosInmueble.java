package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.inmueble.EstadoInmueble;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.servicios.interfaces.IInmueble;

public class ServiciosInmueble implements IInmueble {
    @Override
    public void cambiarEstado(Inmueble inmueble, EstadoInmueble estado) {
        inmueble.setEstado(estado);
    }

    @Override
    public void actualizarPrecio(Inmueble inmueble, double precio) {
        inmueble.setPrecio(precio);
    }
}
