package com.edu.uniquindio.proptech.app;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.Inmueble;
import com.edu.uniquindio.proptech.modelo.operaciones.*;
import com.edu.uniquindio.proptech.modelo.usuario.*;
import com.edu.uniquindio.proptech.utils.alerta.Accion;
import com.edu.uniquindio.proptech.utils.alerta.Alerta;

import java.time.format.DateTimeFormatter;

/**
 * Utilidad para serialización manual a JSON.
 * No depende de librerías externas para cumplir el requisito académico.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class JsonUtil {

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter D_FMT   = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ── Wrappers base ─────────────────────────────────────────────────────────

    public static String ok(String data) {
        return "{\"ok\":true,\"data\":" + data + "}";
    }

    public static String msg(String mensaje) {
        return "{\"ok\":true,\"msg\":\"" + esc(mensaje) + "\"}";
    }

    public static String error(String msg) {
        return "{\"ok\":false,\"error\":\"" + esc(msg == null ? "Error desconocido" : msg) + "\"}";
    }

    // ── Entidades individuales ────────────────────────────────────────────────

    public static String inmueble(Inmueble i) {
        if (i == null) return "null";
        return "{\"codigo\":\"" + esc(i.getCodigo()) + "\","
                + "\"direccion\":\"" + esc(i.getDireccion()) + "\","
                + "\"ciudad\":\"" + esc(i.getCiudad()) + "\","
                + "\"zona\":\"" + esc(i.getZona()) + "\","
                + "\"tipo\":\"" + i.getTipo() + "\","
                + "\"finalidad\":\"" + esc(i.getFinalidad()) + "\","
                + "\"precio\":" + i.getPrecio() + ","
                + "\"area\":" + i.getArea() + ","
                + "\"habitaciones\":" + i.getHabitaciones() + ","
                + "\"banios\":" + i.getBanios() + ","
                + "\"estado\":\"" + i.getEstado() + "\","
                + "\"visitas\":" + i.getVisitas() + ","
                + "\"asesorId\":\"" + (i.getAsesor() != null ? esc(i.getAsesor().getId()) : "") + "\","
                + "\"asesorNombre\":\"" + (i.getAsesor() != null ? esc(i.getAsesor().getNombre()) : "") + "\","
                + "\"fotoUrl\":\"" + esc(i.getFotoUrl() != null ? i.getFotoUrl() : "") + "\"}";
    }

    public static String cliente(Cliente c) {
        if (c == null) return "null";
        return "{\"id\":\"" + esc(c.getId()) + "\","
                + "\"nombre\":\"" + esc(c.getNombre()) + "\","
                + "\"correo\":\"" + esc(c.getCorreo()) + "\","
                + "\"telefono\":\"" + esc(c.getTelefono() != null ? c.getTelefono() : "") + "\","
                + "\"tipo\":\"" + c.getTipoCliente() + "\","
                + "\"presupuesto\":" + c.getPresupuesto() + ","
                + "\"zonasInteres\":\"" + esc(c.getZonasInteres() != null ? c.getZonasInteres() : "") + "\","
                + "\"tipoDeseado\":\"" + (c.getTipoDeseado() != null ? c.getTipoDeseado() : "") + "\","
                + "\"minHabitaciones\":" + c.getMinHabitaciones() + ","
                + "\"estadoBusqueda\":\"" + c.getEstadoBusqueda() + "\","
                + "\"favoritos\":" + c.getFavoritos().tamanio() + ","
                + "\"historialVisitas\":" + c.getHistorialVisitas().tamanio() + "}";
    }

    public static String asesor(Asesor a) {
        if (a == null) return "null";
        return "{\"id\":\"" + esc(a.getId()) + "\","
                + "\"nombre\":\"" + esc(a.getNombre()) + "\","
                + "\"correo\":\"" + esc(a.getCorreo()) + "\","
                + "\"telefono\":\"" + esc(a.getTelefono() != null ? a.getTelefono() : "") + "\","
                + "\"zonaAsignada\":\"" + esc(a.getZonaAsignada() != null ? a.getZonaAsignada() : "") + "\","
                + "\"inmuebles\":" + a.getInmueblesAsignados().tamanio() + ","
                + "\"visitas\":" + a.getVisitasAtendidas().tamanio() + ","
                + "\"cierres\":" + a.getCierresRealizados() + ","
                + "\"efectividad\":" + a.getEfectividad() + "}";
    }

    public static String visita(Visita v) {
        if (v == null) return "null";
        String fecha = v.getFechayHora() != null ? v.getFechayHora().format(DT_FMT) : "";
        return "{\"clienteId\":\"" + esc(v.getCliente() != null ? v.getCliente().getId() : "") + "\","
                + "\"clienteNombre\":\"" + esc(v.getCliente() != null ? v.getCliente().getNombre() : "") + "\","
                + "\"inmuebleCod\":\"" + esc(v.getInmueble() != null ? v.getInmueble().getCodigo() : "") + "\","
                + "\"asesorId\":\"" + esc(v.getAsesor() != null ? v.getAsesor().getId() : "") + "\","
                + "\"asesorNombre\":\"" + esc(v.getAsesor() != null ? v.getAsesor().getNombre() : "") + "\","
                + "\"fechayHora\":\"" + esc(fecha) + "\","
                + "\"estado\":\"" + v.getEstado() + "\","
                + "\"observaciones\":\"" + esc(v.getObservaciones() != null ? v.getObservaciones() : "") + "\"}";
    }

    public static String operacion(Operacion op) {
        if (op == null) return "null";
        String fecha = op.getFecha() != null ? op.getFecha().format(D_FMT) : "";
        return "{\"id\":\"" + esc(op.getId()) + "\","
                + "\"tipo\":\"" + op.getTipo() + "\","
                + "\"estado\":\"" + op.getEstado() + "\","
                + "\"fecha\":\"" + fecha + "\","
                + "\"valor\":" + op.getValor() + ","
                + "\"comision\":" + op.getComision() + ","
                + "\"inmuebleCodigo\":\"" + esc(op.getInmueble() != null ? op.getInmueble().getCodigo() : "") + "\","
                + "\"clienteNombre\":\"" + esc(op.getCliente() != null ? op.getCliente().getNombre() : "") + "\","
                + "\"asesorNombre\":\"" + esc(op.getAsesor() != null ? op.getAsesor().getNombre() : "") + "\"}";
    }

    public static String contrato(Contrato c) {
        if (c == null) return "null";
        String fi = c.getFechaInicio() != null ? c.getFechaInicio().format(D_FMT) : "";
        String ff = c.getFechaFin()    != null ? c.getFechaFin().format(D_FMT)    : "";
        return "{\"id\":\"" + esc(c.getId()) + "\","
                + "\"tipo\":\"" + c.getTipo() + "\","
                + "\"estado\":\"" + c.getEstado() + "\","
                + "\"fechaInicio\":\"" + fi + "\","
                + "\"fechaFin\":\"" + ff + "\","
                + "\"valorMensual\":" + c.getValorMensual() + ","
                + "\"inmuebleCodigo\":\"" + esc(c.getInmueble() != null ? c.getInmueble().getCodigo() : "") + "\","
                + "\"clienteNombre\":\"" + esc(c.getCliente() != null ? c.getCliente().getNombre() : "") + "\"}";
    }

    public static String alerta(Alerta a) {
        if (a == null) return "null";
        String fecha = a.getFecha() != null ? a.getFecha().format(DT_FMT) : "";
        return "{\"id\":\"" + esc(a.getId()) + "\","
                + "\"mensaje\":\"" + esc(a.getMensaje()) + "\","
                + "\"nivel\":\"" + a.getNivel() + "\","
                + "\"tipo\":\"" + esc(a.getTipo() != null ? a.getTipo() : "") + "\","
                + "\"fecha\":\"" + fecha + "\","
                + "\"resuelta\":" + a.isResuelta() + "}";
    }

    public static String accion(Accion<?> a) {
        if (a == null) return "null";
        String fecha = a.getFecha() != null ? a.getFecha().format(DT_FMT) : "";
        return "{\"tipo\":\"" + a.getTipo() + "\","
                + "\"descripcion\":\"" + esc(a.getDescripcion()) + "\","
                + "\"fecha\":\"" + fecha + "\"}";
    }

    // ── Listas ────────────────────────────────────────────────────────────────

    public static String listaInmuebles(ListaSimple<Inmueble> lista) {
        return buildArray(lista, JsonUtil::inmueble);
    }

    public static String listaClientes(ListaSimple<Cliente> lista) {
        return buildArray(lista, JsonUtil::cliente);
    }

    public static String listaAsesores(ListaSimple<Asesor> lista) {
        return buildArray(lista, JsonUtil::asesor);
    }

    public static String listaVisitas(ListaSimple<Visita> lista) {
        return buildArray(lista, JsonUtil::visita);
    }

    public static String listaOperaciones(ListaSimple<Operacion> lista) {
        return buildArray(lista, JsonUtil::operacion);
    }

    public static String listaContratos(ListaSimple<Contrato> lista) {
        return buildArray(lista, JsonUtil::contrato);
    }

    public static String listaAlertas(ListaSimple<Alerta> lista) {
        return buildArray(lista, JsonUtil::alerta);
    }

    public static String listaAcciones(ListaSimple<Accion<?>> lista) {
        return buildArray(lista, JsonUtil::accion);
    }

    public static String intencion(Intencion i) {
        if (i == null) return "null";
        String fecha = i.getFecha() != null ? i.getFecha().format(DT_FMT) : "";
        return "{\"id\":\"" + esc(i.getId()) + "\","
                + "\"tipo\":\"" + i.getTipo() + "\","
                + "\"estado\":\"" + i.getEstado() + "\","
                + "\"oferta\":" + i.getOferta() + ","
                + "\"observaciones\":\"" + esc(i.getObservaciones()) + "\","
                + "\"fecha\":\"" + fecha + "\","
                + "\"inmuebleCodigo\":\"" + esc(i.getInmueble() != null ? i.getInmueble().getCodigo() : "") + "\","
                + "\"inmuebleTipo\":\"" + (i.getInmueble() != null ? i.getInmueble().getTipo().toString() : "") + "\","
                + "\"clienteNombre\":\"" + esc(i.getCliente() != null ? i.getCliente().getNombre() : "") + "\","
                + "\"clienteId\":\"" + esc(i.getCliente() != null ? i.getCliente().getId() : "") + "\"}";
    }

    public static String listaIntenciones(ListaSimple<Intencion> lista) {
        return buildArray(lista, JsonUtil::intencion);
    }

    public static <T> String listaFromJavaList(java.util.List<T> lista,
                                                java.util.function.Function<T, String> mapper) {
        if (lista == null || lista.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(mapper.apply(lista.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static <T> String buildArray(ListaSimple<T> lista,
                                          java.util.function.Function<T, String> mapper) {
        if (lista == null || lista.estaVacia()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.tamanio(); i++) {
            if (i > 0) sb.append(",");
            sb.append(mapper.apply(lista.obtener(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    /** Escapa caracteres especiales en strings JSON. */
    public static String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
