package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.cola.Cola;
import com.edu.uniquindio.proptech.estructuras.cola.ColaLista;
import com.edu.uniquindio.proptech.estructuras.colaPrioridad.ColaPrioridad;
import com.edu.uniquindio.proptech.estructuras.colaPrioridad.ColaPrioridadLista;
import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.estructuras.pila.Pila;
import com.edu.uniquindio.proptech.estructuras.pila.PilaLista;
import com.edu.uniquindio.proptech.estructuras.tablaHash.TablaHash;
import com.edu.uniquindio.proptech.estructuras.tablaHash.TablaHashEncadenada;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.EstadoVisita;
import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;
import com.edu.uniquindio.proptech.modelo.operaciones.Visita;
import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.servicios.interfaces.ISistemaInmobiliario;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;
import com.edu.uniquindio.proptech.utils.excepciones.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

/**
 * Implementación principal del sistema inmobiliario PropTech.
 * <p>
 * Gestiona el registro y búsqueda de clientes, inmuebles y asesores,
 * el agendamiento y atención de visitas, la generación de alertas
 * prioritarias y el historial de acciones del sistema.
 * </p>
 *
 * <p>Estructuras de datos utilizadas:</p>
 * <ul>
 *   <li>{@link TablaHash} — almacenamiento y búsqueda O(1) de entidades</li>
 *   <li>{@link Cola} — visitas pendientes (FIFO)</li>
 *   <li>{@link Pila} — historial de acciones (LIFO)</li>
 *   <li>{@link ColaPrioridad} — alertas ordenadas por prioridad</li>
 *   <li>{@link ListaSimple} — registro de operaciones</li>
 * </ul>
 *
 * @author PropTech
 * @version 1.0
 */
@Getter
@Setter
public class SistemaInmobiliario implements ISistemaInmobiliario {

    /** Tabla hash de clientes registrados, indexada por nombre. */
    private TablaHashEncadenada<String, Cliente> clientes;

    /** Tabla hash de inmuebles registrados, indexada por código. */
    private TablaHashEncadenada<String, Inmueble> inmuebles;

    /** Tabla hash de asesores registrados, indexada por nombre. */
    private TablaHashEncadenada<String, Asesor> asesores;

    /** Cola FIFO de visitas pendientes por atender. */
    private ColaLista<Visita> visitasPendientes;

    /** Pila LIFO con el historial de acciones realizadas en el sistema. */
    private PilaLista<String> historialAcciones;

    /** Cola de prioridad para alertas del sistema, ordenadas por urgencia. */
    private ColaPrioridadLista<Alerta> alertas;

    /** Lista de todas las operaciones (ventas/arriendos) registradas. */
    private ListaSimple<Operacion> operaciones;

    /**
     * Constructor que inicializa todas las estructuras de datos del sistema.
     */
    public SistemaInmobiliario() {
        clientes          = new TablaHashEncadenada<>(50);
        inmuebles         = new TablaHashEncadenada<>(50);
        asesores          = new TablaHashEncadenada<>(20);
        visitasPendientes = new ColaLista<>();
        historialAcciones = new PilaLista<>();
        alertas           = new ColaPrioridadLista<>(Comparator.comparing(Alerta::getNivel));
        operaciones       = new ListaSimple<>();
    }

    // =========================================================
    //  CLIENTES
    // =========================================================

    /**
     * Registra un nuevo cliente en el sistema.
     *
     * @param cliente el cliente a registrar; no puede ser {@code null}
     * @throws ParametroVacioException      si {@code cliente} es {@code null}
     * @throws ClienteYaRegistradoException si ya existe un cliente con el mismo nombre
     */
    @Override
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new ParametroVacioException("El cliente no puede ser nulo.");
        }
        if (clientes.get(cliente.getNombre()) != null) {
            throw new ClienteYaRegistradoException(
                    "El cliente '" + cliente.getNombre() + "' ya está registrado.");
        }
        clientes.put(cliente.getNombre(), cliente);
        historialAcciones.push("Registro de cliente: " + cliente.getNombre());
    }

    /**
     * Busca y retorna un cliente por su identificador.
     *
     * @param id identificador (nombre) del cliente; no puede ser {@code null} ni vacío
     * @return el {@link Cliente} encontrado
     * @throws ParametroVacioException     si {@code id} es {@code null} o vacío
     * @throws ElementoNoEncontradoException si no existe un cliente con ese id
     */
    @Override
    public Cliente buscarCliente(String id) {
        if (id == null || id.isEmpty()) {
            throw new ParametroVacioException("El id del cliente no puede ser nulo o vacío.");
        }
        Cliente cliente = clientes.get(id);
        if (cliente == null) {
            throw new ElementoNoEncontradoException(
                    "No se encontró ningún cliente con el id: " + id);
        }
        return cliente;
    }

    // =========================================================
    //  INMUEBLES
    // =========================================================

    /**
     * Registra un nuevo inmueble en el sistema.
     *
     * @param inmueble el inmueble a registrar; no puede ser {@code null}
     * @throws ParametroVacioException si {@code inmueble} es {@code null}
     * @throws ClienteYaRegistradoException si ya existe un inmueble con el mismo código
     */
    @Override
    public void registrarInmueble(Inmueble inmueble) {
        if (inmueble == null) {
            throw new ParametroVacioException("El inmueble no puede ser nulo.");
        }
        if (inmuebles.get(inmueble.getCodigo()) != null) {
            throw new ClienteYaRegistradoException(
                    "El inmueble con código '" + inmueble.getCodigo() + "' ya está registrado.");
        }
        inmuebles.put(inmueble.getCodigo(), inmueble);
        historialAcciones.push("Registro de inmueble: " + inmueble.getCodigo());
    }

    /**
     * Busca y retorna un inmueble por su código.
     *
     * @param codigo código único del inmueble; no puede ser {@code null} ni vacío
     * @return el {@link Inmueble} encontrado
     * @throws ParametroVacioException       si {@code codigo} es {@code null} o vacío
     * @throws ElementoNoEncontradoException si no existe un inmueble con ese código
     */
    @Override
    public Inmueble buscarInmueble(String codigo) {
        if (codigo == null || codigo.isEmpty()) {
            throw new ParametroVacioException("El código del inmueble no puede ser nulo o vacío.");
        }
        Inmueble inmueble = inmuebles.get(codigo);
        if (inmueble == null) {
            throw new ElementoNoEncontradoException(
                    "No se encontró ningún inmueble con el código: " + codigo);
        }
        return inmueble;
    }

    // =========================================================
    //  ASESORES
    // =========================================================

    /**
     * Registra un nuevo asesor en el sistema.
     *
     * @param asesor el asesor a registrar; no puede ser {@code null}
     * @throws ParametroVacioException      si {@code asesor} es {@code null}
     * @throws ClienteYaRegistradoException si ya existe un asesor con el mismo nombre
     */
    @Override
    public void registrarAsesor(Asesor asesor) {
        if (asesor == null) {
            throw new ParametroVacioException("El asesor no puede ser nulo.");
        }
        if (asesores.get(asesor.getNombre()) != null) {
            throw new ClienteYaRegistradoException(
                    "El asesor '" + asesor.getNombre() + "' ya está registrado.");
        }
        asesores.put(asesor.getNombre(), asesor);
        historialAcciones.push("Registro de asesor: " + asesor.getNombre());
    }

    // =========================================================
    //  VISITAS
    // =========================================================

    /**
     * Agenda una visita encólándola en la cola de visitas pendientes.
     *
     * @param visita la visita a agendar; no puede ser {@code null}
     * @throws ParametroVacioException si {@code visita} es {@code null}
     * @throws VisitaInvalidaException si la visita no tiene cliente o inmueble asignado
     */
    @Override
    public void agendarVisita(Visita visita) {
        if (visita == null) {
            throw new ParametroVacioException("La visita no puede ser nula.");
        }
        if (visita.getCliente() == null || visita.getInmueble() == null) {
            throw new VisitaInvalidaException(
                    "La visita debe tener un cliente y un inmueble asignados.");
        }
        visitasPendientes.encolar(visita);
        historialAcciones.push("Visita agendada para: " + visita.getCliente().getNombre());
    }

    /**
     * Atiende la siguiente visita pendiente en la cola.
     * <p>
     * Extrae la primera visita de la cola y la marca como {@link EstadoVisita#REALIZADA}.
     * </p>
     *
     * @param visita la visita a marcar como realizada; no puede ser {@code null}
     * @throws ParametroVacioException si {@code visita} es {@code null}
     * @throws ListaVaciaException     si no hay visitas pendientes en la cola
     */
    @Override
    public void atenderVisita(Visita visita) {
        if (visita == null) {
            throw new ParametroVacioException("La visita no puede ser nula.");
        }
        if (visitasPendientes.estaVacia()) {
            throw new ListaVaciaException("No hay visitas pendientes por atender.");
        }
        Visita siguiente = visitasPendientes.desencolar();
        siguiente.setEstado(EstadoVisita.REALIZADA);
        historialAcciones.push("Visita atendida: " + siguiente.getCliente().getNombre());
    }

    // =========================================================
    //  ALERTAS
    // =========================================================

    /**
     * Genera y encola una nueva alerta en la cola de prioridad.
     *
     * @param alerta la alerta a registrar; no puede ser {@code null}
     * @throws ParametroVacioException si {@code alerta} es {@code null}
     */
    @Override
    public void generarAlerta(Alerta alerta) {
        if (alerta == null) {
            throw new ParametroVacioException("La alerta no puede ser nula.");
        }
        alertas.encolar(alerta);
        historialAcciones.push("Alerta generada.");
    }

    /**
     * Retorna y elimina la alerta de mayor prioridad en el sistema.
     *
     * @return la {@link Alerta} más prioritaria
     * @throws ListaVaciaException si no hay alertas registradas
     */
    @Override
    public Alerta obtenerAlertaPrioritaria() {
        if (alertas.estaVacia()) {
            throw new ListaVaciaException("No hay alertas registradas en el sistema.");
        }
        return alertas.desencolar();
    }

    // =========================================================
    //  HISTORIAL
    // =========================================================

    /**
     * Retorna y elimina la última acción registrada en el historial.
     *
     * @return {@code String} con la descripción de la última acción
     * @throws ListaVaciaException si el historial está vacío
     */
    public String deshacerUltimaAccion() {
        if (historialAcciones.estaVacia()) {
            throw new ListaVaciaException("El historial de acciones está vacío.");
        }
        return historialAcciones.pop();
    }
}