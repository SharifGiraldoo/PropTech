package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.servicios.interfaces.IAsesor;
import com.edu.uniquindio.proptech.utils.excepciones.ParametroVacioException;

public class ServiciosAsesor implements IAsesor {
    @Override
    public void asignarInmueble(Asesor asesor, Inmueble inmueble) {
        if (inmueble == null){
            new ParametroVacioException("El inmueble no puede ser nulo");
        } else{
            if(inmueble instanceof Inmueble){
                asesor.getInmueblesAsignados().agregarFinal(inmueble);
            }
        }
    }

    @Override
    public void registrarVisita(Asesor asesor, Visita visita) {
        if (visita == null){
            new ParametroVacioException("La visita no puede ser nula");
        } else{
            if(visita instanceof Visita){
                asesor.getVisitasAgendadas().agregarFinal(visita);
            }
        }

    }
}
