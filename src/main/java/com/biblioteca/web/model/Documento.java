package com.biblioteca.web.model;

import lombok.Data;

/**
 * Clase abstracta que representa la entidad base de un Documento de información dentro de la biblioteca.
 * Contiene los atributos globales compartidos por cualquier tipo de material.
 * * @author Xavier Larios
 * @version 1.0
 */
@Data
public abstract class Documento {
    /** Identificador único del documento en la base de datos. */
    private int idDocumento;

    /** Título de la obra o material educativo. */
    private String titulo;

    /** Año de publicación del recurso. */
    private int anioPublicacion;

    /** Cantidad total de ejemplares físicos disponibles en el inventario. */
    private int cantidadEjemplares;

    /**
     * Método abstracto para obtener el tipo de documento específico (Libro, Revista, CD).
     * @return Una cadena de texto indicando la clasificación del material.
     */
    public abstract String getTipoDocumento();
}
