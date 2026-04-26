package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.modelo.usuario.Usuario;
import com.edu.uniquindio.proptech.servicios.interfaces.IUsuario;
import com.edu.uniquindio.proptech.utils.excepciones.ElementoNoEncontradoException;
import com.edu.uniquindio.proptech.utils.excepciones.ParametroVacioException;

public class ServiciosUsuario implements IUsuario {

    @Override
    public Usuario iniciarSesion(String email, String contrasenia){
        if (email == null || email.isEmpty()) new ParametroVacioException("El email esta vacio");
        if (contrasenia == null || contrasenia.isEmpty()) new ParametroVacioException("La contrasenia esta vacia");

        Usuario usuario = usuarioRepositorio.buscarUsuarioPorEmail(email);

        if (usuario == null) new ElementoNoEncontradoException("El usuario no existe");
        if (!usuario.getContrasenia().equals(contrasenia)) new ElementoNoEncontradoException("La contrasenia no coincide con el email");

        return usuario;
    }
}
