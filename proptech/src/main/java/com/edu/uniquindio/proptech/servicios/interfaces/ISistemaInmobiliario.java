package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;

public interface ISistemaInmobiliario {
    void registrarCliente(Cliente cliente);
    void registrarInmueble(Inmueble inmueble);
    void registrarAsesor(Asesor asesor);
    Cliente buscarCliente(String id);
    Inmueble buscarInmueble(String codigo);
    void agendarVisita(Visita visita);
    void atenderVisita();
    void generarAlerta(Alerta alerta);
    Alerta obtenerAlertaPrioritaria();

}
