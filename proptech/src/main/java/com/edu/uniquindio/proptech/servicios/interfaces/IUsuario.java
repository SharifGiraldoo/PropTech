package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.usuario.Usuario;

public interface IUsuario {

    Usuario iniciarSesion(String email, String contrasenia);
}
