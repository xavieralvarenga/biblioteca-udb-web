package com.biblioteca.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CD extends Documento {
    private int duracionMinutos;
    private String tipoContenido;

    public String getTipoDocumento() {
        return "CD";
    }
}