package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionPorNiveles implements ServicioAprobacion {

    private final NivelAprobacion cadena;

    public ServicioAprobacionPorNiveles() {
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
        return cadena.evaluar(solicitud);
    }
}