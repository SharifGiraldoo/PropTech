package com.edu.uniquindio.proptech.modelo.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin extends Usuario {
    public Admin(String id, String nombre, String correo, String telefono, String contrasenia) {
        super(id, nombre, correo, telefono, contrasenia);
    }
}
