package com.universidad.compras.estado;

public class EstadoPendiente implements EstadoSolicitud {

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EstadoAprobada());
        return "Aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EstadoRechazada());
        return "Rechazada";
    }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        return "Error: debe estar aprobada antes de ejecutarse";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EstadoCancelada());
        return "Cancelada";
    }

    @Override
    public String nombre() { return "PENDIENTE"; }
}