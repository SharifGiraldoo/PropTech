package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.servicios.interfaces.ICliente;
import com.edu.uniquindio.proptech.utils.Excepciones.ParametroVacioException;
import com.edu.uniquindio.proptech.utils.Excepciones.PosicionInvalidaException;

public class ServiciosCliente implements ICliente {
    @Override
    public void agregarFavoritos(Cliente cliente, Inmueble inmueble) {
        if (inmueble == null){
            new ParametroVacioException("El inmueble no puede ser nulo");
        } else{
            if(inmueble instanceof Inmueble){
                cliente.getFavoritos().agregarFinal(inmueble);
            }
        }
    }

    @Override
    public void eliminarFavoritos(Cliente cliente, Inmueble inmueble) {
        if (inmueble == null){
            new ParametroVacioException("El inmueble no puede ser nulo");
        } else{
            if(inmueble instanceof Inmueble){
                cliente.getFavoritos().eliminar(inmueble);
            }
        }
    }

    @Override
    public void registrarConsulta(Cliente cliente, Inmueble inmueble) {
        if (inmueble == null){
            new ParametroVacioException("El inmueble no puede ser nulo");
        } else{
            if(inmueble instanceof Inmueble){
                cliente.getHistorialConsultas().agregarFinal(inmueble);
            }
        }
    }

    @Override
    public void agendarVisita(Cliente cliente, Visita visita) {
        if (visita == null){
            new ParametroVacioException("La visita no puede ser nulo");
        } else{
            if(visita instanceof Visita){
                cliente.getHistorialVisitas().agregarFinal(visita);
            }
        }

    }
}
