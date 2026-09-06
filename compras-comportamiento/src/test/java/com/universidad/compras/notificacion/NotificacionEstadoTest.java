package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");
        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado();

        assertDoesNotThrow(() -> mecanismo.notificarCambio(s));
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        List<String> colectorDePrueba = new ArrayList<>();

        NotificadorCambioEstado mecanismo = new NotificadorCambioEstado();
        mecanismo.suscribir(solicitud -> colectorDePrueba.add("Notificado: " + solicitud.getId()));

        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1000000, "SOFTWARE", "CC-200");
        assertDoesNotThrow(() -> mecanismo.notificarCambio(s));

        assertEquals(1, colectorDePrueba.size());
        assertEquals("Notificado: S-021", colectorDePrueba.get(0));
    }
}