package com.biblioteca.web.dao;

import com.biblioteca.web.model.Ejemplar;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO {

    public List<Ejemplar> obtenerEjemplaresDisponibles() {
        List<Ejemplar> lista = new ArrayList<>();

        String sql = "SELECT e.id_ejemplar, e.codigo_de_barras, d.titulo, d.autor, t.Nombre AS tipo_documento " +
                "FROM ejemplar e " +
                "INNER JOIN documento d ON e.id_documento = d.id_documento " +
                "INNER JOIN tipodocumento t ON d.id_tipo_doc = t.id_tipo_doc " +
                "WHERE e.estado = 'Disponible'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setCodigoDeBarras(rs.getString("codigo_de_barras"));
                ejemplar.setTituloDocumento(rs.getString("titulo"));
                ejemplar.setAutorDocumento(rs.getString("autor"));
                ejemplar.setTipoDocumento(rs.getString("tipo_documento"));

                lista.add(ejemplar);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ejemplares disponibles: " + e.getMessage());
        }
        return lista;
    }
}