package com.edu.uniquindio.proptech.modelo.usuario;

/**
 * Clase base para todos los usuarios del sistema.
 * Autores: Sharif Giraldo Obando, Juan Sebastián Hernández, Santiago Ospina Sánchez
 */
public abstract class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasenia;
    private String fotoPerfil; // ruta relativa o base64 de la foto de perfil

    public Usuario(String id, String nombre, String correo, String telefono, String contrasenia) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
    }

    public abstract String getRol();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    @Override
    public String toString() {
        return getRol() + "[" + id + " - " + nombre + " - " + correo + "]";
    }
}
