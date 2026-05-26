package com.biblioteca.web.dao;

import com.biblioteca.web.model.Documento;
import com.biblioteca.web.model.Libro;
import com.biblioteca.web.model.Revista;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente DAO encargado de centralizar las operaciones CRUD en las tablas de inventario de documentos.
 * Soporta de forma polimórfica la extracción y persistencia de Libros y Revistas.
 * * @author Xavier Larios
 * @version 1.0
 */
public class DocumentoDAO {

    /**
     * Recupera la lista completa de documentos registrados en el sistema, unificando los tipos.
     * @return Una lista de objetos derivados de {@link Documento}.
     */
    public List<Documento> listarTodos() {
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT d.*, l.isbn, l.autor, l.numero_paginas, l.editorial, " +
                "r.numero_edicion, r.periodicidad, r.organismo_responsable " +
                "FROM Documentos d " +
                "LEFT JOIN Libros l ON d.id_documento = l.id_documento " +
                "LEFT JOIN Revistas r ON d.id_documento = r.id_documento";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("tipo_documento");
                if ("Libro".equalsIgnoreCase(tipo)) {
                    Libro libro = new Libro();
                    mapearCamposComunes(rs, libro);
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setNumeroPaginas(rs.getInt("numero_paginas"));
                    libro.setEditorial(rs.getString("editorial"));
                    lista.add(libro);
                } else if ("Revista".equalsIgnoreCase(tipo)) {
                    Revista revista = new Revista();
                    mapearCamposComunes(rs, revista);
                    revista.setNumeroEdicion(rs.getInt("numero_edicion"));
                    revista.setPeriodicidad(rs.getString("periodicidad"));
                    revista.setOrganismoResponsable(rs.getString("organismo_responsable"));
                    lista.add(revista);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Transfiere las columnas comunes de la consulta SQL hacia la instancia del modelo de dominio.
     */
    private void mapearCamposComunes(ResultSet rs, Documento doc) throws SQLException {
        doc.setIdDocumento(rs.getInt("id_documento"));
        doc.setTitulo(rs.getString("titulo"));
        doc.setAnioPublicacion(rs.getInt("anio_publicacion"));
        doc.setCantidadEjemplares(rs.getInt("cantidad_ejemplares"));
    }

    /**
     * Inserta un nuevo Libro en el sistema aplicando una transacción sobre las tablas jerárquicas.
     * @param libro Objeto que contiene la información completa a almacenar.
     * @return {@code true} si la inserción fue exitosa; {@code false} de lo contrario.
     */
    public boolean insertarLibro(Libro libro) {
        String sqlDoc = "INSERT INTO Documentos (titulo, anio_publicacion, cantidad_ejemplares, tipo_documento) VALUES (?, ?, ?, 'Libro')";
        String sqlLib = "INSERT INTO Libros (id_documento, isbn, autor, numero_paginas, editorial) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Inicia bloque de transacción unificada

            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS)) {
                psDoc.setString(1, libro.getTitulo());
                psDoc.setInt(2, libro.getAnioPublicacion());
                psDoc.setInt(3, libro.getCantidadEjemplares());
                psDoc.executeUpdate();

                try (ResultSet generatedKeys = psDoc.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        libro.setIdDocumento(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Error al obtener la llave primaria asignada.");
                    }
                }
            }

            try (PreparedStatement psLib = conn.prepareStatement(sqlLib)) {
                psLib.setInt(1, libro.getIdDocumento());
                psLib.setString(2, libro.getIsbn());
                psLib.setString(3, libro.getAutor());
                psLib.setInt(4, libro.getNumeroPaginas());
                psLib.setString(5, libro.getEditorial());
                psLib.executeUpdate();
            }

            conn.commit(); // Confirma los datos en la base
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        }
        return false;
    }
}