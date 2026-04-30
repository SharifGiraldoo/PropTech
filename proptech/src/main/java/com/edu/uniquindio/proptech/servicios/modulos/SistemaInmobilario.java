package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.cola.Cola;
import com.edu.uniquindio.proptech.estructuras.colaPrioridad.ColaPrioridad;
import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.estructuras.pila.Pila;
import com.edu.uniquindio.proptech.estructuras.tablaHash.TablaHash;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.servicios.interfaces.ISistemaInmobiliario;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;
import com.edu.uniquindio.proptech.utils.excepciones.ParametroVacioException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SistemaInmobilario implements ISistemaInmobiliario {
    TablaHash<String, Cliente> clientes;
    TablaHash<String, Inmueble> inmuebles;
    TablaHash<String, Asesor> asesores;

    Cola<Visita> visitasPendientes;

    Pila<String> historialAcciones;

    ColaPrioridad<Alerta> alertas;

    ListaSimple<Operacion> operaciones;

    public SistemaInmobilario() {
        operaciones = new ListaSimple<>();
    }

    @Override
    public void registrarCliente(Cliente cliente) {
        if(clientes == null){
            new ParametroVacioException("El cliente no puede ser nulo.");

        }else{
            clientes.put(cliente.getNombre(), cliente);
        }

    }

    @Override
    public void registrarInmueble(Inmueble inmueble) {
        if(inmueble == null){
            new ParametroVacioException("El inmueble no puede ser nulo.");

        }else{
            inmuebles.put(inmueble.getCodigo(), inmueble);
        }
    }

    @Override
    public void registrarAsesor(Asesor asesor) {
        if(asesores == null){
            new ParametroVacioException("El asesor no puede ser nulo.");

        }else{
            asesores.put(asesor.getNombre(), asesor);
        }
    }

    @Override
    public Cliente buscarCliente(String id) {
        return clientes.get(id);
    }

    @Override
    public Inmueble buscarInmueble(String codigo) {
        return inmuebles.get(codigo);
    }

    @Override
    public void agendarVisita(Visita visita) {
        visitasPendientes.encolar(visita);
    }

    @Override
    public void atenderVisita() {

    }

    @Override
    public void generarAlerta(Alerta alerta) {
        alertas.encolar(alerta);
    }

    @Override
    public Alerta obtenerAlertaPrioritaria() {
        return alertas.desencolar();
    }
}
