package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificacionAuditoriaObserver implements ObservadorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.registrarAuditoria(
                solicitud.getId(), solicitud.getEstado(),
                "Nivel resolutor: " + solicitud.getNivelResolutor());
    }
}