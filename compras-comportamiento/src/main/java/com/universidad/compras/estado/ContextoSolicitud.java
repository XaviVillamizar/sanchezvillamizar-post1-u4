package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

/**
 * Contexto del patrón State: mantiene la Solicitud y su estado
 * actual, delegando cada operación en el objeto-estado activo.
 * El estado inicial se deriva del getEstado() actual de la Solicitud
 * (dado que Solicitud es una clase provista que ya maneja su estado
 * como String).
 */
public class ContextoSolicitud {

    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public ContextoSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = mapearEstado(solicitud.getEstado());
    }

    private EstadoSolicitud mapearEstado(String estado) {
        return switch (estado) {
            case "APROBADA" -> new EstadoAprobada();
            case "EJECUTADA" -> new EstadoEjecutada();
            case "RECHAZADA" -> new EstadoRechazada();
            case "CANCELADA" -> new EstadoCancelada();
            default -> new EstadoPendiente();
        };
    }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.solicitud.setEstado(nuevoEstado.nombre());
    }

    public String aprobar() { return estadoActual.aprobar(this); }
    public String rechazar() { return estadoActual.rechazar(this); }
    public String ejecutar() { return estadoActual.ejecutar(this); }
    public String cancelar() { return estadoActual.cancelar(this); }
}