package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.NotificadorCambioEstado;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionPorNiveles implements ServicioAprobacion {

    private final NivelAprobacion cadena;
    private final NotificadorCambioEstado notificador;

    public ServicioAprobacionPorNiveles(NotificadorCambioEstado notificador) {
        this.notificador = notificador;

        NivelAprobacion cumplimiento = new NivelCumplimientoNormativo();
        NivelAprobacion supervisor = new NivelSupervisorArea();
        NivelAprobacion gerente = new NivelGerenteArea();
        NivelAprobacion director = new NivelDirectorFinanciero();

        cumplimiento.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(director);

        this.cadena = cumplimiento;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = cadena.evaluar(solicitud);
        notificador.notificarCambio(solicitud);
        return resultado;
    }
}