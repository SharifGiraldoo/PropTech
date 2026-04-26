package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.servicios.interfaces.ISistemaInmobiliario;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SistemaInmobilario implements ISistemaInmobiliario {
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

    @Override
    public void registrarCliente(Cliente cliente) {

    }

    @Override
    public void registrarInmueble(Inmueble inmueble) {

    }

    @Override
    public void registrarAsesor(Asesor asesor) {

    }

    @Override
    public Cliente buscarCliente(String id) {
        return null;
    }

    @Override
    public Inmueble buscarInmueble(String codigo) {
        return null;
    }

    @Override
    public void agendarVisita(Visita visita) {

    }

    @Override
    public void atenderVisita() {

    }

    @Override
    public void generarAlerta(Alerta alerta) {

    }

    @Override
    public Alerta obtenerAlertaPrioritaria() {
        return null;
    }
}
