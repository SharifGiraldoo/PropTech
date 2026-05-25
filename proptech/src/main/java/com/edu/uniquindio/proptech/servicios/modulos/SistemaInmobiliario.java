package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.*;
import com.edu.uniquindio.proptech.estructuras.cola.*;
import com.edu.uniquindio.proptech.estructuras.colaPrioridad.*;
import com.edu.uniquindio.proptech.estructuras.lista.*;
import com.edu.uniquindio.proptech.estructuras.pila.*;
import com.edu.uniquindio.proptech.estructuras.tablaHash.*;
import com.edu.uniquindio.proptech.modelo.inmueble.*;
import com.edu.uniquindio.proptech.modelo.operaciones.*;
import com.edu.uniquindio.proptech.modelo.usuario.*;
import com.edu.uniquindio.proptech.utils.alerta.*;
import com.edu.uniquindio.proptech.utils.alerta.Accion.TipoAccion;
import com.edu.uniquindio.proptech.utils.excepciones.*;

import com.edu.uniquindio.proptech.modelo.operaciones.Intencion;
import java.time.LocalDate;
import java.util.*;

/**
 * Sistema central de gestión inmobiliaria PropTech.
 *
 * Estructuras de datos utilizadas:
 *  TablaHashEncadenada  → clientes, inmuebles, asesores    O(1) búsqueda
 *  ColaLista<Visita>    → visitas pendientes               FIFO
 *  PilaLista<Accion>    → historial de acciones            LIFO
 *  ColaPrioridadLista   → alertas por urgencia             Mayor prioridad primero
 *  ListaSimple          → operaciones, contratos, historial, registro general
 *  Arbol<Double>        → BST de precios                   Búsqueda por rango
 *  Grafo                → relaciones entre zonas            Análisis estructural
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class SistemaInmobiliario {

    // ── Estructuras de datos ──────────────────────────────────────────────────
    private final TablaHashEncadenada<String, Cliente>  clientes;
    private final TablaHashEncadenada<String, Inmueble> inmuebles;
    private final TablaHashEncadenada<String, Asesor>   asesores;

    private final ColaLista<Visita>          visitasPendientes;
    private final PilaLista<Accion<?>>       historialAcciones;
    private final ColaPrioridadLista<Alerta> alertas;

    private final ListaSimple<Operacion> operaciones;
    private final ListaSimple<Contrato>  contratos;
    private final ListaSimple<Alerta>    alertasRegistro;
    private final ListaSimple<Visita>    todasLasVisitas;
    private final ListaSimple<Intencion> intenciones;

    // Listas auxiliares para iteración en la UI
    private final ListaSimple<Inmueble> inmueblesLista;
    private final ListaSimple<Cliente>  clientesLista;
    private final ListaSimple<Asesor>   asesoresLista;

    private final Arbol<Double> bstPrecios;
    private final Grafo         grafoZonas;

    private int contadorId = 1;

    public SistemaInmobiliario() {
        this.clientes   = new TablaHashEncadenada<>();
        this.inmuebles  = new TablaHashEncadenada<>();
        this.asesores   = new TablaHashEncadenada<>();
        this.visitasPendientes = new ColaLista<>();
        this.historialAcciones = new PilaLista<>();
        this.alertas    = new ColaPrioridadLista<>(Comparator.comparingInt(Alerta::getPrioridadNumerica).reversed());
        this.operaciones = new ListaSimple<>();
        this.contratos   = new ListaSimple<>();
        this.alertasRegistro = new ListaSimple<>();
        this.todasLasVisitas = new ListaSimple<>();
        this.intenciones     = new ListaSimple<>();
        this.inmueblesLista  = new ListaSimple<>();
        this.clientesLista   = new ListaSimple<>();
        this.asesoresLista   = new ListaSimple<>();
        this.bstPrecios = new Arbol<>();
        this.grafoZonas = new Grafo();
    }

    // ── Getters de estructuras ────────────────────────────────────────────────
    public TablaHashEncadenada<String, Cliente>  getClientes()          { return clientes; }
    public TablaHashEncadenada<String, Inmueble> getInmuebles()         { return inmuebles; }
    public TablaHashEncadenada<String, Asesor>   getAsesores()          { return asesores; }
    public ColaLista<Visita>                     getVisitasPendientes() { return visitasPendientes; }
    public PilaLista<Accion<?>>                  getHistorialAcciones() { return historialAcciones; }
    public ColaPrioridadLista<Alerta>            getAlertas()           { return alertas; }
    public ListaSimple<Operacion>                getOperaciones()       { return operaciones; }
    public ListaSimple<Contrato>                 getContratos()         { return contratos; }
    public ListaSimple<Alerta>                   getAlertasRegistro()   { return alertasRegistro; }
    public ListaSimple<Visita>                   getTodasLasVisitas()   { return todasLasVisitas; }
    public ListaSimple<Intencion>                getIntenciones()       { return intenciones; }
    public ListaSimple<Inmueble>                 getInmueblesLista()    { return inmueblesLista; }
    public ListaSimple<Cliente>                  getClientesLista()     { return clientesLista; }
    public ListaSimple<Asesor>                   getAsesoresLista()     { return asesoresLista; }
    public Arbol<Double>                         getBstPrecios()        { return bstPrecios; }
    public Grafo                                 getGrafoZonas()        { return grafoZonas; }

    public String generarId(String prefijo) {
        return prefijo + String.format("%04d", contadorId++);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CLIENTES
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarCliente(Cliente cliente) {
        if (cliente == null) throw new ParametroVacioException("El cliente no puede ser nulo");
        clientes.put(cliente.getId(), cliente);
        clientesLista.agregarFinal(cliente);
        historialAcciones.push(new Accion<>(TipoAccion.CREACION_CLIENTE,
                null, cliente, "Cliente registrado: " + cliente.getNombre()));
    }

    public Cliente buscarCliente(String id) {
        if (id == null || id.isBlank()) return null;
        return clientes.getSafe(id);
    }

    public void eliminarCliente(String id) {
        Cliente c = clientes.getSafe(id);
        if (c == null) throw new ElementoNoEncontradoException("Cliente no encontrado: " + id);
        clientes.eliminar(id);
        clientesLista.eliminar(c);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // INMUEBLES
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarInmueble(Inmueble inmueble) {
        if (inmueble == null) throw new ParametroVacioException("El inmueble no puede ser nulo");
        inmuebles.put(inmueble.getCodigo(), inmueble);
        inmueblesLista.agregarFinal(inmueble);
        bstPrecios.insertar(inmueble.getPrecio());
        grafoZonas.agregarNodo(inmueble.getZona());
        historialAcciones.push(new Accion<>(TipoAccion.CREACION_INMUEBLE,
                null, inmueble, "Inmueble registrado: " + inmueble.getCodigo()));
    }

    public Inmueble buscarInmueble(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;
        return inmuebles.getSafe(codigo);
    }

    public void eliminarInmueble(String codigo) {
        Inmueble inm = inmuebles.getSafe(codigo);
        if (inm == null) throw new ElementoNoEncontradoException("Inmueble no encontrado: " + codigo);
        inmuebles.eliminar(codigo);
        inmueblesLista.eliminar(inm);
        bstPrecios.eliminar(inm.getPrecio());
        historialAcciones.push(new Accion<>(TipoAccion.ELIMINAR_INMUEBLE,
                inm, null, "Inmueble eliminado: " + codigo));
    }

    public void cambiarEstadoInmueble(String codigo, EstadoInmueble nuevoEstado) {
        Inmueble inm = buscarInmueble(codigo);
        if (inm == null) throw new ElementoNoEncontradoException("Inmueble no encontrado: " + codigo);
        EstadoInmueble anterior = inm.getEstado();
        inm.setEstado(nuevoEstado);
        historialAcciones.push(new Accion<>(TipoAccion.CAMBIO_ESTADO,
                anterior, nuevoEstado, codigo + ": " + anterior + " → " + nuevoEstado));
    }

    public void actualizarPrecioInmueble(String codigo, double nuevoPrecio) {
        Inmueble inm = buscarInmueble(codigo);
        if (inm == null) throw new ElementoNoEncontradoException("Inmueble no encontrado: " + codigo);
        double anterior = inm.getPrecio();
        bstPrecios.eliminar(anterior);
        inm.setPrecio(nuevoPrecio);
        bstPrecios.insertar(nuevoPrecio);
        historialAcciones.push(new Accion<>(TipoAccion.EDICION_INMUEBLE,
                anterior, nuevoPrecio, "Precio actualizado: " + codigo));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ASESORES
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarAsesor(Asesor asesor) {
        if (asesor == null) throw new ParametroVacioException("El asesor no puede ser nulo");
        asesores.put(asesor.getId(), asesor);
        asesoresLista.agregarFinal(asesor);
        grafoZonas.agregarNodo(asesor.getZonaAsignada());
        historialAcciones.push(new Accion<>(TipoAccion.CREACION_ASESOR,
                null, asesor, "Asesor registrado: " + asesor.getNombre()));
    }

    public Asesor buscarAsesor(String id) {
        if (id == null || id.isBlank()) return null;
        return asesores.getSafe(id);
    }

    public void asignarInmuebleAsesor(String asesorId, String inmuebleCodigo) {
        Asesor   asesor  = buscarAsesor(asesorId);
        Inmueble inmueble = buscarInmueble(inmuebleCodigo);
        if (asesor   == null) throw new ElementoNoEncontradoException("Asesor no encontrado: " + asesorId);
        if (inmueble == null) throw new ElementoNoEncontradoException("Inmueble no encontrado: " + inmuebleCodigo);
        if (!asesor.getInmueblesAsignados().contiene(inmueble)) {
            asesor.getInmueblesAsignados().agregarFinal(inmueble);
        }
        inmueble.setAsesor(asesor);
        if (inmueble.getZona() != null && !inmueble.getZona().equals(asesor.getZonaAsignada())) {
            grafoZonas.agregarArista(asesor.getZonaAsignada(), inmueble.getZona());
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // VISITAS — ColaLista<Visita> FIFO
    // ═════════════════════════════════════════════════════════════════════════

    public void agendarVisita(Visita visita) {
        if (visita == null) throw new ParametroVacioException("La visita no puede ser nula");
        visitasPendientes.encolar(visita);
        todasLasVisitas.agregarFinal(visita);
        if (visita.getAsesor() != null)
            visita.getAsesor().getVisitasAtendidas().agregarFinal(visita);
        if (visita.getCliente() != null)
            visita.getCliente().registrarVisita(visita);
        if (visita.getInmueble() != null)
            visita.getInmueble().setVisitas(visita.getInmueble().getVisitas() + 1);
        historialAcciones.push(new Accion<>(TipoAccion.AGENDAR_VISITA, null, visita,
                "Visita agendada: " +
                (visita.getCliente() != null ? visita.getCliente().getNombre() : "?") +
                " → " + (visita.getInmueble() != null ? visita.getInmueble().getCodigo() : "?")));
    }

    public Visita atenderVisita() {
        if (visitasPendientes.estaVacia()) return null;
        Visita v = visitasPendientes.desencolar();
        v.setEstado(EstadoVisita.REALIZADA);
        return v;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // OPERACIONES
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarOperacion(Operacion op) {
        if (op == null) throw new ParametroVacioException("Operación nula");
        operaciones.agregarFinal(op);
        if (op.getAsesor() != null) op.getAsesor().incrementarCierres();
        historialAcciones.push(new Accion<>(TipoAccion.REGISTRO_OPERACION, null, op,
                "Operación registrada: " + op.getId()));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CONTRATOS
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarContrato(Contrato contrato) {
        if (contrato == null) throw new ParametroVacioException("Contrato nulo");
        contratos.agregarFinal(contrato);
        historialAcciones.push(new Accion<>(TipoAccion.REGISTRO_CONTRATO, null, contrato,
                "Contrato registrado: " + contrato.getId()));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ALERTAS — ColaPrioridadLista<Alerta>
    // ═════════════════════════════════════════════════════════════════════════

    public void generarAlerta(Alerta alerta) {
        alertas.insertar(alerta);
        alertasRegistro.agregarFinal(alerta);
    }

    public Alerta obtenerAlertaPrioritaria() {
        return alertas.extraerMaximo();
    }



    // ═════════════════════════════════════════════════════════════════════════
    // INTENCIONES DE COMPRA/ARRIENDO (req. 4.5 PDF)
    // ═════════════════════════════════════════════════════════════════════════

    public void registrarIntencion(Intencion intencion) {
        if (intencion == null) throw new ParametroVacioException("Intención nula");
        intenciones.agregarFinal(intencion);
        // Registrar en historial del cliente
        if (intencion.getCliente() != null && intencion.getInmueble() != null) {
            intencion.getCliente().registrarConsulta(intencion.getInmueble());
        }
        historialAcciones.push(new Accion<>(TipoAccion.CREACION_CLIENTE,
                null, intencion, "Intención registrada: " + intencion.getId()));
    }

    public ListaSimple<Intencion> getIntencionesPorCliente(String clienteId) {
        ListaSimple<Intencion> resultado = new ListaSimple<>();
        for (int i = 0; i < intenciones.tamanio(); i++) {
            Intencion inten = intenciones.obtener(i);
            if (inten.getCliente() != null && inten.getCliente().getId().equals(clienteId)) {
                resultado.agregarFinal(inten);
            }
        }
        return resultado;
    }

    /** Clientes con alta probabilidad de cierre (intenciones activas + presupuesto alto) */
    public ListaSimple<Cliente> clientesAltaProbabilidadCierre() {
        ListaSimple<Cliente> resultado = new ListaSimple<>();
        for (int i = 0; i < clientesLista.tamanio(); i++) {
            Cliente c = clientesLista.obtener(i);
            boolean tieneIntencion = false;
            for (int j = 0; j < intenciones.tamanio(); j++) {
                Intencion inten = intenciones.obtener(j);
                if (inten.getCliente() != null && inten.getCliente().getId().equals(c.getId())
                        && inten.getEstado() == Intencion.EstadoIntencion.ACTIVA) {
                    tieneIntencion = true; break;
                }
            }
            if (tieneIntencion) resultado.agregarFinal(c);
        }
        return resultado;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // EDICIÓN DE INMUEBLE (req. 6.1 PDF)
    // ═════════════════════════════════════════════════════════════════════════

    public void editarInmueble(String codigo, String direccion, String zona,
                                double precio, double area, int habitaciones,
                                int banios, String finalidad) {
        Inmueble inm = buscarInmueble(codigo);
        if (inm == null) throw new ElementoNoEncontradoException("Inmueble no encontrado: " + codigo);
        if (direccion != null && !direccion.isBlank()) inm.setDireccion(direccion);
        if (zona      != null && !zona.isBlank())      inm.setZona(zona);
        if (precio    > 0) actualizarPrecioInmueble(codigo, precio);
        if (area      > 0) inm.setArea(area);
        if (habitaciones >= 0) inm.setHabitaciones(habitaciones);
        if (banios    >= 0) inm.setBanios(banios);
        if (finalidad != null && !finalidad.isBlank()) inm.setFinalidad(finalidad);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // EDICIÓN DE CLIENTE (req. 6.2 PDF)
    // ═════════════════════════════════════════════════════════════════════════

    public void editarCliente(String id, String nombre, String telefono,
                               double presupuesto, String zonasInteres) {
        Cliente c = buscarCliente(id);
        if (c == null) throw new ElementoNoEncontradoException("Cliente no encontrado: " + id);
        if (nombre      != null && !nombre.isBlank())      c.setNombre(nombre);
        if (telefono    != null && !telefono.isBlank())    c.setTelefono(telefono);
        if (presupuesto  > 0)                              c.setPresupuesto(presupuesto);
        if (zonasInteres != null && !zonasInteres.isBlank()) c.setZonasInteres(zonasInteres);
        historialAcciones.push(new Accion<>(TipoAccion.CREACION_CLIENTE,
                null, c, "Cliente editado: " + c.getNombre()));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // RANKING DE INMUEBLES POR DEMANDA (req. 8 PDF)
    // ═════════════════════════════════════════════════════════════════════════

    public ListaSimple<Inmueble> rankingInmueblesPorDemanda() {
        java.util.List<Inmueble> lista = inmueblesLista.toJavaList();
        lista.sort((a, b) -> Integer.compare(b.getVisitas(), a.getVisitas()));
        ListaSimple<Inmueble> resultado = new ListaSimple<>();
        for (Inmueble i : lista) resultado.agregarFinal(i);
        return resultado;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CONTRATOS PRÓXIMOS A VENCER (req. 4.7 PDF)
    // ═════════════════════════════════════════════════════════════════════════

    public ListaSimple<Contrato> contratosProximosVencer(int diasLimite) {
        ListaSimple<Contrato> resultado = new ListaSimple<>();
        LocalDate hoy = LocalDate.now();
        for (int i = 0; i < contratos.tamanio(); i++) {
            Contrato c = contratos.obtener(i);
            if (c.getFechaFin() != null && c.getEstado() == Contrato.EstadoContrato.ACTIVO) {
                long dias = java.time.temporal.ChronoUnit.DAYS.between(hoy, c.getFechaFin());
                if (dias >= 0 && dias <= diasLimite) resultado.agregarFinal(c);
            }
        }
        return resultado;
    }

    /** Detecta automáticamente comportamientos inusuales y genera alertas (req. 4.9). */
    public void detectarComportamientoInusual() {
        // 1. Inmuebles con demasiadas visitas sin cierre
        for (int i = 0; i < inmueblesLista.tamanio(); i++) {
            Inmueble inm = inmueblesLista.obtener(i);
            if (inm.getVisitas() >= 5 && inm.getEstado() == EstadoInmueble.DISPONIBLE) {
                generarAlerta(new Alerta("ALT-V-" + inm.getCodigo(),
                        "Inmueble " + inm.getCodigo() + " tiene " + inm.getVisitas() + " visitas sin cierre",
                        Alerta.NivelAlerta.ALTA, "VISITAS_SIN_CIERRE"));
            }
        }
        // 2. Asesores con sobrecarga
        for (int i = 0; i < asesoresLista.tamanio(); i++) {
            Asesor a = asesoresLista.obtener(i);
            if (a.getVisitasAtendidas().tamanio() >= 6) {
                generarAlerta(new Alerta("ALT-A-" + a.getId(),
                        "Asesor " + a.getNombre() + " tiene sobrecarga (" + a.getVisitasAtendidas().tamanio() + " visitas)",
                        Alerta.NivelAlerta.MEDIA, "SOBRECARGA_ASESOR"));
            }
        }
        // 3. Contratos próximos a vencer (60 días)
        ListaSimple<Contrato> prox = contratosProximosVencer(60);
        for (int i = 0; i < prox.tamanio(); i++) {
            Contrato ct = prox.obtener(i);
            generarAlerta(new Alerta("ALT-CT-" + ct.getId(),
                    "Contrato " + ct.getId() + " vence en menos de 60 días",
                    Alerta.NivelAlerta.ALTA, "CONTRATO_PRONTO_VENCER"));
        }
        // 4. Inmuebles reservados sin cierre por más de 30 días (simulado)
        for (int i = 0; i < inmueblesLista.tamanio(); i++) {
            Inmueble inm = inmueblesLista.obtener(i);
            if (inm.getEstado() == EstadoInmueble.RESERVADO) {
                generarAlerta(new Alerta("ALT-R-" + inm.getCodigo(),
                        "Inmueble " + inm.getCodigo() + " lleva tiempo RESERVADO sin cierre",
                        Alerta.NivelAlerta.MEDIA, "RESERVADO_SIN_CIERRE"));
            }
        }
        // 5. Clientes sin seguimiento
        for (int i = 0; i < clientesLista.tamanio(); i++) {
            Cliente c = clientesLista.obtener(i);
            if (c.getHistorialVisitas().estaVacia() && c.getEstadoBusqueda() == Cliente.EstadoBusqueda.BUSCANDO) {
                generarAlerta(new Alerta("ALT-C-" + c.getId(),
                        "Cliente " + c.getNombre() + " sin visitas ni seguimiento",
                        Alerta.NivelAlerta.BAJA, "CLIENTE_SIN_SEGUIMIENTO"));
            }
        }
    }



    public ListaSimple<Inmueble> recomendarInmuebles(Cliente cliente) {
        if (cliente == null) return new ListaSimple<>();
        ListaSimple<Inmueble> recomendaciones = new ListaSimple<>();
        for (int i = 0; i < inmueblesLista.tamanio(); i++) {
            Inmueble inm = inmueblesLista.obtener(i);
            if (inm.getEstado() != EstadoInmueble.DISPONIBLE) continue;
            boolean precioOk = inm.getPrecio() <= cliente.getPresupuesto();
            boolean tipoOk   = cliente.getTipoDeseado() == null || inm.getTipo() == cliente.getTipoDeseado();
            boolean habOk    = inm.getHabitaciones() >= cliente.getMinHabitaciones();
            boolean zonaOk   = cliente.getZonasInteres() == null || cliente.getZonasInteres().isBlank()
                    || cliente.getZonasInteres().toLowerCase().contains(inm.getZona().toLowerCase());
            if (precioOk && tipoOk && habOk) {
                recomendaciones.agregarFinal(inm);
            }
        }
        return recomendaciones;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FILTRO MULTI-CRITERIO
    // ═════════════════════════════════════════════════════════════════════════

    public ListaSimple<Inmueble> filtrarInmuebles(TipoInmueble tipo, String finalidad,
                                                   double precioMin, double precioMax,
                                                   int minHabitaciones) {
        ListaSimple<Inmueble> resultado = new ListaSimple<>();
        for (int i = 0; i < inmueblesLista.tamanio(); i++) {
            Inmueble inm = inmueblesLista.obtener(i);
            if (tipo != null && inm.getTipo() != tipo) continue;
            if (finalidad != null && !finalidad.isBlank()
                    && !inm.getFinalidad().equalsIgnoreCase(finalidad)) continue;
            if (precioMin > 0 && inm.getPrecio() < precioMin) continue;
            if (precioMax > 0 && inm.getPrecio() > precioMax) continue;
            if (minHabitaciones > 0 && inm.getHabitaciones() < minHabitaciones) continue;
            resultado.agregarFinal(inm);
        }
        return resultado;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // RANKING
    // ═════════════════════════════════════════════════════════════════════════

    /** Ranking de zonas por número de inmuebles. */
    public Map<String, Integer> rankingZonas() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        for (int i = 0; i < inmueblesLista.tamanio(); i++) {
            String zona = inmueblesLista.obtener(i).getZona();
            if (zona != null) conteo.merge(zona, 1, Integer::sum);
        }
        // Ordenar por valor descendente
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(conteo.entrySet());
        entries.sort((a, b) -> b.getValue() - a.getValue());
        Map<String, Integer> ordenado = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : entries) ordenado.put(e.getKey(), e.getValue());
        return ordenado;
    }

    /** Ranking de asesores por efectividad. */
    public ListaSimple<Asesor> rankingAsesores() {
        // Copia y ordena por efectividad descendente
        java.util.List<Asesor> lista = asesoresLista.toJavaList();
        lista.sort((a, b) -> Double.compare(b.getEfectividad(), a.getEfectividad()));
        ListaSimple<Asesor> resultado = new ListaSimple<>();
        for (Asesor a : lista) resultado.agregarFinal(a);
        return resultado;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // FAVORITOS
    // ═════════════════════════════════════════════════════════════════════════

    public void toggleFavorito(String clienteId, String inmuebleCodigo) {
        Cliente c = buscarCliente(clienteId);
        Inmueble inm = buscarInmueble(inmuebleCodigo);
        if (c == null || inm == null) return;
        if (c.getFavoritos().contiene(inm)) c.quitarFavorito(inm);
        else c.agregarFavorito(inm);
    }

    // ═════════════════════════════════════════════════════════════════════════
    // DESHACER (PILA)
    // ═════════════════════════════════════════════════════════════════════════

    public String deshacerUltimaAccion() {
        if (historialAcciones.estaVacia()) return "No hay acciones para deshacer";
        Accion<?> accion = historialAcciones.pop();
        return "Deshecho: " + accion.getDescripcion();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CONEXIÓN DE ZONAS
    // ═════════════════════════════════════════════════════════════════════════

    public void conectarZonas(String zona1, String zona2, int distancia) {
        grafoZonas.agregarArista(zona1, zona2, distancia);
    }
}
