package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

/**
 * State: cada estado posible de una Solicitud sabe qué operaciones
 * acepta y a qué estado transiciona. Agregar un estado nuevo es
 * agregar una clase nueva, no modificar if/else dispersos.
 */
public interface EstadoSolicitud {
    String aprobar(ContextoSolicitud contexto);
    String rechazar(ContextoSolicitud contexto);
    String ejecutar(ContextoSolicitud contexto);
    String cancelar(ContextoSolicitud contexto);
    String nombre();
}