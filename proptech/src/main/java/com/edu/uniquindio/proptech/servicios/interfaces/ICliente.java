package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;

public interface ICliente {

    void agregarFavoritos(Cliente cliente, Inmueble inmueble);
    void eliminarFavoritos(Cliente cliente, Inmueble inmueble);
    void registrarConsulta(Cliente cliente, Inmueble inmueble);
    void agendarVisita(Cliente cliente, Visita visita);
}
