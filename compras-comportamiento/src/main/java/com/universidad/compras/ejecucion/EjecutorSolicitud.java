package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import java.util.ArrayList;
import java.util.List;

/**
 * Invocador del patrón Command: mantiene un historial ordenado de
 * todas las operaciones ejecutadas sobre una solicitud, permitiendo
 * deshacer cualquiera de ellas de forma independiente.
 */
public class EjecutorSolicitud {

    private final List<OperacionEjecutable> historial = new ArrayList<>();

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
    }
}