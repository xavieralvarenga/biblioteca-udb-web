package com.biblioteca.web.model;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Prestamos {
    private int idPrestamo;
    private int idUsuario;
    private String nombres; 
    private String carnetDocenteAlumno;
    private LocalDate fechaPrestamo;
    public enum EstadoGeneral {
        ACTIVO, CONDEUDA, FINALIZADO, PARCIAL
    };
    private EstadoGeneral estadoGeneral;
}
