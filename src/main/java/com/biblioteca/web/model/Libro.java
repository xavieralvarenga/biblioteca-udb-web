package com.biblioteca.web.model;

import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * Representa un Documento específico de tipo Libro.
 * Extiende de la clase base {@link Documento} añadiendo propiedades exclusivas de publicaciones impresas literarias.
 * * @author Xavier Larios
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Libro extends Documento {
    /** Código Internacional Normalizado para Libros. */
    private String isbn;

    /** Nombre del autor o creador de la obra. */
    private String autor;

    /** Número total de páginas del ejemplar. */
    private int numeroPaginas;

    /** Casa editorial encargada de la distribución. */
    private String editorial;

    @Override
    public String getTipoDocumento() {
        return "Libro";
    }
}