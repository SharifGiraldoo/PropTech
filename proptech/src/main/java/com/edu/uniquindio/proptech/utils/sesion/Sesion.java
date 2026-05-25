package com.edu.uniquindio.proptech.utils.sesion;

import com.edu.uniquindio.proptech.modelo.usuario.Usuario;

public class Sesion {
    private static Sesion INSTANCIA;
    private Usuario usuario;

    private Sesion() {}

    public static Sesion getInstancia() {
        if (INSTANCIA == null) INSTANCIA = new Sesion();
        return INSTANCIA;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public boolean haySesionActiva() { return usuario != null; }
    public void cerrarSesion() { usuario = null; }
}
