package com.edu.uniquindio.proptech.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mensaje de chat entre cliente, admin y asesor.
 * Autores: Sharif Giraldo Obando, Juan S. Hernández, Santiago Ospina Sánchez
 */
public class MensajeChat {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String remitenteId;
    private final String remitenteNombre;
    private final String remitenteRol;
    private final String destinatarioId;
    private final String contenido;
    private LocalDateTime fecha; // mutable para permitir carga desde CSV

    public MensajeChat(String id, String remitenteId, String remitenteNombre,
                       String remitenteRol, String destinatarioId, String contenido) {
        this.id              = id;
        this.remitenteId     = remitenteId     != null ? remitenteId     : "";
        this.remitenteNombre = remitenteNombre != null ? remitenteNombre : "";
        this.remitenteRol    = remitenteRol    != null ? remitenteRol    : "";
        this.destinatarioId  = destinatarioId  != null ? destinatarioId  : "";
        this.contenido       = contenido       != null ? contenido       : "";
        this.fecha           = LocalDateTime.now();
    }

    /** Constructor para cargar desde CSV con fecha explícita. */
    public MensajeChat(String id, String remitenteId, String remitenteNombre,
                       String remitenteRol, String destinatarioId,
                       String contenido, LocalDateTime fecha) {
        this(id, remitenteId, remitenteNombre, remitenteRol, destinatarioId, contenido);
        this.fecha = fecha;
    }

    public String getId()              { return id; }
    public String getRemitenteId()     { return remitenteId; }
    public String getRemitenteNombre() { return remitenteNombre; }
    public String getRemitenteRol()    { return remitenteRol; }
    public String getDestinatarioId()  { return destinatarioId; }
    public String getContenido()       { return contenido; }
    public LocalDateTime getFecha()    { return fecha; }

    public String toJson() {
        return "{\"id\":\""              + esc(id)              + "\","
             + "\"remitenteId\":\""      + esc(remitenteId)     + "\","
             + "\"remitenteNombre\":\"" + esc(remitenteNombre) + "\","
             + "\"remitenteRol\":\""    + esc(remitenteRol)    + "\","
             + "\"destinatarioId\":\"" + esc(destinatarioId)  + "\","
             + "\"contenido\":\""       + esc(contenido)        + "\","
             + "\"fecha\":\""           + fecha.format(FMT)    + "\"}";
    }

    private static String esc(String v) {
        if (v == null) return "";
        return v.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "");
    }
}
