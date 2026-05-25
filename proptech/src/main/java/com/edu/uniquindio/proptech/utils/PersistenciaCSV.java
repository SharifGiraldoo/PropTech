package com.edu.uniquindio.proptech.utils;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.inmueble.*;
import com.edu.uniquindio.proptech.modelo.operaciones.*;
import com.edu.uniquindio.proptech.modelo.operaciones.EstadoVisita;
import com.edu.uniquindio.proptech.modelo.operaciones.TipoOperacion;
import com.edu.uniquindio.proptech.modelo.operaciones.EstadoOperacion;
import com.edu.uniquindio.proptech.modelo.usuario.*;
import com.edu.uniquindio.proptech.servicios.modulos.SistemaInmobiliario;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia CSV para todos los datos del sistema PropTech.
 * Guarda y carga: inmuebles, clientes, asesores, visitas, operaciones,
 * contratos, intenciones y fotos de perfil.
 *
 * Formato CSV con '|' como separador para evitar conflictos con comas
 * en direcciones y nombres.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class PersistenciaCSV {

    private static final String SEP    = "|";
    private static final String SEP_RX = "\\|";
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter D  = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Path dir;

    public PersistenciaCSV(String directorioData) {
        this.dir = Paths.get(directorioData);
        try { Files.createDirectories(dir); } catch (IOException ignored) {}
    }

    // ═══════════════════════════════════════════════════════════════════
    // GUARDAR
    // ═══════════════════════════════════════════════════════════════════

    public void guardarTodo(SistemaInmobiliario s) {
        guardarInmuebles(s);
        guardarClientes(s);
        guardarAsesores(s);
        guardarVisitas(s);
        guardarOperaciones(s);
        guardarContratos(s);
        guardarIntenciones(s);
    }

    public void guardarInmuebles(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"codigo","direccion","ciudad","zona","tipo","finalidad",
                "precio","area","habitaciones","banios","estado","asesorId","visitas","fotoUrl"});
        for (int i = 0; i < s.getInmueblesLista().tamanio(); i++) {
            Inmueble inm = s.getInmueblesLista().obtener(i);
            rows.add(new String[]{
                inm.getCodigo(), esc(inm.getDireccion()), esc(inm.getCiudad()),
                esc(inm.getZona()), inm.getTipo().toString(), inm.getFinalidad(),
                String.valueOf(inm.getPrecio()), String.valueOf(inm.getArea()),
                String.valueOf(inm.getHabitaciones()), String.valueOf(inm.getBanios()),
                inm.getEstado().toString(),
                inm.getAsesor() != null ? inm.getAsesor().getId() : "",
                String.valueOf(inm.getVisitas()),
                inm.getFotoUrl() != null ? inm.getFotoUrl() : ""
            });
        }
        escribir("inmuebles.csv", rows);
    }

    public void guardarClientes(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id","nombre","correo","telefono","contrasenia","tipoCliente",
                "presupuesto","zonasInteres","tipoDeseado","minHabitaciones","estadoBusqueda","fotoPerfil"});
        for (int i = 0; i < s.getClientesLista().tamanio(); i++) {
            Cliente c = s.getClientesLista().obtener(i);
            rows.add(new String[]{
                c.getId(), esc(c.getNombre()), c.getCorreo(), c.getTelefono() != null ? c.getTelefono() : "",
                c.getContrasenia(), c.getTipoCliente().toString(),
                String.valueOf(c.getPresupuesto()), esc(c.getZonasInteres() != null ? c.getZonasInteres() : ""),
                c.getTipoDeseado() != null ? c.getTipoDeseado().toString() : "",
                String.valueOf(c.getMinHabitaciones()), c.getEstadoBusqueda().toString(),
                c.getFotoPerfil() != null ? c.getFotoPerfil() : ""
            });
        }
        escribir("clientes.csv", rows);
    }

    public void guardarAsesores(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id","nombre","correo","telefono","contrasenia","zonaAsignada","cierres","fotoPerfil"});
        for (int i = 0; i < s.getAsesoresLista().tamanio(); i++) {
            Asesor a = s.getAsesoresLista().obtener(i);
            rows.add(new String[]{
                a.getId(), esc(a.getNombre()), a.getCorreo(),
                a.getTelefono() != null ? a.getTelefono() : "", a.getContrasenia(),
                esc(a.getZonaAsignada()), String.valueOf(a.getCierresRealizados()),
                a.getFotoPerfil() != null ? a.getFotoPerfil() : ""
            });
        }
        escribir("asesores.csv", rows);
    }

    public void guardarVisitas(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"clienteId","inmuebleCodigo","asesorId","fechayHora","estado","observaciones"});
        ListaSimple<Visita> todas = s.getTodasLasVisitas();
        for (int i = 0; i < todas.tamanio(); i++) {
            Visita v = todas.obtener(i);
            rows.add(new String[]{
                v.getCliente() != null ? v.getCliente().getId() : "",
                v.getInmueble() != null ? v.getInmueble().getCodigo() : "",
                v.getAsesor()   != null ? v.getAsesor().getId()   : "",
                v.getFechayHora() != null ? v.getFechayHora().format(DT) : "",
                v.getEstado().toString(),
                esc(v.getObservaciones() != null ? v.getObservaciones() : "")
            });
        }
        escribir("visitas.csv", rows);
    }

    public void guardarOperaciones(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id","tipo","inmuebleCodigo","clienteId","asesorId","valor","comision","estado","fecha"});
        for (int i = 0; i < s.getOperaciones().tamanio(); i++) {
            Operacion o = s.getOperaciones().obtener(i);
            rows.add(new String[]{
                o.getId(), o.getTipo().toString(),
                o.getInmueble() != null ? o.getInmueble().getCodigo() : "",
                o.getCliente()  != null ? o.getCliente().getId()  : "",
                o.getAsesor()   != null ? o.getAsesor().getId()   : "",
                String.valueOf(o.getValor()), String.valueOf(o.getComision()),
                o.getEstado().toString(),
                o.getFecha() != null ? o.getFecha().format(D) : ""
            });
        }
        escribir("operaciones.csv", rows);
    }

    public void guardarContratos(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id","tipo","inmuebleCodigo","clienteId","fechaInicio","fechaFin","valorMensual","estado"});
        for (int i = 0; i < s.getContratos().tamanio(); i++) {
            Contrato c = s.getContratos().obtener(i);
            rows.add(new String[]{
                c.getId(), c.getTipo().toString(),
                c.getInmueble() != null ? c.getInmueble().getCodigo() : "",
                c.getCliente()  != null ? c.getCliente().getId()  : "",
                c.getFechaInicio() != null ? c.getFechaInicio().format(D) : "",
                c.getFechaFin()    != null ? c.getFechaFin().format(D)    : "",
                String.valueOf(c.getValorMensual()), c.getEstado().toString()
            });
        }
        escribir("contratos.csv", rows);
    }

    public void guardarIntenciones(SistemaInmobiliario s) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"id","clienteId","inmuebleCodigo","tipo","oferta","observaciones","estado","fecha"});
        for (int i = 0; i < s.getIntenciones().tamanio(); i++) {
            Intencion in = s.getIntenciones().obtener(i);
            rows.add(new String[]{
                in.getId(),
                in.getCliente()  != null ? in.getCliente().getId()        : "",
                in.getInmueble() != null ? in.getInmueble().getCodigo()   : "",
                in.getTipo().toString(), String.valueOf(in.getOferta()),
                esc(in.getObservaciones()), in.getEstado().toString(),
                in.getFecha() != null ? in.getFecha().format(DT) : ""
            });
        }
        escribir("intenciones.csv", rows);
    }

    // ═══════════════════════════════════════════════════════════════════
    // CARGAR
    // ═══════════════════════════════════════════════════════════════════

    public boolean cargarTodo(SistemaInmobiliario s) {
        Path inmPath = dir.resolve("inmuebles.csv");
        if (!Files.exists(inmPath)) return false; // sin datos previos

        cargarAsesores(s);
        cargarInmuebles(s);
        cargarClientes(s);
        cargarVisitas(s);
        cargarOperaciones(s);
        cargarContratos(s);
        cargarIntenciones(s);
        return true;
    }

    private void cargarAsesores(SistemaInmobiliario s) {
        for (String[] f : leer("asesores.csv")) {
            if (f.length < 7 || f[0].equals("id")) continue;
            try {
                Asesor a = new Asesor(f[0], f[1], f[2], f[3], des(f[5]), f[4]);
                a.setCierresRealizados(Integer.parseInt(safe(f, 6, "0")));
                a.setFotoPerfil(safe(f, 7, ""));
                s.registrarAsesor(a);
            } catch (Exception e) { System.err.println("[CSV] Asesor skip: " + e.getMessage()); }
        }
    }

    private void cargarInmuebles(SistemaInmobiliario s) {
        for (String[] f : leer("inmuebles.csv")) {
            if (f.length < 12 || f[0].equals("codigo")) continue;
            try {
                TipoInmueble tipo = TipoInmueble.valueOf(f[4]);
                EstadoInmueble estado = EstadoInmueble.valueOf(f[10]);
                Asesor asesor = f[11].isBlank() ? null : s.buscarAsesor(f[11]);
                Inmueble inm = new Inmueble(f[0], des(f[1]), des(f[2]), des(f[3]),
                        tipo, f[5], Double.parseDouble(f[6]), Double.parseDouble(f[7]),
                        Integer.parseInt(f[8]), Integer.parseInt(f[9]), estado, asesor);
                inm.setVisitas(Integer.parseInt(safe(f, 12, "0")));
                inm.setFotoUrl(safe(f, 13, ""));
                s.registrarInmueble(inm);
                if (asesor != null) asesor.getInmueblesAsignados().agregarFinal(inm);
            } catch (Exception e) { System.err.println("[CSV] Inmueble skip: " + e.getMessage()); }
        }
    }

    private void cargarClientes(SistemaInmobiliario s) {
        for (String[] f : leer("clientes.csv")) {
            if (f.length < 11 || f[0].equals("id")) continue;
            try {
                Cliente.TipoCliente tc = Cliente.TipoCliente.valueOf(safe(f, 5, "COMPRADOR"));
                TipoInmueble td = null;
                try { if (!safe(f,8,"").isBlank()) td = TipoInmueble.valueOf(f[8]); } catch (Exception ignored){}
                Cliente c = new Cliente(f[0], des(f[1]), f[2], f[3], f[4],
                        tc, Double.parseDouble(safe(f,6,"0")), des(safe(f,7,"")),
                        td, Integer.parseInt(safe(f,9,"0")));
                try { c.setEstadoBusqueda(Cliente.EstadoBusqueda.valueOf(f[10])); } catch (Exception ignored){}
                c.setFotoPerfil(safe(f, 11, ""));
                s.registrarCliente(c);
            } catch (Exception e) { System.err.println("[CSV] Cliente skip: " + e.getMessage()); }
        }
    }

    private void cargarVisitas(SistemaInmobiliario s) {
        for (String[] f : leer("visitas.csv")) {
            if (f.length < 5 || f[0].equals("clienteId")) continue;
            try {
                Cliente cli = s.buscarCliente(f[0]);
                Inmueble inm = s.buscarInmueble(f[1]);
                Asesor   ase = f[2].isBlank() ? null : s.buscarAsesor(f[2]);
                if (cli == null || inm == null) continue;
                LocalDateTime fh = f[3].isBlank() ? LocalDateTime.now() : LocalDateTime.parse(f[3], DT);
                EstadoVisita ev = EstadoVisita.PENDIENTE;
                try { ev = EstadoVisita.valueOf(f[4]); } catch (Exception ignored){}
                String obs = safe(f, 5, "");
                Visita v = new Visita(cli, inm, ase, fh, ev, des(obs));
                s.getTodasLasVisitas().agregarFinal(v);
                if (ev == EstadoVisita.PENDIENTE || ev == EstadoVisita.CONFIRMADA) {
                    s.getVisitasPendientes().encolar(v);
                }
            } catch (Exception e) { System.err.println("[CSV] Visita skip: " + e.getMessage()); }
        }
    }

    private void cargarOperaciones(SistemaInmobiliario s) {
        for (String[] f : leer("operaciones.csv")) {
            if (f.length < 8 || f[0].equals("id")) continue;
            try {
                Inmueble inm = s.buscarInmueble(f[2]);
                Cliente  cli = s.buscarCliente(f[3]);
                Asesor   ase = f[4].isBlank() ? null : s.buscarAsesor(f[4]);
                TipoOperacion tipo = TipoOperacion.valueOf(f[1]);
                EstadoOperacion est = EstadoOperacion.valueOf(safe(f,7,"ACTIVA"));
                Operacion o = new Operacion(f[0], inm, cli, ase, tipo,
                        java.time.LocalDate.now(), Double.parseDouble(safe(f,5,"0")), Double.parseDouble(safe(f,6,"0")), est);
                s.getOperaciones().agregarFinal(o);
            } catch (Exception e) { System.err.println("[CSV] Operacion skip: " + e.getMessage()); }
        }
    }

    private void cargarContratos(SistemaInmobiliario s) {
        for (String[] f : leer("contratos.csv")) {
            if (f.length < 7 || f[0].equals("id")) continue;
            try {
                Inmueble inm = s.buscarInmueble(f[2]);
                Cliente  cli = s.buscarCliente(f[3]);
                Contrato.TipoContrato tipo = Contrato.TipoContrato.valueOf(f[1]);
                LocalDate fi = f[4].isBlank() ? null : LocalDate.parse(f[4], D);
                LocalDate ff = f[5].isBlank() ? null : LocalDate.parse(f[5], D);
                Contrato c = new Contrato(f[0], inm, cli, tipo, fi, ff, Double.parseDouble(safe(f,6,"0")));
                try { c.setEstado(Contrato.EstadoContrato.valueOf(f[7])); } catch (Exception ignored){}
                s.getContratos().agregarFinal(c);
            } catch (Exception e) { System.err.println("[CSV] Contrato skip: " + e.getMessage()); }
        }
    }

    private void cargarIntenciones(SistemaInmobiliario s) {
        for (String[] f : leer("intenciones.csv")) {
            if (f.length < 6 || f[0].equals("id")) continue;
            try {
                Cliente  cli = s.buscarCliente(f[1]);
                Inmueble inm = s.buscarInmueble(f[2]);
                if (cli == null || inm == null) continue;
                Intencion.TipoIntencion tipo = Intencion.TipoIntencion.valueOf(safe(f,3,"COMPRA"));
                Intencion intent = new Intencion(f[0], cli, inm, tipo,
                        Double.parseDouble(safe(f,4,"0")), des(safe(f,5,"")));
                try { intent.setEstado(Intencion.EstadoIntencion.valueOf(f[6])); } catch (Exception ignored){}
                s.getIntenciones().agregarFinal(intent);
            } catch (Exception e) { System.err.println("[CSV] Intencion skip: " + e.getMessage()); }
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // GUARDAR FOTO — escribe el archivo de imagen en /fotos/
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Guarda una foto en base64 como archivo PNG en data/fotos/.
     * @return ruta relativa al archivo guardado (ej. "fotos/inmuebles/INM-001.png")
     */
    public String guardarFoto(String subdir, String nombre, String base64) throws IOException {
        String datos = base64;
        if (datos.contains(",")) datos = datos.substring(datos.indexOf(',') + 1);
        byte[] bytes = java.util.Base64.getDecoder().decode(datos);
        Path fotoDir = dir.resolve("fotos").resolve(subdir);
        Files.createDirectories(fotoDir);
        // Detectar extensión por cabecera base64
        String ext = base64.startsWith("data:image/png") ? "png"
                   : base64.startsWith("data:image/gif") ? "gif"
                   : "jpg";
        Path archivo = fotoDir.resolve(nombre + "." + ext);
        Files.write(archivo, bytes);
        return "fotos/" + subdir + "/" + nombre + "." + ext;
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPERS I/O
    // ═══════════════════════════════════════════════════════════════════

    private void escribir(String nombre, List<String[]> rows) {
        Path p = dir.resolve(nombre);
        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            for (String[] row : rows) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < row.length; i++) {
                    if (i > 0) sb.append(SEP);
                    sb.append(row[i] != null ? row[i] : "");
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("[CSV] Error escribiendo " + nombre + ": " + e.getMessage());
        }
    }

    private List<String[]> leer(String nombre) {
        Path p = dir.resolve(nombre);
        List<String[]> result = new ArrayList<>();
        if (!Files.exists(p)) return result;
        try (BufferedReader br = Files.newBufferedReader(p)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) result.add(line.split(SEP_RX, -1));
            }
        } catch (IOException e) {
            System.err.println("[CSV] Error leyendo " + nombre + ": " + e.getMessage());
        }
        return result;
    }

    /** Escapa el carácter separador en valores de texto */
    private static String esc(String v) {
        return v == null ? "" : v.replace(SEP, "&#124;").replace("\n", " ").replace("\r", "");
    }
    /** Desescapa el carácter separador */
    private static String des(String v) {
        return v == null ? "" : v.replace("&#124;", SEP);
    }
    /** Acceso seguro a array */
    private static String safe(String[] arr, int idx, String def) {
        return (arr != null && idx < arr.length && arr[idx] != null && !arr[idx].isBlank())
                ? arr[idx] : def;
    }
}
