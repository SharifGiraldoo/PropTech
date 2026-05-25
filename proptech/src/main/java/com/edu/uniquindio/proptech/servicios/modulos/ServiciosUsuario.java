package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.usuario.Asesor;
import com.edu.uniquindio.proptech.modelo.usuario.Cliente;
import com.edu.uniquindio.proptech.modelo.usuario.Usuario;
import com.edu.uniquindio.proptech.utils.excepciones.ElementoNoEncontradoException;
import com.edu.uniquindio.proptech.utils.excepciones.ParametroVacioException;

import java.util.List;

/**
 * Servicio de autenticación de usuarios.
 * Busca en la lista de admins registrados Y en las tablas hash de
 * clientes y asesores del sistema para validar credenciales.
 *
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public class ServiciosUsuario {

    private final List<Usuario> admins;
    private final SistemaInmobiliario sistema;

    public ServiciosUsuario(List<Usuario> admins, SistemaInmobiliario sistema) {
        this.admins = admins;
        this.sistema = sistema;
    }

    /**
     * Inicia sesión buscando en admins, clientes y asesores.
     * Devuelve el Usuario con su rol correcto.
     */
    public Usuario iniciarSesion(String correo, String contrasenia) {
        if (correo == null || correo.isBlank())
            throw new ParametroVacioException("El correo no puede estar vacío");
        if (contrasenia == null || contrasenia.isBlank())
            throw new ParametroVacioException("La contraseña no puede estar vacía");

        String correoNorm = correo.trim().toLowerCase();

        // 1. Buscar en admins
        for (Usuario u : admins) {
            if (u.getCorreo().equalsIgnoreCase(correoNorm)
                    && u.getContrasenia().equals(contrasenia)) {
                return u;
            }
        }

        // 2. Buscar en asesores
        for (Asesor a : sistema.getAsesores().valores()) {
            if (a.getCorreo().equalsIgnoreCase(correoNorm)
                    && a.getContrasenia().equals(contrasenia)) {
                return a;
            }
        }

        // 3. Buscar en clientes
        for (Cliente c : sistema.getClientes().valores()) {
            if (c.getCorreo().equalsIgnoreCase(correoNorm)
                    && c.getContrasenia().equals(contrasenia)) {
                return c;
            }
        }

        throw new ElementoNoEncontradoException("Credenciales incorrectas");
    }

    /** Busca un usuario por ID en admins, asesores y clientes. */
    public Usuario buscarUsuario(String id) {
        if (id == null) return null;
        for (Usuario u : admins) { if (id.equals(u.getId())) return u; }
        for (Asesor  a : sistema.getAsesores().valores()) { if (id.equals(a.getId())) return a; }
        for (Cliente c : sistema.getClientes().valores()) { if (id.equals(c.getId())) return c; }
        return null;
    }
}
