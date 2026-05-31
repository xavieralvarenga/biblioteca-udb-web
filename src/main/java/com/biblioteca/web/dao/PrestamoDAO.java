package com.biblioteca.web.dao;

import com.biblioteca.web.model.Prestamos;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public List<Prestamos> obtenerTodosLosPrestamos() {
        List<Prestamos> lista = new ArrayList<>();
        
        // ¡AQUÍ ESTÁ LA SUB-CONSULTA QUE FALTABA (total_deuda)!
        String sql = "SELECT p.id_prestamo, u.carnet_docente_alumno, u.Nombres, p.fecha_prestamo, p.estado_general, " +
                     "(SELECT COUNT(*) FROM detalle_prestamo dp WHERE dp.id_prestamo = p.id_prestamo) AS total_items, " +
                     "(SELECT COALESCE(SUM(monto_mora - monto_pagado), 0) FROM detalle_prestamo dp WHERE dp.id_prestamo = p.id_prestamo AND dp.estado_pago_mora = 'Pendiente') AS total_deuda " +
                     "FROM prestamo p INNER JOIN usuarios u ON p.id_usuario = u.ID_Usuario ORDER BY p.id_prestamo DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Prestamos prestamo = new Prestamos();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                
                String estadoDesdeBD = rs.getString("estado_general");
                if (estadoDesdeBD != null) {
                    prestamo.setEstadoGeneral(Prestamos.EstadoGeneral.valueOf(estadoDesdeBD.replace(" ", "").toUpperCase()));
                }
                
                prestamo.setNombres(rs.getString("Nombres"));
                prestamo.setCarnetDocenteAlumno(rs.getString("carnet_docente_alumno"));
                
                // ¡ESTA LÍNEA ES VITAL PARA QUE EL BOTÓN MUESTRE LA CANTIDAD REAL!
                prestamo.setTotalDeuda(rs.getDouble("total_deuda"));
                
                lista.add(prestamo);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener préstamos: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarNuevoPrestamo(int idUsuario, java.time.LocalDate fechaPrestamo,
                                          List<Integer> idsEjemplares, List<java.time.LocalDate> fechasLimites) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            String sqlCabecera = "INSERT INTO Prestamo (id_usuario, fecha_prestamo, estado_general) VALUES (?, ?, 'Activo')";
            int idPrestamoGenerado = 0;

            try (PreparedStatement psCabecera = con.prepareStatement(sqlCabecera, Statement.RETURN_GENERATED_KEYS)) {
                psCabecera.setInt(1, idUsuario);
                psCabecera.setDate(2, java.sql.Date.valueOf(fechaPrestamo));
                psCabecera.executeUpdate();
                try (ResultSet rs = psCabecera.getGeneratedKeys()) {
                    if (rs.next()) idPrestamoGenerado = rs.getInt(1);
                    else throw new SQLException("Error al generar ID de Préstamo.");
                }
            }

            String sqlDetalle = "INSERT INTO Detalle_Prestamo (id_prestamo, id_ejemplar, fecha_limite, estado_item) VALUES (?, ?, ?, 'Activo')";
            String sqlEjemplar = "UPDATE Ejemplar SET estado = 'Prestado' WHERE id_ejemplar = ?";

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement psEjemplar = con.prepareStatement(sqlEjemplar)) {
                for (int i = 0; i < idsEjemplares.size(); i++) {
                    psDetalle.setInt(1, idPrestamoGenerado);
                    psDetalle.setInt(2, idsEjemplares.get(i));
                    psDetalle.setDate(3, java.sql.Date.valueOf(fechasLimites.get(i)));
                    psDetalle.executeUpdate();

                    psEjemplar.setInt(1, idsEjemplares.get(i));
                    psEjemplar.executeUpdate();
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) { con.setAutoCommit(true); con.close(); }
        }
    }

    // --- MÉTODOS PARA VER EL DETALLE DEL PRÉSTAMO ---

    public Prestamos obtenerPrestamoPorId(int idPrestamo) {
        Prestamos prestamo = null;
        String sql = "SELECT p.id_prestamo, u.carnet_docente_alumno, u.Nombres, p.fecha_prestamo, p.estado_general " +
                "FROM prestamo p INNER JOIN usuarios u ON p.id_usuario = u.ID_Usuario " +
                "WHERE p.id_prestamo = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prestamo = new Prestamos();
                    prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                    prestamo.setCarnetDocenteAlumno(rs.getString("carnet_docente_alumno"));
                    prestamo.setNombres(rs.getString("Nombres"));
                    prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());

                    String estado = rs.getString("estado_general");
                    if (estado != null) {
                        prestamo.setEstadoGeneral(Prestamos.EstadoGeneral.valueOf(estado.replace(" ", "").toUpperCase()));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cabecera del préstamo: " + e.getMessage());
        }
        return prestamo;
    }

    public List<Object[]> obtenerDetallesPorPrestamo(int idPrestamo) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT dp.id_detalle, e.codigo_de_barras, d.titulo, dp.fecha_limite, dp.estado_item, dp.estado_pago_mora " +
                "FROM detalle_prestamo dp " +
                "INNER JOIN ejemplar e ON dp.id_ejemplar = e.id_ejemplar " +
                "INNER JOIN documento d ON e.id_documento = d.id_documento " +
                "WHERE dp.id_prestamo = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                            rs.getInt("id_detalle"),
                            rs.getString("codigo_de_barras"),
                            rs.getString("titulo"),
                            rs.getDate("fecha_limite").toLocalDate(),
                            rs.getString("estado_item"),
                            rs.getString("estado_pago_mora")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles del préstamo: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> obtenerDetallesParaDevolucion(int idPrestamo) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT dp.id_detalle, dp.id_ejemplar, e.codigo_de_barras, d.titulo, dp.fecha_limite, " +
                "GREATEST(DATEDIFF(CURDATE(), dp.fecha_limite), 0) AS dias_retraso, " +
                "(SELECT COALESCE(tarifa_diaria, 0.50) FROM mora_anual WHERE anio = YEAR(CURDATE())) AS tarifa_diaria " +
                "FROM detalle_prestamo dp " +
                "INNER JOIN ejemplar e ON dp.id_ejemplar = e.id_ejemplar " +
                "INNER JOIN documento d ON e.id_documento = d.id_documento " +
                "WHERE dp.id_prestamo = ? AND dp.estado_item = 'Activo'";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int diasRetraso = rs.getInt("dias_retraso");
                    double tarifa = rs.getDouble("tarifa_diaria");
                    lista.add(new Object[]{
                            rs.getInt("id_detalle"), rs.getInt("id_ejemplar"), rs.getString("codigo_de_barras"),
                            rs.getString("titulo"), rs.getDate("fecha_limite").toLocalDate(),
                            diasRetraso, tarifa, (diasRetraso * tarifa) // Mora Calculada
                    });
                }
            }
        } catch (SQLException e) { System.err.println("Error al calcular mora: " + e.getMessage()); }
        return lista;
    }

    // =========================================================================
    // NUEVA LÓGICA DE NEGOCIO: PAGO "TODO O NADA"
    // =========================================================================

    public boolean procesarDevolucionCompleta(int idPrestamo, boolean pagoCompleto, String observaciones) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            List<Object[]> detalles = obtenerDetallesParaDevolucion(idPrestamo);

            String sqlDetalle = "UPDATE detalle_prestamo SET estado_item = 'Devuelto', fecha_devolucion_real = CURDATE(), " +
                    "dias_retraso = ?, monto_mora = ?, monto_pagado = ?, estado_pago_mora = ? WHERE id_detalle = ?";
            String sqlEjemplar = "UPDATE ejemplar SET estado = 'Disponible' WHERE id_ejemplar = ?";
            String sqlDevolucion = "INSERT INTO devolucion (id_detalle, fecha_devolucion, observaciones_estado_fisico) VALUES (?, NOW(), ?)";

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement psEjemplar = con.prepareStatement(sqlEjemplar);
                 PreparedStatement psDev = con.prepareStatement(sqlDevolucion)) {

                for (Object[] row : detalles) {
                    int idDetalle = (int) row[0];
                    int idEjemplar = (int) row[1];
                    int diasRetraso = (int) row[5];
                    double moraItem = (double) row[7];

                    // Lógica TODO O NADA
                    String estadoPago = (moraItem <= 0) ? "Sin Mora" : (pagoCompleto ? "Pagado" : "Pendiente");
                    double montoPagado = pagoCompleto ? moraItem : 0.0;

                    // A. Actualizar Detalle
                    psDetalle.setInt(1, diasRetraso);
                    psDetalle.setDouble(2, moraItem);
                    psDetalle.setDouble(3, montoPagado);
                    psDetalle.setString(4, estadoPago);
                    psDetalle.setInt(5, idDetalle);
                    psDetalle.executeUpdate();

                    // B. Liberar Ejemplar a Disponible
                    psEjemplar.setInt(1, idEjemplar);
                    psEjemplar.executeUpdate();

                    // C. Registrar en Devolución
                    psDev.setInt(1, idDetalle);
                    psDev.setString(2, observaciones);
                    psDev.executeUpdate();
                }
            }

            actualizarMoraUsuario(con, idPrestamo);
            recalcularEstadoCabecera(con, idPrestamo);
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) { con.setAutoCommit(true); con.close(); }
        }
    }

    public boolean pagarDeudaPrestamo(int idPrestamo) throws SQLException {
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false);

            // Liquida toda la mora pendiente de un ticket
            String sql = "UPDATE detalle_prestamo SET monto_pagado = monto_mora, estado_pago_mora = 'Pagado' " +
                    "WHERE id_prestamo = ? AND estado_pago_mora = 'Pendiente'";
            try(PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idPrestamo);
                ps.executeUpdate();
            }

            actualizarMoraUsuario(con, idPrestamo);
            recalcularEstadoCabecera(con, idPrestamo);
            con.commit();
            return true;
        } catch (SQLException e) {
            if(con!=null) con.rollback();
            throw e;
        } finally {
            if(con!=null) { con.setAutoCommit(true); con.close(); }
        }
    }

    // =========================================================================
    // MÉTODOS PRIVADOS AUXILIARES
    // =========================================================================

    private void actualizarMoraUsuario(Connection con, int idPrestamo) throws SQLException {
        int idUsuario = 0;
        try (PreparedStatement ps = con.prepareStatement("SELECT id_usuario FROM prestamo WHERE id_prestamo = ?")) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) idUsuario = rs.getInt(1); }
        }

        boolean sigueDeudor = false;
        String sql = "SELECT COUNT(*) FROM detalle_prestamo dp INNER JOIN prestamo p ON dp.id_prestamo = p.id_prestamo WHERE p.id_usuario = ? AND dp.estado_pago_mora = 'Pendiente'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) sigueDeudor = rs.getInt(1) > 0; }
        }

        try (PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET estado_mora = ? WHERE ID_Usuario = ?")) {
            ps.setBoolean(1, sigueDeudor);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    private void recalcularEstadoCabecera(Connection con, int idPrestamo) throws SQLException {
        String sqlCheck = "SELECT COUNT(*) AS total, " +
                "SUM(IF(estado_item = 'Devuelto', 1, 0)) AS devueltos, " +
                "SUM(IF(estado_pago_mora = 'Pendiente', 1, 0)) AS con_deuda " +
                "FROM detalle_prestamo WHERE id_prestamo = ?";

        int total = 0, devueltos = 0, conDeuda = 0;
        try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                    devueltos = rs.getInt("devueltos");
                    conDeuda = rs.getInt("con_deuda");
                }
            }
        }

        String nuevoEstado = (devueltos > 0 && devueltos < total) ? "Parcial" :
                (devueltos == total && conDeuda > 0) ? "Con Deuda" :
                        (devueltos == total && conDeuda == 0) ? "Finalizado" : "Activo";

        try (PreparedStatement ps = con.prepareStatement("UPDATE prestamo SET estado_general = ? WHERE id_prestamo = ?")) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPrestamo);
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // VALIDACIONES DE REGLAS DE NEGOCIO
    // =========================================================================
    
    public int contarLibrosActivosPorUsuario(int idUsuario) {
        int totalActivos = 0;
        String sql = "SELECT COUNT(*) FROM detalle_prestamo dp " +
                     "INNER JOIN prestamo p ON dp.id_prestamo = p.id_prestamo " +
                     "WHERE p.id_usuario = ? AND dp.estado_item = 'Activo'";
                     
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalActivos = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al contar libros activos: " + e.getMessage());
        }
        return totalActivos;
    }

}