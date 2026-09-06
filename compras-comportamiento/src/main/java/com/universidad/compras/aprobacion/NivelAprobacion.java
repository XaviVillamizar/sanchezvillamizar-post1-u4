package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Eslabón de la cadena de aprobación. Cada nivel decide si resuelve
 * la solicitud (aprobándola o rechazándola) o la delega al siguiente
 * nivel de la cadena. El siguiente nivel se configura con
 * setSiguiente(), permitiendo agregar, quitar o reordenar niveles sin
 * modificar ControladorSolicitudes ni los demás niveles.
 */
public abstract class NivelAprobacion {

    private NivelAprobacion siguiente;

    public NivelAprobacion setSiguiente(NivelAprobacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (puedeResolver(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.evaluar(solicitud);
        }
        return new ResultadoAprobacion(false, "Sin resolver", "Ningún nivel pudo resolver la solicitud");
    }

    protected abstract boolean puedeResolver(Solicitud solicitud);
    protected abstract ResultadoAprobacion resolver(Solicitud solicitud);
}