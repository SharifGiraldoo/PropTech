package com.edu.uniquindio.proptech.servicios.modulos;

import com.edu.uniquindio.proptech.estructuras.lista.ListaSimple;
import com.edu.uniquindio.proptech.modelo.operaciones.EstadoOperacion;
import com.edu.uniquindio.proptech.modelo.operaciones.Operacion;
import com.edu.uniquindio.proptech.servicios.modulos.SistemaInmobilario;
import com.edu.uniquindio.proptech.servicios.interfaces.IOperacion;

public class ServiciosOperacion implements IOperacion {
    @Override
    public void calcularComision(Operacion operacion) {
        SistemaInmobilario sistemaInmobilario = new SistemaInmobilario();
        ListaSimple<Operacion> operaciones = sistemaInmobilario.getOperaciones();

        double comision = 0;

        for (int j = 0; j < operaciones.tamanio(); j++) {
            Operacion op = operaciones.obtener(j);

            if (op.getAsesor().equals(operacion.getAsesor())) {
                comision += op.getValor() * 0.05;
            }
        }
    }

    @Override
    public void cambiarEstado(Operacion operacion, EstadoOperacion estado) {
        operacion.setEstado(estado);
    }
}
