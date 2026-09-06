package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");

        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new NotificadorCambioEstado());
        ejecutor.ejecutarTodo(s, new PresupuestoService(), new OrdenCompraService(), "Proveedor XYZ");

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenCompraService = new OrdenCompraService();

        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new NotificadorCambioEstado());

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoCommand(presupuestoService, s.getCentroCosto(), s.getMonto()));
            ejecutor.ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, s.getId(), "Proveedor ABC"));
            ejecutor.deshacerUltima();
        });

        assertEquals(1, ejecutor.getHistorial().size());
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 2000000, "SOFTWARE", "CC-300");
        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenCompraService = new OrdenCompraService();

        EjecutorSolicitud ejecutor = new EjecutorSolicitud(new NotificadorCambioEstado());

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoCommand(presupuestoService, s.getCentroCosto(), s.getMonto()));
            ejecutor.ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, s.getId(), "Proveedor XYZ"));
        });

        assertEquals(2, ejecutor.getHistorial().size());
    }
}