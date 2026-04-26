package com.edu.uniquindio.proptech.servicios.interfaces;

import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;

public interface IOperacion {
    void calcularComision(Operacion operacion);
    void cambiarEstado(Operacion operacion);
}
