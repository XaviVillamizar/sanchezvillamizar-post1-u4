package com.universidad.compras.ejecucion;

/**
 * Command: encapsula una operación como objeto, con capacidad de
 * ejecutarse y deshacerse de forma independiente.
 */
public interface OperacionEjecutable {
    void ejecutar();
    void deshacer();
    String getDescripcion();
}