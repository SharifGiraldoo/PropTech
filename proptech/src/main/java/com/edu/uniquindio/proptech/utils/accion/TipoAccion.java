package com.edu.uniquindio.proptech.utils.accion;

/**
 * Enumeracion que define los tipos de acciones que se pueden realizar sobre el sistema.
 * Permite clasificar los cambios para su posterior reversa mediante la pila.
 * Autores: [Sharif Giraldo Obando, Juan Sebastián Hernández y Santiago Ospina Sánchez]
 * Fecha de creacion: 2026-04-26
 * Licencia: MIT
 */
public enum TipoAccion {
    CREACION_INMUEBLE,
    EDICION_INMUEBLE,
    ELIMINACION_INMUEBLE,
    CAMBIO_ESTADO
}
