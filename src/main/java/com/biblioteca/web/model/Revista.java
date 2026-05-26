package com.biblioteca.web.model;

import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * Representa un Documento específico de tipo Revista o Publicación Periódica.
 * Contiene información de volumen, número y periodicidad.
 * * @author Xavier Larios
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Revista extends Documento {
    /** Número de edición o volumen de la revista. */
    private int numeroEdicion;

    /** Frecuencia de publicación (Mensual, Semestral, Anual). */
    private String periodicidad;

    /** Entidad corporativa, institucional o académica responsable de la edición. */
    private String organismoResponsable;

    @Override
    public String getTipoDocumento() {
        return "Revista";
    }
}
