package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificacionCorreoObserver implements ObservadorCambioEstado {
    @Override
    public void onCambioEstado(Solicitud solicitud) {
        ClientesNotificacion.enviarCorreo(
                solicitud.getSolicitanteEmail(),
                "Actualización de su solicitud " + solicitud.getId(),
                "Su solicitud cambió al estado: " + solicitud.getEstado());
    }
}