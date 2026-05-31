package com.biblioteca.web.model;

import lombok.Data;

@Data
public class Ejemplar {
    private int idEjemplar;
    private int idDocumento;
    private String codigoDeBarras;
    private String estado;

    // Atributos extra (DTO) traídos con un JOIN para mostrarlos en la vista web
    private String tituloDocumento;
    private String autorDocumento;
    private String tipoDocumento;
}