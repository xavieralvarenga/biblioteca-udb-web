package com.biblioteca.web.model;

import lombok.Data;

/**
 * Clase modelo que representa la entidad base Documento.
 * Contiene los atributos compartidos por todos los recursos del catálogo.
 * @author Xavier Larios
 */
@Data
public class Documento {
    private int idDocumento;
    private int idTipoDoc;
    private String titulo;
    private String autor;
    private String ubicacionFisica;
    private String codigoDeBarras;
    private String estado;
    private int cantidadEjemplares; // Campo calculado en el DAO
}