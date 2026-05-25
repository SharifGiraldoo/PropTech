package com.edu.uniquindio.proptech.app;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.*;
import com.edu.uniquindio.proptech.modelo.operaciones.*;
import com.edu.uniquindio.proptech.modelo.operaciones.Intencion;
import com.edu.uniquindio.proptech.modelo.usuario.*;
import com.edu.uniquindio.proptech.servicios.modulos.*;
import com.edu.uniquindio.proptech.utils.alerta.*;
import com.edu.uniquindio.proptech.utils.alerta.Accion.TipoAccion;
import com.edu.uniquindio.proptech.utils.excepciones.*;
import com.edu.uniquindio.proptech.utils.sesion.Sesion;
import com.edu.uniquindio.proptech.utils.PersistenciaCSV;
import com.edu.uniquindio.proptech.servicios.GestorChat;
import com.edu.uniquindio.proptech.modelo.MensajeChat;
import java.util.List;
import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * ══════════════════════════════════════════════════════════════════
 * App — Servidor HTTP embebido PropTech
 *
 * Sirve el frontend HTML estático y expone API REST en /api/*
 *
 * CREDENCIALES DE ACCESO (semilla):
 *   Administrador : admin@proptech.co      / admin123
 *   Asesor 1      : ana@proptech.co        / ana123
 *   Asesor 2      : carlos@proptech.co     / car123
 *   Asesor 3      : lucia@proptech.co      / luc123
 *   Cliente 1     : mario@gmail.com        / mar123
 *   Cliente 2     : sandra@gmail.com       / san123
 *   Cliente 3     : pedro@gmail.com        / ped123
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 * ══════════════════════════════════════════════════════════════════
 */
public class App {

    private static final int PORT = 8080;
    private static SistemaInmobiliario sistema;
    private static PersistenciaCSV csv;
    private static GestorChat chat;
    private static final String DATA_DIR = "data";
    private static ServiciosUsuario    serviciosUsuario;

    // ═══════════════════════════════════════════════════════════════════════
    // MAIN
    // ═══════════════════════════════════════════════════════════════════════

    public static void main(String[] args) throws Exception {
        sistema = new SistemaInmobiliario();
        csv  = new PersistenciaCSV(DATA_DIR);
        chat = new GestorChat(DATA_DIR);

        // API key de Anthropic — env var tiene prioridad
        String apiKeyCheck = System.getenv("ANTHROPIC_API_KEY");
        if (apiKeyCheck == null || apiKeyCheck.isBlank())
            apiKeyCheck = System.getProperty("ANTHROPIC_API_KEY", "");
        if (apiKeyCheck == null || apiKeyCheck.isBlank())
            apiKeyCheck = "integrada";
        System.out.println("  Chatbot IA activo (" + apiKeyCheck.substring(0, Math.min(8, apiKeyCheck.length())) + "…)");
        // Inicializar siempre el admin (no persiste en CSV por seguridad)
        List<Usuario> admins = new java.util.ArrayList<>();
        admins.add(new Admin("ADM-001", "Admin PropTech", "admin@proptech.co", "000-000-0000", "admin123"));
        serviciosUsuario = new ServiciosUsuario(admins, sistema);
        // Cargar datos persistidos; si no existen, usar semilla
        boolean cargado = csv.cargarTodo(sistema);
        if (!cargado) {
            cargarDatosSemilla();
            csv.guardarTodo(sistema);
        }
        iniciarServidor();
        System.out.println("════════════════════════════════════════════════");
        System.out.println("  PropTech — Universidad del Quindío 2026-1");
        System.out.println("  Servidor:  http://localhost:" + PORT);
        System.out.println("────────────────────────────────────────────────");
        System.out.println("  admin@proptech.co   / admin123  (Admin)");
        System.out.println("  ana@proptech.co     / ana123    (Asesor)");
        System.out.println("  mario@gmail.com     / mar123    (Cliente)");
        System.out.println("════════════════════════════════════════════════");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DATOS DE SEMILLA
    // ═══════════════════════════════════════════════════════════════════════

    private static void cargarDatosSemilla() {
        // ── Admin ──────────────────────────────────────────────────────────
        List<Usuario> admins = new ArrayList<>();
        admins.add(new Admin("ADM-001", "Admin PropTech",
                "admin@proptech.co", "000-000-0000", "admin123"));
        serviciosUsuario = new ServiciosUsuario(admins, sistema);

        // ── Asesores ───────────────────────────────────────────────────────
        Asesor a1 = new Asesor("ASE-001", "Ana Martínez",  "ana@proptech.co",    "315-111-2222", "Norte",   "ana123");
        Asesor a2 = new Asesor("ASE-002", "Carlos Gómez",  "carlos@proptech.co", "315-333-4444", "Sur",     "car123");
        Asesor a3 = new Asesor("ASE-003", "Lucía Herrera", "lucia@proptech.co",  "315-555-6666", "Centro",  "luc123");
        sistema.registrarAsesor(a1);
        sistema.registrarAsesor(a2);
        sistema.registrarAsesor(a3);

        // ── Clientes ───────────────────────────────────────────────────────
        Cliente c1 = new Cliente("CLI-001", "Mario Restrepo",  "mario@gmail.com",  "300-111-1111", "mar123",
                Cliente.TipoCliente.COMPRADOR, 350_000_000, "Norte,Centro",
                TipoInmueble.APARTAMENTO, 2);
        Cliente c2 = new Cliente("CLI-002", "Sandra Vélez",   "sandra@gmail.com", "300-222-2222", "san123",
                Cliente.TipoCliente.ARRENDATARIO, 2_500_000, "Sur,Centro",
                TipoInmueble.CASA, 3);
        Cliente c3 = new Cliente("CLI-003", "Pedro Salcedo",  "pedro@gmail.com",  "300-333-3333", "ped123",
                Cliente.TipoCliente.INVERSIONISTA, 800_000_000, "Industrial",
                TipoInmueble.LOCALCOMERCIAL, 0);
        sistema.registrarCliente(c1);
        sistema.registrarCliente(c2);
        sistema.registrarCliente(c3);

        // ── Inmuebles ──────────────────────────────────────────────────────
        Inmueble[] inms = {
            new Inmueble("INM-001","Cra 15 # 20-30",   "Armenia","Norte",      TipoInmueble.APARTAMENTO,  "VENTA",   280_000_000, 85,  3, 2, EstadoInmueble.DISPONIBLE, a1),
            new Inmueble("INM-002","Cl 25 # 10-50",    "Armenia","Sur",        TipoInmueble.CASA,         "ARRIENDO",  1_800_000, 160, 4, 3, EstadoInmueble.DISPONIBLE, a2),
            new Inmueble("INM-003","Av Bolívar # 5-10","Calarcá", "Centro",    TipoInmueble.LOCALCOMERCIAL,"VENTA",  450_000_000, 120, 0, 2, EstadoInmueble.DISPONIBLE, a3),
            new Inmueble("INM-004","Cll 10 # 8-20",    "Armenia","Sur",        TipoInmueble.APARTAMENTO,  "VENTA",   195_000_000, 65,  2, 1, EstadoInmueble.DISPONIBLE, a2),
            new Inmueble("INM-005","Cra 20 # 30-15",   "Armenia","Norte",      TipoInmueble.CASA,         "VENTA",   380_000_000, 210, 5, 3, EstadoInmueble.DISPONIBLE, a1),
            new Inmueble("INM-006","Cl 5 # 12-40",     "Armenia","Industrial", TipoInmueble.BODEGA,       "ARRIENDO",  2_200_000, 300, 0, 1, EstadoInmueble.DISPONIBLE, a3),
            new Inmueble("INM-007","Torre 3 Piso 4",   "Armenia","Centro",     TipoInmueble.OFICINA,      "ARRIENDO",  1_500_000, 50,  0, 1, EstadoInmueble.DISPONIBLE, a1),
            new Inmueble("INM-008","Cra 8 # 15-30",    "Armenia","Norte",      TipoInmueble.APARTAMENTO,  "ARRIENDO",    950_000, 55,  2, 1, EstadoInmueble.ARRENDADO,  a2),
        };
        for (Inmueble inm : inms) {
            sistema.registrarInmueble(inm);
                csv.guardarInmuebles(sistema);
        }
        // Asignar asesores
        sistema.asignarInmuebleAsesor("ASE-001", "INM-001");
        sistema.asignarInmuebleAsesor("ASE-001", "INM-005");
        sistema.asignarInmuebleAsesor("ASE-001", "INM-007");
        sistema.asignarInmuebleAsesor("ASE-002", "INM-002");
        sistema.asignarInmuebleAsesor("ASE-002", "INM-004");
        sistema.asignarInmuebleAsesor("ASE-002", "INM-008");
        sistema.asignarInmuebleAsesor("ASE-003", "INM-003");
        sistema.asignarInmuebleAsesor("ASE-003", "INM-006");

        // Visitas de muestra
        Visita v1 = new Visita(c1, inms[0], a1,
                LocalDateTime.now().plusDays(2), EstadoVisita.PENDIENTE, "Cliente muy interesado");
        Visita v2 = new Visita(c2, inms[1], a2,
                LocalDateTime.now().plusDays(3), EstadoVisita.PENDIENTE, "Busca casa familiar");
        Visita v3 = new Visita(c3, inms[2], a3,
                LocalDateTime.now().plusDays(1), EstadoVisita.CONFIRMADA, "Local comercial zona alta");
        sistema.agendarVisita(v1);
        sistema.agendarVisita(v2);
        sistema.agendarVisita(v3);

        // Operación de muestra
        Operacion op1 = new Operacion("OP-001", inms[7], c2, a2,
                TipoOperacion.ARRIENDO, LocalDate.now().minusDays(30),
                950_000, 3.5, EstadoOperacion.ACTIVA);
        sistema.registrarOperacion(op1);

        // Contrato de muestra
        Contrato ct1 = new Contrato("CT-001", inms[7], c2,
                Contrato.TipoContrato.ARRIENDO,
                LocalDate.now().minusDays(30), LocalDate.now().plusMonths(11),
                950_000);
        sistema.registrarContrato(ct1);

        // Alertas iniciales
        sistema.generarAlerta(new Alerta("ALT-001",
                "Contrato CT-001 vence en menos de 60 días",
                Alerta.NivelAlerta.ALTA, "CONTRATO_PRONTO_VENCER"));
        sistema.generarAlerta(new Alerta("ALT-002",
                "Inmueble INM-003 sin visitas en 30 días",
                Alerta.NivelAlerta.MEDIA, "INMUEBLE_SIN_ACTIVIDAD"));

        // Intenciones de compra/arriendo
        Intencion i1 = new Intencion("INT-001", c1, inms[0],
                Intencion.TipoIntencion.COMPRA, 270_000_000, "Precio negociable");
        Intencion i2 = new Intencion("INT-002", c2, inms[1],
                Intencion.TipoIntencion.ARRIENDO, 1_700_000, "Interesada en el barrio");
        Intencion i3 = new Intencion("INT-003", c3, inms[2],
                Intencion.TipoIntencion.COMPRA, 430_000_000, "Inversión comercial");
        sistema.registrarIntencion(i1);
        sistema.registrarIntencion(i2);
        sistema.registrarIntencion(i3);

        // Grafo de zonas
        sistema.conectarZonas("Norte",    "Centro",     5);
        sistema.conectarZonas("Centro",   "Sur",        4);
        sistema.conectarZonas("Sur",      "Industrial", 6);
        sistema.conectarZonas("Norte",    "Industrial", 10);
        sistema.conectarZonas("Centro",   "Industrial", 7);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SERVIDOR HTTP
    // ═══════════════════════════════════════════════════════════════════════

    private static void iniciarServidor() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newCachedThreadPool()); // ilimitado para SSE

        // Frontend estático
        server.createContext("/", App::servirFrontend);

        // API
        server.createContext("/api/health",           App::handleHealth);
        server.createContext("/api/dashboard",         App::handleDashboard);
        server.createContext("/api/inmuebles",         App::handleInmuebles);
        server.createContext("/api/clientes",          App::handleClientes);
        server.createContext("/api/asesores",          App::handleAsesores);
        server.createContext("/api/visitas",           App::handleVisitas);
        server.createContext("/api/operaciones",       App::handleOperaciones);
        server.createContext("/api/contratos",         App::handleContratos);
        server.createContext("/api/alertas",           App::handleAlertas);
        server.createContext("/api/recomendaciones",   App::handleRecomendaciones);
        server.createContext("/api/comportamiento",    App::handleComportamiento);
        server.createContext("/api/ranking/zonas",     App::handleRankingZonas);
        server.createContext("/api/ranking/asesores",  App::handleRankingAsesores);
        server.createContext("/api/historial",         App::handleHistorial);
        server.createContext("/api/grafo",             App::handleGrafo);
        server.createContext("/api/bst",               App::handleBst);
        server.createContext("/api/sesion/login",      App::handleLogin);
        server.createContext("/api/sesion/logout",     App::handleLogout);
        server.createContext("/api/filtrar",           App::handleFiltrar);
        server.createContext("/api/favoritos",         App::handleFavoritos);
        server.createContext("/api/perfil",            App::handlePerfil);
        server.createContext("/api/cliente/portal",   App::handleClientePortal);
        server.createContext("/api/asesor/portal",    App::handleAsesorPortal);
        server.createContext("/api/intenciones",      App::handleIntenciones);
        server.createContext("/api/ranking/inmuebles",App::handleRankingInmuebles);
        server.createContext("/api/inmuebles/editar", App::handleEditarInmueble);
        server.createContext("/api/clientes/editar",  App::handleEditarCliente);
        server.createContext("/api/inmuebles/foto",   App::handleFotoInmueble);
        server.createContext("/api/perfil/foto",      App::handleFotoPerfil);
        server.createContext("/fotos",                App::handleServirFoto);
        server.createContext("/api/chat/mensajes",    App::handleChatMensajes);
        server.createContext("/api/chat/enviar",      App::handleChatEnviar);
        server.createContext("/api/chat/canales",     App::handleChatCanales);
        server.createContext("/api/chat/sse",         App::handleChatSSE);
        server.createContext("/api/ia",               App::handleIA);

        server.start();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // FRONTEND ESTÁTICO
    // ═══════════════════════════════════════════════════════════════════════

    private static void servirFrontend(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try (InputStream is = App.class.getResourceAsStream("/static/index.html")) {
            if (is == null) {
                String body = "<h1>PropTech corriendo en puerto " + PORT + "</h1>";
                ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
                ex.sendResponseHeaders(200, body.length());
                ex.getResponseBody().write(body.getBytes());
                return;
            }
            byte[] bytes = is.readAllBytes();
            ex.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            ex.getResponseHeaders().set("Cache-Control", "no-cache");
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);
        }
    }

    // ── HEALTH ───────────────────────────────────────────────────────────────

    private static void handleHealth(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        json(ex, 200, "{\"ok\":true,\"status\":\"UP\",\"version\":\"1.0\"}");
    }

    // ── DASHBOARD ────────────────────────────────────────────────────────────

    private static void handleDashboard(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            int totalInm   = sistema.getInmueblesLista().tamanio();
            int disponibles = 0;
            double totalValor = 0;
            for (int i = 0; i < totalInm; i++) {
                Inmueble inm = sistema.getInmueblesLista().obtener(i);
                if (inm.getEstado() == EstadoInmueble.DISPONIBLE) disponibles++;
                totalValor += inm.getPrecio();
            }
            int totalCli   = sistema.getClientesLista().tamanio();
            int totalAse   = sistema.getAsesoresLista().tamanio();
            int totalVis   = sistema.getTodasLasVisitas().tamanio();
            int colaVis    = sistema.getVisitasPendientes().tamanio();
            int totalAl    = sistema.getAlertasRegistro().tamanio();
            int totalOp    = sistema.getOperaciones().tamanio();
            int totalCt    = sistema.getContratos().tamanio();

            String body = "{\"ok\":true,"
                    + "\"totalInmuebles\":" + totalInm + ","
                    + "\"disponibles\":" + disponibles + ","
                    + "\"totalClientes\":" + totalCli + ","
                    + "\"totalAsesores\":" + totalAse + ","
                    + "\"totalVisitas\":" + totalVis + ","
                    + "\"visitasPendientes\":" + colaVis + ","
                    + "\"totalAlertas\":" + totalAl + ","
                    + "\"totalOperaciones\":" + totalOp + ","
                    + "\"totalContratos\":" + totalCt + ","
                    + "\"valorPortafolio\":" + totalValor + "}";
            json(ex, 200, body);
        } catch (Exception e) {
            json(ex, 500, JsonUtil.error(e.getMessage()));
        }
    }

    // ── INMUEBLES ─────────────────────────────────────────────────────────────

    private static void handleInmuebles(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            String path = ex.getRequestURI().getPath();

            if ("GET".equals(method)) {
                json(ex, 200, JsonUtil.ok(JsonUtil.listaInmuebles(sistema.getInmueblesLista())));

            } else if ("PUT".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                if (path.contains("/estado")) {
                    sistema.cambiarEstadoInmueble(d.get("codigo"),
                            EstadoInmueble.valueOf(d.get("estado").toUpperCase()));
                    json(ex, 200, JsonUtil.msg("Estado actualizado"));
                } else if (path.contains("/precio")) {
                    /* csv save estado */ sistema.actualizarPrecioInmueble(d.get("codigo"),
                            Double.parseDouble(d.get("precio")));
                    json(ex, 200, JsonUtil.msg("Precio actualizado"));
                } else {
                    json(ex, 404, JsonUtil.error("Endpoint no encontrado"));
                }

            } else if ("POST".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                String asesorId = d.getOrDefault("asesorId", "");
                Asesor asesor = asesorId.isBlank() ? null : sistema.buscarAsesor(asesorId);
                TipoInmueble tipo = TipoInmueble.valueOf(
                        d.getOrDefault("tipo", "APARTAMENTO").toUpperCase().replace(" ",""));
                EstadoInmueble est = EstadoInmueble.valueOf(
                        d.getOrDefault("estado", "DISPONIBLE").toUpperCase());
                Inmueble inm = new Inmueble(
                        d.get("codigo"), d.get("direccion"), d.get("ciudad"),
                        d.get("zona"), tipo, d.getOrDefault("finalidad","VENTA"),
                        parseDouble(d.getOrDefault("precio","0")),
                        parseDouble(d.getOrDefault("area","0")),
                        parseInt(d.getOrDefault("habitaciones","0")),
                        parseInt(d.getOrDefault("banios","0")),
                        est, asesor);
                sistema.registrarInmueble(inm);
                if (asesor != null) sistema.asignarInmuebleAsesor(asesorId, inm.getCodigo());
                json(ex, 201, JsonUtil.ok(JsonUtil.inmueble(inm)));

            } else if ("DELETE".equals(method)) {
                String codigo = path.replace("/api/inmuebles/","").replace("/api/inmuebles","").trim();
                if (codigo.isBlank()) {
                    Map<String,String> d = parseJson(readBody(ex));
                    codigo = d.getOrDefault("codigo","");
                }
                sistema.eliminarInmueble(codigo);
                json(ex, 200, JsonUtil.msg("Inmueble " + codigo + " eliminado"));

            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── CLIENTES ──────────────────────────────────────────────────────────────

    private static void handleClientes(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            String path = ex.getRequestURI().getPath();
            if ("GET".equals(method)) {
                json(ex, 200, JsonUtil.ok(JsonUtil.listaClientes(sistema.getClientesLista())));

            } else if ("POST".equals(method)) {
                if (path.endsWith("/eliminar")) {
                    Map<String,String> d = parseJson(readBody(ex));
                    sistema.eliminarCliente(d.get("id"));
                    json(ex, 200, JsonUtil.msg("Cliente eliminado"));
                } else {
                    Map<String,String> d = parseJson(readBody(ex));
                    String tipoStr = d.getOrDefault("tipo","COMPRADOR").toUpperCase();
                    Cliente.TipoCliente tipoCli = Cliente.TipoCliente.valueOf(tipoStr);
                    TipoInmueble tipoDeseado = null;
                    try {
                        String td = d.getOrDefault("tipoDeseado","");
                        if (!td.isBlank()) tipoDeseado = TipoInmueble.valueOf(td.toUpperCase());
                    } catch (Exception ignored) {}
                    Cliente c = new Cliente(
                            d.get("id"), d.get("nombre"), d.get("correo"),
                            d.getOrDefault("telefono",""), d.getOrDefault("contrasenia","cli"+System.currentTimeMillis()),
                            tipoCli,
                            parseDouble(d.getOrDefault("presupuesto","0")),
                            d.getOrDefault("zonasInteres",""),
                            tipoDeseado,
                            parseInt(d.getOrDefault("minHabitaciones","0")));
                    sistema.registrarCliente(c);
                csv.guardarClientes(sistema);
                    json(ex, 201, JsonUtil.ok(JsonUtil.cliente(c)));
                }

            } else if ("DELETE".equals(method)) {
                String id = path.replace("/api/clientes/","").replace("/api/clientes","").trim();
                sistema.eliminarCliente(id);
                json(ex, 200, JsonUtil.msg("Cliente " + id + " eliminado"));

            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── ASESORES ──────────────────────────────────────────────────────────────

    private static void handleAsesores(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            String path = ex.getRequestURI().getPath();
            if ("GET".equals(method)) {
                json(ex, 200, JsonUtil.ok(JsonUtil.listaAsesores(sistema.getAsesoresLista())));
            } else if ("POST".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                if (path.endsWith("/asignar")) {
                    sistema.asignarInmuebleAsesor(d.get("asesorId"), d.get("inmuebleCodigo"));
                    json(ex, 200, JsonUtil.msg("Inmueble asignado al asesor"));
                } else {
                    Asesor a = new Asesor(
                            d.get("id"), d.get("nombre"), d.get("correo"),
                            d.getOrDefault("telefono",""), d.getOrDefault("zonaAsignada",""),
                            d.getOrDefault("contrasenia","ase"+System.currentTimeMillis()));
                    sistema.registrarAsesor(a);
                csv.guardarAsesores(sistema);
                    json(ex, 201, JsonUtil.ok(JsonUtil.asesor(a)));
                }
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── VISITAS ───────────────────────────────────────────────────────────────

    private static void handleVisitas(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            String path = ex.getRequestURI().getPath();
            if ("GET".equals(method)) {
                // Serializar cola sin modificarla
                ListaSimple<Visita> colaLista = new ListaSimple<>();
                List<Visita> colaItems = sistema.getVisitasPendientes().peekN(50);
                for (Visita v : colaItems) colaLista.agregarFinal(v);

                String cola  = JsonUtil.listaVisitas(colaLista);
                String todas = JsonUtil.listaVisitas(sistema.getTodasLasVisitas());
                json(ex, 200, "{\"ok\":true,\"cola\":" + cola + ",\"todas\":" + todas + "}");

            } else if ("POST".equals(method)) {
                if (path.endsWith("/atender")) {
                    Visita v = sistema.atenderVisita();
                    if (v == null) json(ex, 200, JsonUtil.msg("Cola vacía"));
                    else json(ex, 200, JsonUtil.ok(JsonUtil.visita(v)));
                } else {
                    Map<String,String> d = parseJson(readBody(ex));
                    Cliente  c   = sistema.buscarCliente(d.get("clienteId"));
                    Inmueble inm = sistema.buscarInmueble(d.get("inmuebleCodigo"));
                    Asesor   a   = sistema.buscarAsesor(d.get("asesorId"));
                    if (c == null)   { json(ex, 400, JsonUtil.error("Cliente no encontrado: " + d.get("clienteId"))); return; }
                    if (inm == null) { json(ex, 400, JsonUtil.error("Inmueble no encontrado: " + d.get("inmuebleCodigo"))); return; }
                    LocalDateTime fecha = parseFecha(d.getOrDefault("fechayHora",""));
                    EstadoVisita est = EstadoVisita.PENDIENTE;
                    try { est = EstadoVisita.valueOf(d.getOrDefault("estado","PENDIENTE").toUpperCase()); }
                    catch (Exception ignored) {}
                    Visita v = new Visita(c, inm, a, fecha, est, d.getOrDefault("observaciones",""));
                    sistema.agendarVisita(v);
                csv.guardarVisitas(sistema);
                    json(ex, 201, JsonUtil.ok(JsonUtil.visita(v)));
                }
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── OPERACIONES ───────────────────────────────────────────────────────────

    private static void handleOperaciones(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            if ("GET".equals(method)) {
                json(ex, 200, JsonUtil.ok(JsonUtil.listaOperaciones(sistema.getOperaciones())));
            } else if ("POST".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                Inmueble inm = sistema.buscarInmueble(d.get("inmuebleCodigo"));
                Cliente  c   = sistema.buscarCliente(d.get("clienteId"));
                Asesor   a   = sistema.buscarAsesor(d.get("asesorId"));
                TipoOperacion tipo = TipoOperacion.valueOf(
                        d.getOrDefault("tipo","ARRIENDO").toUpperCase());
                EstadoOperacion est = EstadoOperacion.ACTIVA;
                try { est = EstadoOperacion.valueOf(d.getOrDefault("estado","ACTIVA").toUpperCase()); }
                catch (Exception ignored) {}
                String id = "OP-" + String.format("%03d", sistema.getOperaciones().tamanio() + 1);
                Operacion op = new Operacion(id, inm, c, a, tipo,
                        LocalDate.now(), parseDouble(d.getOrDefault("valor","0")),
                        parseDouble(d.getOrDefault("comision","3")), est);
                sistema.registrarOperacion(op);
                csv.guardarOperaciones(sistema);
                json(ex, 201, JsonUtil.ok(JsonUtil.operacion(op)));
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── CONTRATOS ─────────────────────────────────────────────────────────────

    private static void handleContratos(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            if ("GET".equals(method)) {
                json(ex, 200, JsonUtil.ok(JsonUtil.listaContratos(sistema.getContratos())));
            } else if ("POST".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                Inmueble inm = sistema.buscarInmueble(d.get("inmuebleCodigo"));
                Cliente  c   = sistema.buscarCliente(d.get("clienteId"));
                Contrato.TipoContrato tipo = Contrato.TipoContrato.ARRIENDO;
                try { tipo = Contrato.TipoContrato.valueOf(d.getOrDefault("tipo","ARRIENDO").toUpperCase()); }
                catch (Exception ignored) {}
                String id = "CT-" + String.format("%03d", sistema.getContratos().tamanio() + 1);
                LocalDate fi = LocalDate.now();
                LocalDate ff = fi.plusMonths(12);
                Contrato ct = new Contrato(id, inm, c, tipo, fi, ff,
                        parseDouble(d.getOrDefault("valorMensual","0")));
                sistema.registrarContrato(ct);
                csv.guardarContratos(sistema);
                json(ex, 201, JsonUtil.ok(JsonUtil.contrato(ct)));
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── ALERTAS ───────────────────────────────────────────────────────────────

    private static void handleAlertas(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            String path = ex.getRequestURI().getPath();
            if ("GET".equals(method)) {
                Alerta top = sistema.getAlertas().verMaximo();
                String topJson = top != null ? JsonUtil.alerta(top) : "null";
                String todas  = JsonUtil.listaAlertas(sistema.getAlertasRegistro());
                json(ex, 200, "{\"ok\":true,\"prioritaria\":" + topJson + ",\"registro\":" + todas + "}");

            } else if ("POST".equals(method)) {
                if (path.endsWith("/detectar")) {
                    sistema.detectarComportamientoInusual();
                    json(ex, 200, JsonUtil.msg("Detección completada"));
                } else if (path.endsWith("/resolver")) {
                    Alerta a = sistema.obtenerAlertaPrioritaria();
                    if (a == null) json(ex, 200, JsonUtil.msg("Sin alertas pendientes"));
                    else {
                        a.setResuelta(true);
                        json(ex, 200, JsonUtil.ok(JsonUtil.alerta(a)));
                    }
                } else {
                    Map<String,String> d = parseJson(readBody(ex));
                    Alerta.NivelAlerta nivel = Alerta.NivelAlerta.MEDIA;
                    try { nivel = Alerta.NivelAlerta.valueOf(d.getOrDefault("nivel","MEDIA").toUpperCase()); }
                    catch (Exception ignored) {}
                    String id = "ALT-" + System.currentTimeMillis();
                    Alerta a = new Alerta(id, d.getOrDefault("mensaje","Sin descripción"), nivel,
                            d.getOrDefault("tipo","MANUAL"));
                    sistema.generarAlerta(a);
                    json(ex, 201, JsonUtil.ok(JsonUtil.alerta(a)));
                }
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── RECOMENDACIONES ───────────────────────────────────────────────────────

    private static void handleRecomendaciones(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            String clienteId = q.getOrDefault("clienteId","");
            Cliente c = sistema.buscarCliente(clienteId);
            if (c == null) {
                // Sin cliente específico → devolver primeros 5 disponibles
                ListaSimple<Inmueble> disp = new ListaSimple<>();
                for (int i = 0; i < sistema.getInmueblesLista().tamanio() && disp.tamanio() < 5; i++) {
                    Inmueble inm = sistema.getInmueblesLista().obtener(i);
                    if (inm.getEstado() == EstadoInmueble.DISPONIBLE) disp.agregarFinal(inm);
                }
                json(ex, 200, JsonUtil.ok(JsonUtil.listaInmuebles(disp)));
                return;
            }
            ListaSimple<Inmueble> rec = sistema.recomendarInmuebles(c);
            json(ex, 200, JsonUtil.ok(JsonUtil.listaInmuebles(rec)));
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── COMPORTAMIENTO ────────────────────────────────────────────────────────

    private static void handleComportamiento(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            List<Map<String,Object>> hallazgos = new ArrayList<>();
            // Inmuebles con muchas visitas sin cierre
            for (int i = 0; i < sistema.getInmueblesLista().tamanio(); i++) {
                Inmueble inm = sistema.getInmueblesLista().obtener(i);
                if (inm.getVisitas() >= 3 && inm.getEstado() == EstadoInmueble.DISPONIBLE) {
                    Map<String,Object> h = new LinkedHashMap<>();
                    h.put("tipo", "VISITAS_SIN_CIERRE");
                    h.put("entidad", inm.getCodigo());
                    h.put("descripcion", inm.getCodigo() + " tiene " + inm.getVisitas() + " visitas sin cierre");
                    h.put("nivel", "ALTA");
                    hallazgos.add(h);
                }
            }
            // Asesores con sobrecarga
            for (int i = 0; i < sistema.getAsesoresLista().tamanio(); i++) {
                Asesor a = sistema.getAsesoresLista().obtener(i);
                if (a.getVisitasAtendidas().tamanio() >= 5) {
                    Map<String,Object> h = new LinkedHashMap<>();
                    h.put("tipo","SOBRECARGA_ASESOR");
                    h.put("entidad", a.getNombre());
                    h.put("descripcion", a.getNombre() + " tiene " + a.getVisitasAtendidas().tamanio() + " visitas asignadas");
                    h.put("nivel","MEDIA");
                    hallazgos.add(h);
                }
            }
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < hallazgos.size(); i++) {
                if (i > 0) sb.append(",");
                Map<String,Object> h = hallazgos.get(i);
                sb.append("{\"tipo\":\"").append(h.get("tipo")).append("\","
                        + "\"entidad\":\"").append(h.get("entidad")).append("\","
                        + "\"descripcion\":\"").append(h.get("descripcion")).append("\","
                        + "\"nivel\":\"").append(h.get("nivel")).append("\"}");
            }
            sb.append("]");
            json(ex, 200, "{\"ok\":true,\"hallazgos\":" + sb + "}");
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── RANKING ZONAS ─────────────────────────────────────────────────────────

    private static void handleRankingZonas(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,Integer> ranking = sistema.rankingZonas();
            StringBuilder sb = new StringBuilder("[");
            boolean first = true;
            for (Map.Entry<String,Integer> e : ranking.entrySet()) {
                if (!first) sb.append(",");
                sb.append("{\"zona\":\"").append(JsonUtil.esc(e.getKey()))
                  .append("\",\"cantidad\":").append(e.getValue()).append("}");
                first = false;
            }
            sb.append("]");
            json(ex, 200, "{\"ok\":true,\"ranking\":" + sb + "}");
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── RANKING ASESORES ──────────────────────────────────────────────────────

    private static void handleRankingAsesores(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            ListaSimple<Asesor> ranking = sistema.rankingAsesores();
            json(ex, 200, "{\"ok\":true,\"ranking\":" + JsonUtil.listaAsesores(ranking) + "}");
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── HISTORIAL (PILA) ──────────────────────────────────────────────────────

    private static void handleHistorial(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            if ("GET".equals(method)) {
                ListaSimple<Accion<?>> hist = sistema.getHistorialAcciones().getLista();
                json(ex, 200, "{\"ok\":true,\"historial\":" + JsonUtil.listaAcciones(hist) + "}");
            } else if ("POST".equals(method)) {
                String resultado = sistema.deshacerUltimaAccion();
                json(ex, 200, JsonUtil.msg(resultado));
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── GRAFO ─────────────────────────────────────────────────────────────────

    private static void handleGrafo(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            com.edu.uniquindio.proptech.estructuras.Grafo g = sistema.getGrafoZonas();
            StringBuilder sb = new StringBuilder("{\"ok\":true,\"nodos\":[");
            boolean first = true;
            for (String nodo : g.getNodos()) {
                if (!first) sb.append(",");
                sb.append("\"").append(JsonUtil.esc(nodo)).append("\"");
                first = false;
            }
            sb.append("],\"aristas\":[");
            first = true;
            for (String nodo : g.getNodos()) {
                for (com.edu.uniquindio.proptech.estructuras.Grafo.Arista a : g.getVecinos(nodo)) {
                    if (nodo.compareTo(a.destino) < 0) {
                        if (!first) sb.append(",");
                        sb.append("{\"origen\":\"").append(JsonUtil.esc(nodo))
                          .append("\",\"destino\":\"").append(JsonUtil.esc(a.destino))
                          .append("\",\"peso\":").append(a.peso).append("}");
                        first = false;
                    }
                }
            }
            sb.append("]}");
            json(ex, 200, sb.toString());
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── BST ───────────────────────────────────────────────────────────────────

    private static void handleBst(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            List<Double> inorden = sistema.getBstPrecios().inorden();
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < inorden.size(); i++) {
                if (i > 0) sb.append(",");
                sb.append(inorden.get(i));
            }
            sb.append("]");
            String treeJson = buildTreeJson(sistema.getBstPrecios().getRaiz());
            json(ex, 200, "{\"ok\":true,\"inorden\":" + sb + ",\"tree\":" + treeJson
                    + ",\"altura\":" + sistema.getBstPrecios().altura()
                    + ",\"tamanio\":" + sistema.getBstPrecios().tamanio() + "}");
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    private static String buildTreeJson(com.edu.uniquindio.proptech.estructuras.Arbol.NodoArbol<Double> nodo) {
        if (nodo == null) return "null";
        return "{\"v\":" + nodo.valor
                + ",\"l\":" + buildTreeJson(nodo.izquierdo)
                + ",\"r\":" + buildTreeJson(nodo.derecho) + "}";
    }

    // ── SESIÓN (LOGIN/LOGOUT) ─────────────────────────────────────────────────
    /**
     * POST /api/sesion/login
     * Body: { "correo": "...", "contrasenia": "..." }
     * Respuesta: { "ok": true, "id": "...", "nombre": "...", "rol": "admin|cliente|asesor",
     *              "clienteId": "..." (si rol=cliente), "asesorId": "..." (si rol=asesor) }
     *
     * ¡CORRECCIÓN CRÍTICA! El JSON de respuesta ahora incluye el campo "rol"
     * para que el frontend muestre la vista correcta según el tipo de usuario.
     */
    private static void handleLogin(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            String correo = d.getOrDefault("correo", "").trim().toLowerCase();
            String pass   = d.getOrDefault("contrasenia", "");

            Usuario u = serviciosUsuario.iniciarSesion(correo, pass);
            Sesion.getInstancia().setUsuario(u);

            String rol = u.getRol(); // "admin" | "cliente" | "asesor"
            String clienteId = (u instanceof Cliente) ? u.getId() : "";
            String asesorId  = (u instanceof Asesor)  ? u.getId() : "";

            String body = "{\"ok\":true,"
                    + "\"id\":\"" + JsonUtil.esc(u.getId()) + "\","
                    + "\"nombre\":\"" + JsonUtil.esc(u.getNombre()) + "\","
                    + "\"correo\":\"" + JsonUtil.esc(u.getCorreo()) + "\","
                    + "\"rol\":\"" + rol + "\","
                    + "\"clienteId\":\"" + JsonUtil.esc(clienteId) + "\","
                    + "\"asesorId\":\"" + JsonUtil.esc(asesorId) + "\"}";
            json(ex, 200, body);

        } catch (ElementoNoEncontradoException | ParametroVacioException e) {
            json(ex, 401, JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            json(ex, 500, JsonUtil.error("Error interno: " + e.getMessage()));
        }
    }

    private static void handleLogout(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        Sesion.getInstancia().cerrarSesion();
        json(ex, 200, JsonUtil.msg("Sesión cerrada"));
    }

    // ── FILTRAR ───────────────────────────────────────────────────────────────

    private static void handleFiltrar(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            TipoInmueble tipo = null;
            if (q.containsKey("tipo") && !q.get("tipo").isBlank()) {
                try { tipo = TipoInmueble.valueOf(q.get("tipo").toUpperCase()); }
                catch (Exception ignored) {}
            }
            String finalidad  = q.getOrDefault("finalidad","");
            double precioMin  = parseDoubleOr0(q.getOrDefault("precioMin",""));
            double precioMax  = parseDoubleOr0(q.getOrDefault("precioMax",""));
            int    minHab     = parseIntOr0(q.getOrDefault("minHab",""));

            ListaSimple<Inmueble> res = sistema.filtrarInmuebles(tipo, finalidad, precioMin, precioMax, minHab);
            json(ex, 200, JsonUtil.ok(JsonUtil.listaInmuebles(res)));
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── FAVORITOS ─────────────────────────────────────────────────────────────

    private static void handleFavoritos(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            String clienteId    = q.getOrDefault("clienteId","");
            String inmuebleCod  = q.getOrDefault("inmCodigo","");
            if (clienteId.isBlank() || inmuebleCod.isBlank()) {
                json(ex, 400, JsonUtil.error("clienteId e inmCodigo requeridos")); return;
            }
            sistema.toggleFavorito(clienteId, inmuebleCod);
            json(ex, 200, JsonUtil.msg("Favorito actualizado"));
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── PERFIL ────────────────────────────────────────────────────────────────

    private static void handlePerfil(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            Sesion sesion = Sesion.getInstancia();
            if ("GET".equals(method)) {
                if (!sesion.haySesionActiva()) { json(ex, 401, JsonUtil.error("Sin sesión activa")); return; }
                Usuario u = sesion.getUsuario();
                String body = "{\"ok\":true,"
                        + "\"id\":\"" + JsonUtil.esc(u.getId()) + "\","
                        + "\"nombre\":\"" + JsonUtil.esc(u.getNombre()) + "\","
                        + "\"correo\":\"" + JsonUtil.esc(u.getCorreo()) + "\","
                        + "\"rol\":\"" + u.getRol() + "\","
                        + "\"telefono\":\"" + JsonUtil.esc(u.getTelefono() != null ? u.getTelefono() : "") + "\","
                        + "\"fotoPerfil\":\"" + JsonUtil.esc(u.getFotoPerfil() != null ? u.getFotoPerfil() : "") + "\"}";
                json(ex, 200, body);
            } else if ("POST".equals(method)) {
                if (!sesion.haySesionActiva()) { json(ex, 401, JsonUtil.error("Sin sesión activa")); return; }
                Map<String,String> d = parseJson(readBody(ex));
                Usuario u = sesion.getUsuario();
                if (d.containsKey("nombre") && !d.get("nombre").isBlank()) u.setNombre(d.get("nombre"));
                if (d.containsKey("telefono")) u.setTelefono(d.get("telefono"));
                if (d.containsKey("nuevaContrasenia") && !d.get("nuevaContrasenia").isBlank())
                    u.setContrasenia(d.get("nuevaContrasenia"));
                json(ex, 200, JsonUtil.msg("Perfil actualizado"));
            } else {
                json(ex, 405, JsonUtil.error("Método no permitido"));
            }
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── PORTAL CLIENTE ────────────────────────────────────────────────────────

    private static void handleClientePortal(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            String clienteId = q.getOrDefault("clienteId","");
            Cliente c = sistema.buscarCliente(clienteId);
            if (c == null) { json(ex, 404, JsonUtil.error("Cliente no encontrado: " + clienteId)); return; }

            String favJson = JsonUtil.listaInmuebles(c.getFavoritos());
            String visJson = JsonUtil.listaVisitas(c.getHistorialVisitas());
            ListaSimple<Inmueble> recs = sistema.recomendarInmuebles(c);
            String recJson = JsonUtil.listaInmuebles(recs);

            String body = "{\"ok\":true,\"cliente\":" + JsonUtil.cliente(c)
                    + ",\"favoritos\":" + favJson
                    + ",\"historialVisitas\":" + visJson
                    + ",\"recomendaciones\":" + recJson + "}";
            json(ex, 200, body);
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }

    // ── PORTAL ASESOR ─────────────────────────────────────────────────────────

    private static void handleAsesorPortal(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            String asesorId = q.getOrDefault("asesorId","");
            Asesor a = sistema.buscarAsesor(asesorId);
            if (a == null) { json(ex, 404, JsonUtil.error("Asesor no encontrado: " + asesorId)); return; }

            String inmsJson = JsonUtil.listaInmuebles(a.getInmueblesAsignados());
            String visJson  = JsonUtil.listaVisitas(a.getVisitasAtendidas());

            // Operaciones del asesor
            ListaSimple<Operacion> opsAsesor = new ListaSimple<>();
            for (int i = 0; i < sistema.getOperaciones().tamanio(); i++) {
                Operacion op = sistema.getOperaciones().obtener(i);
                if (op.getAsesor() != null && op.getAsesor().getId().equals(asesorId))
                    opsAsesor.agregarFinal(op);
            }

            String body = "{\"ok\":true,\"asesor\":" + JsonUtil.asesor(a)
                    + ",\"inmuebles\":" + inmsJson
                    + ",\"visitas\":" + visJson
                    + ",\"operaciones\":" + JsonUtil.listaOperaciones(opsAsesor) + "}";
            json(ex, 200, body);
        } catch (Exception e) {
            json(ex, 400, JsonUtil.error(e.getMessage()));
        }
    }
    // ── INTENCIONES ───────────────────────────────────────────────────────────

    private static void handleIntenciones(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if ("OPTIONS".equals(method)) { options(ex); return; }
        try {
            Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
            if ("GET".equals(method)) {
                String clienteId = q.getOrDefault("clienteId","");
                ListaSimple<Intencion> lista = clienteId.isBlank()
                        ? sistema.getIntenciones()
                        : sistema.getIntencionesPorCliente(clienteId);
                json(ex, 200, JsonUtil.ok(JsonUtil.listaIntenciones(lista)));
            } else if ("POST".equals(method)) {
                Map<String,String> d = parseJson(readBody(ex));
                Cliente c    = sistema.buscarCliente(d.get("clienteId"));
                Inmueble inm = sistema.buscarInmueble(d.get("inmuebleCodigo"));
                if (c   == null) { json(ex, 400, JsonUtil.error("Cliente no encontrado")); return; }
                if (inm == null) { json(ex, 400, JsonUtil.error("Inmueble no encontrado")); return; }
                Intencion.TipoIntencion tipo = Intencion.TipoIntencion.COMPRA;
                try { tipo = Intencion.TipoIntencion.valueOf(d.getOrDefault("tipo","COMPRA").toUpperCase()); }
                catch (Exception ignored) {}
                String id = "INT-" + String.format("%03d", sistema.getIntenciones().tamanio() + 1);
                Intencion intent = new Intencion(id, c, inm, tipo,
                        parseDouble(d.getOrDefault("oferta","0")),
                        d.getOrDefault("observaciones",""));
                sistema.registrarIntencion(intent);
                csv.guardarIntenciones(sistema);
                json(ex, 201, JsonUtil.ok(JsonUtil.intencion(intent)));
            } else { json(ex, 405, JsonUtil.error("Método no permitido")); }
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    private static void handleRankingInmuebles(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            ListaSimple<Inmueble> ranking = sistema.rankingInmueblesPorDemanda();
            json(ex, 200, "{\"ok\":true,\"ranking\":" + JsonUtil.listaInmuebles(ranking) + "}");
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    private static void handleEditarInmueble(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            sistema.editarInmueble(d.get("codigo"), d.getOrDefault("direccion",""),
                    d.getOrDefault("zona",""), parseDouble(d.getOrDefault("precio","0")),
                    parseDouble(d.getOrDefault("area","0")),
                    parseInt(d.getOrDefault("habitaciones","-1")),
                    parseInt(d.getOrDefault("banios","-1")),
                    d.getOrDefault("finalidad",""));
            csv.guardarInmuebles(sistema);
            json(ex, 200, JsonUtil.msg("Inmueble actualizado"));
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    private static void handleEditarCliente(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            sistema.editarCliente(d.get("id"), d.getOrDefault("nombre",""),
                    d.getOrDefault("telefono",""),
                    parseDouble(d.getOrDefault("presupuesto","0")),
                    d.getOrDefault("zonasInteres",""));
            csv.guardarClientes(sistema);
            json(ex, 200, JsonUtil.msg("Cliente actualizado"));
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    // ── CHAT: obtener mensajes de un canal ────────────────────────────────────
    // ── IA PROXY — Anthropic + fallback IA local ──────────────────────────
    private static void handleIA(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        if (!"POST".equals(ex.getRequestMethod()))   { json(ex, 405, JsonUtil.error("Solo POST")); return; }
        Map<String,String> body2 = null;
        try {
            body2 = parseJson(readBody(ex));
            String userMsg  = body2.getOrDefault("userMessage", "");
            String sysMsg   = body2.getOrDefault("system", "");
            String histJson = body2.getOrDefault("history", "[]");

            // Leer API key (Anthropic)
            String apiKey = System.getenv("ANTHROPIC_API_KEY");
            if (apiKey == null || apiKey.isBlank()) apiKey = System.getProperty("ANTHROPIC_API_KEY", "");
            if (apiKey == null || apiKey.isBlank()) {
                java.nio.file.Path cfg = java.nio.file.Paths.get("config.properties");
                if (java.nio.file.Files.exists(cfg)) {
                    for (String line : java.nio.file.Files.readAllLines(cfg)) {
                        if (line.startsWith("ANTHROPIC_API_KEY=")) {
                            apiKey = line.substring("ANTHROPIC_API_KEY=".length()).trim(); break;
                        }
                    }
                }
            }

            if (apiKey == null || apiKey.isBlank()) {
                // Sin key: usar IA local basada en reglas
                json(ex, 200, "{\"ok\":true,\"text\":\"" + escJ(iaLocal(userMsg)) + "\"}" );
                return;
            }

            // Llamar a Anthropic Claude
            StringBuilder msgs = new StringBuilder("[");
            java.util.regex.Matcher hm = java.util.regex.Pattern.compile(
                "\"role\"\\s*:\\s*\"(user|assistant)\"[^}]*?\"content\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"")
                .matcher(histJson);
            boolean fh = true;
            while (hm.find()) {
                if (!fh) msgs.append(","); fh=false;
                msgs.append("{\"role\":\"").append(hm.group(1))
                    .append("\",\"content\":\"").append(hm.group(2)).append("\"}");
            }
            if (!fh) msgs.append(",");
            msgs.append("{\"role\":\"user\",\"content\":\"").append(escJ(userMsg)).append("\"}]");

            String reqBody = "{\"model\":\"claude-haiku-4-5-20251001\",\"max_tokens\":1024,\"system\":\""
                           + escJ(sysMsg) + "\",\"messages\":" + msgs + "}";

            java.net.URL url = new java.net.URL("https://api.anthropic.com/v1/messages");
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15_000);
            conn.setReadTimeout(30_000);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("x-api-key", apiKey);
            conn.setRequestProperty("anthropic-version", "2023-06-01");
            try (OutputStream oss = conn.getOutputStream()) {
                oss.write(reqBody.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            int status = conn.getResponseCode();
            InputStream is2 = status < 400 ? conn.getInputStream() : conn.getErrorStream();
            String resp = new String(is2.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            conn.disconnect();

            if (status == 200) {
                java.util.regex.Matcher tm = java.util.regex.Pattern.compile(
                    "\"text\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").matcher(resp);
                String text = tm.find() ? tm.group(1) : "";
                text = text.replace("\\n", "\n").replace("\\\"", "\"");
                json(ex, 200, "{\"ok\":true,\"text\":\"" + escJ(text) + "\"}" );
            } else {
                // Fallback a IA local si la API falla
                json(ex, 200, "{\"ok\":true,\"text\":\"" + escJ(iaLocal(userMsg)) + "\"}" );
            }
        } catch (Exception e) {
            String um = body2 != null ? body2.getOrDefault("userMessage","") : "";
            json(ex, 200, "{\"ok\":true,\"text\":\"" + escJ(iaLocal(um)) + "\"}" );
        }
    }

    /** IA local basada en reglas — responde preguntas comunes sin API key */
    private static String iaLocal(String pregunta) {
        String p = pregunta.toLowerCase();
        if (p.contains("visita") || p.contains("agenda") || p.contains("ver el inmueb")) {
            return "Para agendar una visita ve a la seccion Mis Visitas y haz clic en + Agendar Visita. "
                 + "Selecciona el inmueble, fecha, hora y un asesor. El asesor confirmara la visita.";
        } else if (p.contains("precio") || p.contains("costo") || p.contains("valor") || p.contains("cuanto")) {
            return "Manejamos inmuebles desde $80M en lotes hasta $1.800M en casas. "
                 + "Apartamentos de $150M a $450M en venta, arriendos desde $800.000 mensuales. "
                 + "Filtra por precio en el catalogo.";
        } else if (p.contains("arriendo") || p.contains("arrendar") || p.contains("alquil")) {
            return "Para arrendar necesitas: identificacion, certificado de ingresos (3 nominas), "
                 + "referencias personales y comerciales, y codeudor o poliza. "
                 + "Comision: 1 mes de canon + IVA.";
        } else if (p.contains("compra") || p.contains("comprar") || p.contains("venta")) {
            return "Para comprar te acompanamos en: visita, oferta, promesa de compraventa, escritura y registro. "
                 + "Comision del 3% sobre el valor. Registra tu intencion en la seccion Mis Intenciones.";
        } else if (p.contains("asesor") || p.contains("contacto") || p.contains("hablar")) {
            return "Contamos con: Ana Martinez (Norte), Carlos Gomez (Sur) y Lucia Herrera (Centro/Rural). "
                 + "Escribeles por el Chat de Dudas o agenda una visita para que te asignen un asesor.";
        } else if (p.contains("tipo") || p.contains("inmueble") || p.contains("propiedad") || p.contains("tienen")) {
            return "Manejamos 6 tipos: Apartamentos, Casas, Locales Comerciales, Oficinas, Lotes y Bodegas. "
                 + "Disponibles para venta o arriendo en Armenia, Calcara, Montenegro y zonas rurales del Quindio.";
        } else if (p.contains("zona") || p.contains("barrio") || p.contains("sector") || p.contains("ubicac")) {
            return "Trabajamos en: Norte, Sur, Centro y area Rural de Armenia, tambien Calcara y Montenegro. "
                 + "Las zonas con mayor demanda son Norte y Centro segun nuestros reportes.";
        } else if (p.contains("contrato") || p.contains("document")) {
            return "Contratos de arriendo por 1 ano (renovables) con inventario. "
                 + "Para venta: promesa de compraventa y escritura ante notaria. "
                 + "Todo queda registrado en el sistema.";
        } else if (p.contains("comision") || p.contains("honorar")) {
            return "Comisiones: Venta 3% sobre el valor. Arriendo 1 mes de canon + IVA. "
                 + "Renovacion de contrato 50% de un canon mensual.";
        } else if (p.contains("favorito") || p.contains("guardar")) {
            return "Marca inmuebles como favoritos haciendo clic en el corazon de cada tarjeta. "
                 + "Se guardan en tu perfil y puedes verlos desde Mi Panel.";
        } else if (p.contains("hola") || p.contains("buenos") || p.contains("buenas") || p.contains("salud")) {
            return "Hola! Bienvenido a PropTech. Puedo ayudarte con informacion sobre inmuebles disponibles, "
                 + "precios, proceso de compra o arriendo, visitas, contratos y mas. En que te puedo ayudar?";
        } else if (p.contains("gracias")) {
            return "Con gusto! Si tienes mas preguntas sobre inmuebles, visitas o contratos, aqui estoy. "
                 + "Que tengas un excelente dia!";
        } else if (p.contains("recomend") || p.contains("suger")) {
            return "Para ver recomendaciones personalizadas ve a la seccion Recomendaciones. "
                 + "El sistema analiza tu presupuesto, zona de interes y tipo de inmueble para sugerirte las mejores opciones.";
        } else if (p.contains("alerta") || p.contains("notific")) {
            return "El sistema genera alertas automaticas para: contratos por vencer, inmuebles sin visitas, "
                 + "asesores con sobrecarga y clientes sin seguimiento. Puedes verlas en la seccion Alertas.";
        } else {
            return "En PropTech gestionamos inmuebles en Armenia y el Eje Cafetero: apartamentos, casas, locales, "
                 + "oficinas, lotes y bodegas para venta y arriendo. "
                 + "Puedes preguntarme sobre precios, visitas, compra, arriendo, contratos o zonas.";
        }
    }


    private static String escJ(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);
            }
        }
        return sb.toString();
    }


    // ── CHAT: historial de mensajes ─────────────────────────────────────────
    private static void handleChatMensajes(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
        String canal = q.getOrDefault("canal", "");
        if (canal.isBlank()) { json(ex, 400, JsonUtil.error("Falta canal")); return; }
        List<MensajeChat> msgs = chat.getHistorial(canal);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < msgs.size(); i++) { if (i > 0) sb.append(","); sb.append(msgs.get(i).toJson()); }
        sb.append("]");
        json(ex, 200, "{\"ok\":true,\"mensajes\":" + sb + "}");
    }

    // ── CHAT: enviar ──────────────────────────────────────────────────────────
    private static void handleChatEnviar(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            String rid  = d.getOrDefault("remitenteId",     "");
            String rnm  = d.getOrDefault("remitenteNombre", "An\u00f3nimo");
            String rrol = d.getOrDefault("remitenteRol",    "cliente");
            String canal= d.getOrDefault("canal",           "");
            String cont = d.getOrDefault("contenido",       "").trim();
            if (canal.isBlank() || cont.isBlank()) { json(ex, 400, JsonUtil.error("Faltan canal o contenido")); return; }
            MensajeChat msg = chat.enviar(rid, rnm, rrol, canal, cont);
            json(ex, 201, "{\"ok\":true,\"mensaje\":" + msg.toJson() + "}");
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    // ── CHAT: canales activos ─────────────────────────────────────────────────
    private static void handleChatCanales(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        Map<String, List<MensajeChat>> canales = chat.getTodosLosCanales();
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Map.Entry<String, List<MensajeChat>> e : canales.entrySet()) {
            if (!first) sb.append(","); first = false;
            String canal = e.getKey(); List<MensajeChat> lista = e.getValue();
            String nombre = canal;
            com.edu.uniquindio.proptech.modelo.usuario.Cliente c = sistema.buscarCliente(canal);
            if (c != null) nombre = c.getNombre();
            MensajeChat ultimo = lista.isEmpty() ? null : lista.get(lista.size() - 1);
            sb.append("{\"canal\":\"").append(JsonUtil.esc(canal)).append("\",")
              .append("\"clienteNombre\":\"").append(JsonUtil.esc(nombre)).append("\",")
              .append("\"totalMensajes\":").append(lista.size()).append(",")
              .append("\"ultimoMensaje\":").append(ultimo != null ? ultimo.toJson() : "null")
              .append("}");
        }
        sb.append("]");
        json(ex, 200, "{\"ok\":true,\"canales\":" + sb + "}");
    }

    // ── CHAT: SSE tiempo real ─────────────────────────────────────────────────
    private static void handleChatSSE(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        Map<String,String> q = parseQuery(ex.getRequestURI().getQuery());
        String canal = q.getOrDefault("canal", "");
        if (canal.isBlank()) { ex.sendResponseHeaders(400, -1); return; }
        ex.getResponseHeaders().set("Content-Type",  "text/event-stream; charset=utf-8");
        ex.getResponseHeaders().set("Cache-Control", "no-cache");
        ex.getResponseHeaders().set("Connection",    "keep-alive");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(200, 0);
        OutputStream os = ex.getResponseBody();
        chat.suscribir(canal, os);
        try {
            String ping = "data: {\"tipo\":\"connected\",\"canal\":\"" + canal + "\"}\n\n";
            os.write(ping.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            os.flush();
            while (true) {
                Thread.sleep(20_000);
                os.write(": ping\n\n".getBytes(java.nio.charset.StandardCharsets.UTF_8));
                os.flush();
            }
        } catch (InterruptedException | IOException ignored) {
        } finally { chat.desuscribir(canal, os); try { os.close(); } catch (IOException ignored) {} }
    }


    private static void handleFotoInmueble(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            String codigo = d.get("codigo");
            String base64 = d.get("foto");
            if (codigo == null || base64 == null || base64.isBlank()) {
                json(ex, 400, JsonUtil.error("Faltan campos codigo y foto")); return;
            }
            Inmueble inm = sistema.buscarInmueble(codigo);
            if (inm == null) { json(ex, 404, JsonUtil.error("Inmueble no encontrado")); return; }
            String ruta = csv.guardarFoto("inmuebles", codigo, base64);
            inm.setFotoUrl("/" + ruta);
            csv.guardarInmuebles(sistema);
            json(ex, 200, "{\"ok\":true,\"url\":\"/" + ruta + "\"}");
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    // ── FOTO PERFIL ────────────────────────────────────────────────────────────

    private static void handleFotoPerfil(HttpExchange ex) throws IOException {
        if ("OPTIONS".equals(ex.getRequestMethod())) { options(ex); return; }
        try {
            Map<String,String> d = parseJson(readBody(ex));
            String userId = d.get("userId");
            String base64 = d.get("foto");
            if (userId == null || base64 == null || base64.isBlank()) {
                json(ex, 400, JsonUtil.error("Faltan campos userId y foto")); return;
            }
            // Buscar en todos los usuarios
            Usuario u = null;
            u = sistema.buscarCliente(userId);
            if (u == null) u = sistema.buscarAsesor(userId);
            if (u == null) u = serviciosUsuario.buscarUsuario(userId);
            if (u == null) { json(ex, 404, JsonUtil.error("Usuario no encontrado")); return; }
            String ruta = csv.guardarFoto("perfiles", userId, base64);
            u.setFotoPerfil("/" + ruta);
            csv.guardarClientes(sistema);
            csv.guardarAsesores(sistema);
            json(ex, 200, "{\"ok\":true,\"url\":\"/" + ruta + "\"}");
        } catch (Exception e) { json(ex, 400, JsonUtil.error(e.getMessage())); }
    }

    // ── SERVIR FOTOS ESTÁTICAS ────────────────────────────────────────────────

    private static void handleServirFoto(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();        // /fotos/inmuebles/INM-001.jpg
        java.nio.file.Path archivo = java.nio.file.Paths.get(DATA_DIR + path);
        if (!java.nio.file.Files.exists(archivo)) {
            ex.sendResponseHeaders(404, -1); return;
        }
        String mime = path.endsWith(".png") ? "image/png"
                    : path.endsWith(".gif") ? "image/gif" : "image/jpeg";
        byte[] bytes = java.nio.file.Files.readAllBytes(archivo);
        ex.getResponseHeaders().set("Content-Type", mime);
        ex.getResponseHeaders().set("Cache-Control", "max-age=86400");
        ex.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static void json(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        Headers h = ex.getResponseHeaders();
        h.set("Content-Type", "application/json; charset=utf-8");
        h.set("Access-Control-Allow-Origin", "*");
        h.set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        h.set("Access-Control-Allow-Headers", "Content-Type,Authorization");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static void options(HttpExchange ex) throws IOException {
        Headers h = ex.getResponseHeaders();
        h.set("Access-Control-Allow-Origin", "*");
        h.set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        h.set("Access-Control-Allow-Headers", "Content-Type,Authorization");
        ex.sendResponseHeaders(204, -1);
    }

    private static String readBody(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static Map<String,String> parseQuery(String query) {
        Map<String,String> map = new LinkedHashMap<>();
        if (query == null || query.isBlank()) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2)
                map.put(kv[0], java.net.URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
            else
                map.put(kv[0], "");
        }
        return map;
    }

    /**
     * Parser JSON manual para objetos planos { "k":"v", "k2":123 }.
     * No soporta anidamiento (suficiente para las requests de la API).
     */
    private static Map<String,String> parseJson(String json) {
        Map<String,String> map = new LinkedHashMap<>();
        if (json == null || json.isBlank()) return map;
        String s = json.trim();
        if (s.startsWith("{")) s = s.substring(1);
        if (s.endsWith("}"))   s = s.substring(0, s.length() - 1);
        // Split por comas que no estén dentro de comillas
        String[] tokens = s.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        for (String token : tokens) {
            String[] kv = token.split(":", 2);
            if (kv.length == 2) {
                String k = kv[0].trim().replaceAll("^\"|\"$", "");
                String v = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(k, "null".equals(v) ? "" : v);
            }
        }
        return map;
    }

    // ── Conversiones seguras ──────────────────────────────────────────────────

    private static double parseDouble(String s) {
        try { return Double.parseDouble(s.replace(",",".")); }
        catch (Exception e) { return 0; }
    }

    private static int parseInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }

    private static double parseDoubleOr0(String s) {
        if (s == null || s.isBlank()) return 0;
        return parseDouble(s);
    }

    private static int parseIntOr0(String s) {
        if (s == null || s.isBlank()) return 0;
        return parseInt(s);
    }

    private static LocalDateTime parseFecha(String s) {
        if (s == null || s.isBlank()) return LocalDateTime.now().plusDays(1);
        try { return LocalDateTime.parse(s.replace(" ","T")); }
        catch (Exception e) { return LocalDateTime.now().plusDays(1); }
    }
}
