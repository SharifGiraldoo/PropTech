package com.edu.uniquindio.proptech.modelo.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class Usuario {
    String id;
    String nombre;
    String correo;
    String telefono;
    String contrasenia;
}
