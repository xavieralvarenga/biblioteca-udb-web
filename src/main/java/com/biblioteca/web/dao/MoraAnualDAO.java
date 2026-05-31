package com.biblioteca.web.dao;

import com.biblioteca.web.model.MoraAnual;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoraAnualDAO {

    // 1. Listar el historial de moras
    public List<MoraAnual> listarTodas() {
        List<MoraAnual> lista = new ArrayList<>();
        String sql = "SELECT anio, tarifa_diaria FROM mora_anual ORDER BY anio DESC";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MoraAnual mora = new MoraAnual();
                mora.setAnio(rs.getInt("anio"));
                mora.setTarifaDiaria(rs.getDouble("tarifa_diaria"));
                lista.add(mora);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar moras: " + e.getMessage());
        }
        return lista;
    }

    // 2. Guardar o Actualizar (Upsert)
    public boolean guardarOActualizar(MoraAnual mora) {
        // Si el año no existe, lo inserta. Si ya existe, actualiza la tarifa.
        String sql = "INSERT INTO mora_anual (anio, tarifa_diaria) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE tarifa_diaria = VALUES(tarifa_diaria)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mora.getAnio());
            ps.setDouble(2, mora.getTarifaDiaria());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar mora: " + e.getMessage());
            return false;
        }
    }
}