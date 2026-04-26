package com.edu.uniquindio.proptech.servicios;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.modelo.utils.Alerta;

public class SistemaInmobilario {
    Map<String, Cliente> clientes;
    Map<String, Inmueble> inmuebles;
    Map<String, Asesor> asesores;

    Queue<Visita> visitasPendientes;

    Stack<String> historialAcciones;

    PriorityQueue<Alerta> alertas;
}
