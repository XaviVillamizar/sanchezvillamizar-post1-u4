package com.universidad.compras.estado;

public class EstadoAprobada implements EstadoSolicitud {

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        return "Error: ya fue aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        return "Error: no se puede rechazar una solicitud ya aprobada";
    }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EstadoEjecutada());
        return "Ejecutada";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.cambiarEstado(new EstadoCancelada());
        return "Cancelada";
    }

    @Override
    public String nombre() { return "APROBADA"; }
}