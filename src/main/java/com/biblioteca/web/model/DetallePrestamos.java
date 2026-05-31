package com.biblioteca.web.model;

import lombok.Data;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
public class DetallePrestamos {

    private int idDetalle;
    private int idPrestamo;
    private int idEjemplar;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucionReal;
    public enum EstadoItem {
        ACTIVO, DEVUELTO
    }
    private EstadoItem estadoItem;
    private Integer diasRetraso;
    private BigDecimal montoMora;
    private BigDecimal montoPagado;
    public enum EstadoPagoMora {
        SIN_MORA, PENDIENTE, PAGADO
    }
    private EstadoPagoMora estadoPagoMora;
}