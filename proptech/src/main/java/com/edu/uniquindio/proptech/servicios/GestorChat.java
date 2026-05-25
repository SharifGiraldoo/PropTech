package com.edu.uniquindio.proptech.servicios;

import com.edu.uniquindio.proptech.modelo.MensajeChat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/**
 * Gestor de chat en tiempo real con Server-Sent Events (SSE).
 * Persiste mensajes en data/chat.csv y notifica a suscriptores SSE.
 * Autores: Sharif Giraldo, Juan S. Hernández, Santiago Ospina
 */
public class GestorChat {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String SEP    = "|";
    private static final String SEP_RX = "\\|";

    private final Map<String, List<MensajeChat>> mensajes    = new ConcurrentHashMap<>();
    private final Map<String, List<OutputStream>> suscriptores = new ConcurrentHashMap<>();
    private final Path csvPath;
    private long contadorId = 1;

    public GestorChat(String dirData) {
        Path dir = Paths.get(dirData);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}
        this.csvPath = dir.resolve("chat.csv");
        cargarHistorial();
    }

    // ── API pública ────────────────────────────────────────────────────────────

    public synchronized MensajeChat enviar(String remitenteId, String remitenteNombre,
                                            String remitenteRol, String canal, String contenido) {
        String id = "MSG-" + String.format("%06d", contadorId++);
        MensajeChat msg = new MensajeChat(id, remitenteId, remitenteNombre, remitenteRol, canal, contenido);
        mensajes.computeIfAbsent(canal, k -> new ArrayList<>()).add(msg);
        persistir(msg);
        notificar(canal, msg);
        return msg;
    }

    public List<MensajeChat> getHistorial(String canal) {
        return Collections.unmodifiableList(mensajes.getOrDefault(canal, Collections.emptyList()));
    }

    public Map<String, List<MensajeChat>> getTodosLosCanales() {
        return Collections.unmodifiableMap(mensajes);
    }

    public void suscribir(String canal, OutputStream os) {
        suscriptores.computeIfAbsent(canal, k -> new CopyOnWriteArrayList<>()).add(os);
    }

    public void desuscribir(String canal, OutputStream os) {
        List<OutputStream> lista = suscriptores.get(canal);
        if (lista != null) lista.remove(os);
    }

    // ── SSE ────────────────────────────────────────────────────────────────────

    private void notificar(String canal, MensajeChat msg) {
        byte[] bytes = ("data: " + msg.toJson() + "\n\n").getBytes(StandardCharsets.UTF_8);
        enviarALista(suscriptores.getOrDefault(canal,     Collections.emptyList()), bytes, canal);
        if (!"admin".equals(canal))
            enviarALista(suscriptores.getOrDefault("admin", Collections.emptyList()), bytes, "admin");
    }

    private void enviarALista(List<OutputStream> lista, byte[] bytes, String canal) {
        List<OutputStream> muertos = new ArrayList<>();
        for (OutputStream os : lista) {
            try { os.write(bytes); os.flush(); }
            catch (IOException e) { muertos.add(os); }
        }
        if (!muertos.isEmpty()) {
            List<OutputStream> actual = suscriptores.get(canal);
            if (actual != null) actual.removeAll(muertos);
        }
    }

    // ── Persistencia ──────────────────────────────────────────────────────────

    private synchronized void persistir(MensajeChat msg) {
        try (BufferedWriter bw = Files.newBufferedWriter(csvPath,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(String.join(SEP, esc(msg.getId()), esc(msg.getRemitenteId()),
                    esc(msg.getRemitenteNombre()), esc(msg.getRemitenteRol()),
                    esc(msg.getDestinatarioId()), esc(msg.getContenido()),
                    msg.getFecha().format(FMT)));
            bw.newLine();
        } catch (IOException e) { System.err.println("[Chat] Error: " + e.getMessage()); }
    }

    private void cargarHistorial() {
        if (!Files.exists(csvPath)) return;
        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] f = line.split(SEP_RX, -1);
                if (f.length < 7) continue;
                try {
                    LocalDateTime fecha = LocalDateTime.parse(f[6].trim(), FMT);
                    MensajeChat msg = new MensajeChat(des(f[0]),des(f[1]),des(f[2]),des(f[3]),des(f[4]),des(f[5]),fecha);
                    mensajes.computeIfAbsent(des(f[4]), k -> new ArrayList<>()).add(msg);
                    try { long n=Long.parseLong(des(f[0]).replace("MSG-",""));if(n>=contadorId)contadorId=n+1; }
                    catch(Exception ignored){}
                } catch (Exception e) { System.err.println("[Chat] Skip: "+e.getMessage()); }
            }
            System.out.println("[Chat] "+mensajes.values().stream().mapToInt(List::size).sum()+" mensajes cargados");
        } catch (IOException e) { System.err.println("[Chat] Error carga: "+e.getMessage()); }
    }

    private static String esc(String v){ return v==null?"":v.replace(SEP,"&#124;").replace("\n"," ").replace("\r",""); }
    private static String des(String v){ return v==null?"":v.replace("&#124;",SEP); }
}
