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

    public List<Ejemplar> listarPorDocumento(int idDocumento) {
        List<Ejemplar> lista = new ArrayList<>();

        // Consulta SQL limpia usando las columnas reales de tu tabla Ejemplar
        // Hacemos un INNER JOIN con Documento para obtener el título en caso de requerirse
        String sql = "SELECT e.id_ejemplar, e.id_documento, e.codigo_de_barras, e.estado, d.titulo " +
                "FROM Ejemplar e " +
                "INNER JOIN Documento d ON e.id_documento = d.id_documento " +
                "WHERE e.id_documento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Asignamos el id del documento al parámetro del query (?)
            ps.setInt(1, idDocumento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ejemplar ej = new Ejemplar();
                    ej.setIdEjemplar(rs.getInt("id_ejemplar"));
                    ej.setIdDocumento(rs.getInt("id_documento"));
                    ej.setCodigoDeBarras(rs.getString("codigo_de_barras"));
                    ej.setEstado(rs.getString("estado"));
                    ej.setTituloDocumento(rs.getString("titulo")); // Atributo auxiliar para la vista

                    lista.add(ej);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

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

    public boolean insertar(Ejemplar ejemplar) {
        String sql = "INSERT INTO Ejemplar (id_documento, codigo_de_barras, estado) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ejemplar.getIdDocumento());
            ps.setString(2, ejemplar.getCodigoDeBarras());
            ps.setString(3, ejemplar.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(int idEjemplar) {
        String sql = "DELETE FROM Ejemplar WHERE id_ejemplar = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEjemplar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}