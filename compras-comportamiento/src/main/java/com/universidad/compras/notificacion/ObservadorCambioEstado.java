package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Observer: cada reacción ante un cambio de estado implementa esta
 * interfaz. El sujeto (NotificadorCambioEstado) no conoce qué hace
 * cada observador, solo que reacciona a un cambio.
 */
public interface ObservadorCambioEstado {
    void onCambioEstado(Solicitud solicitud);
}