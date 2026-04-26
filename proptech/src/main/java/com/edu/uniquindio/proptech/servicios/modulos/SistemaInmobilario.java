package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SistemaInmobilario {
    Map<String, Cliente> clientes;
    Map<String, Inmueble> inmuebles;
    Map<String, Asesor> asesores;

    Queue<Visita> visitasPendientes;

    Stack<String> historialAcciones;

    PriorityQueue<Alerta> alertas;

    ListaSimple<Operacion> operaciones;

    public SistemaInmobilario() {
        operaciones = new ListaSimple<>();
    }
}
