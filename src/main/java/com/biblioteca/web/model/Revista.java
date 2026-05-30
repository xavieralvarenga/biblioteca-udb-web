package com.biblioteca.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Entidad especializada para la gestión de Revistas.
 * @author Xavier Larios
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Revista extends Documento {
    private String issn;
    private String volumen;
    private String mesPublicacion; // Mapea directamente a setMesPublicacion()
}