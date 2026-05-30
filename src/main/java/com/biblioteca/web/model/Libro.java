package com.biblioteca.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad especializada para la gestión de Libros.
 * @author Xavier Larios
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Libro extends Documento {
    private String isbn;
    private String editorial;
    private String edicion; // Cambiado a String para coincidir con tu setEdicion() y el DDL
}