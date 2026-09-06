package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificacionDashboardObserver implements ObservadorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.actualizarDashboardContabilidad(
                solicitud.getId(), solicitud.getEstado(), solicitud.getMonto());
    }
}