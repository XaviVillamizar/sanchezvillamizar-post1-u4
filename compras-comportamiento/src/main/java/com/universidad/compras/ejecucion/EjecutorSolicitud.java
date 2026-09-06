package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import java.util.ArrayList;
import java.util.List;

public class EjecutorSolicitud {

    private final List<OperacionEjecutable> historial = new ArrayList<>();
    private final NotificadorCambioEstado notificador;

    public EjecutorSolicitud(NotificadorCambioEstado notificador) {
        this.notificador = notificador;
    }

    public void ejecutar(OperacionEjecutable operacion) {
        operacion.ejecutar();
        historial.add(operacion);
    }

    public void deshacerUltima() {
        if (!historial.isEmpty()) {
            OperacionEjecutable ultima = historial.remove(historial.size() - 1);
            ultima.deshacer();
        }
    }

    public List<OperacionEjecutable> getHistorial() {
        return historial;
    }

    public void ejecutarTodo(Solicitud solicitud, PresupuestoService presupuestoService,
                            OrdenCompraService ordenCompraService, String proveedor) {
        ejecutar(new ReservarPresupuestoCommand(presupuestoService, solicitud.getCentroCosto(), solicitud.getMonto()));
        ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, solicitud.getId(), proveedor));
        solicitud.setEstado("EJECUTADA");
        notificador.notificarCambio(solicitud);
    }
}