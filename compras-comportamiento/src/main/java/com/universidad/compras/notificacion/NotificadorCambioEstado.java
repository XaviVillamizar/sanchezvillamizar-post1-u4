package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto del patrón Observer: mantiene la lista de observadores
 * suscritos y los notifica ante un cambio de estado, sin conocer
 * qué hace cada uno. Agregar un cuarto observador solo requiere
 * llamar a suscribir(), sin modificar esta clase.
 */
@Component
public class NotificadorCambioEstado {

    private final List<ObservadorCambioEstado> observadores = new ArrayList<>();

    public NotificadorCambioEstado() {
        suscribir(new NotificacionCorreoObserver());
        suscribir(new NotificacionDashboardObserver());
        suscribir(new NotificacionAuditoriaObserver());
    }

    public void suscribir(ObservadorCambioEstado observador) {
        observadores.add(observador);
    }

    public void notificarCambio(Solicitud solicitud) {
        for (ObservadorCambioEstado observador : observadores) {
            observador.onCambioEstado(solicitud);
        }
    }
}