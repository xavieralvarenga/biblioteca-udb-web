package com.biblioteca.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private int idUsuario;
    private int idTipo; // FK hacia TipoUsuario
    private String nombres;
    private String apellidos;
    private String carnetDocenteAlumno;
    private String passwordHash;
    private boolean estadoMora;
    private String estado;

    // Campo extra opcional para mostrar el nombre del rol en la vista sin complicar el DAO
    private String nombreRol;
}