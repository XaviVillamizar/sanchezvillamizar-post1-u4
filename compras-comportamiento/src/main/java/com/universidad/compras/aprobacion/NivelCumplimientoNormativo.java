package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class NivelCumplimientoNormativo extends NivelAprobacion {

    @Override
    protected boolean puedeResolver(Solicitud solicitud) {
        return "INTERNACIONAL".equals(solicitud.getCategoria());
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setNivelResolutor("Revisor de Cumplimiento Normativo");
        return new ResultadoAprobacion(false, "Revisor de Cumplimiento Normativo",
                "Pendiente de revisión de cumplimiento normativo antes de continuar");
    }
}