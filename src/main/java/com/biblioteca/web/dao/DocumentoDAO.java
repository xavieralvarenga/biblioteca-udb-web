package com.biblioteca.web.dao;

import com.biblioteca.web.model.Documento;
import com.biblioteca.web.model.Libro;
import com.biblioteca.web.model.Revista;
import com.biblioteca.web.model.CD;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO ajustado a los atributos y tipos reales del modelo:
 * Libro (isbn, editorial, edicion [String])
 * Revista (issn [String], volumen [String], mesPublicacion [String])
 * CD (duracionMinutos [int], tipoContenido [String])
 */
public class DocumentoDAO {

    public List<Documento> listarTodos() {
        List<Documento> lista = new ArrayList<>();

        String sql = "SELECT d.id_documento, d.id_tipo_doc, d.titulo, d.autor, d.ubicacion_fisica, d.codigo_de_barras, d.estado, " +
                "t.Nombre AS nombre_tipo, " +
                "l.isbn, l.editorial, l.edicion, " +
                "r.issn, r.volumen, r.mes_publicacion, " +
                "c.duracion_minutos, c.tipo_contenido, " +
                "(SELECT COUNT(*) FROM Ejemplar e WHERE e.id_documento = d.id_documento) AS total_ejemplares " +
                "FROM Documento d " +
                "INNER JOIN TipoDocumento t ON d.id_tipo_doc = t.id_tipo_doc " +
                "LEFT JOIN Libro l ON d.id_documento = l.id_documento " +
                "LEFT JOIN Revista r ON d.id_documento = r.id_documento " +
                "LEFT JOIN CD c ON d.id_documento = c.id_documento";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String tipo = rs.getString("nombre_tipo");
                Documento doc;

                if ("Libro".equalsIgnoreCase(tipo)) {
                    Libro l = new Libro();
                    l.setIsbn(rs.getString("isbn"));
                    l.setEditorial(rs.getString("editorial"));
                    l.setEdicion(rs.getString("edicion")); // Mapeado correctamente como String
                    doc = l;
                } else if ("Revista".equalsIgnoreCase(tipo)) {
                    Revista r = new Revista();
                    r.setIssn(rs.getString("issn")); // Mapeado correctamente como String
                    r.setVolumen(rs.getString("volumen")); // Mapeado correctamente como String
                    r.setMesPublicacion(rs.getString("mes_publicacion")); // Mapeado correctamente como String
                    doc = r;
                } else {
                    CD c = new CD();
                    c.setDuracionMinutos(rs.getInt("duracion_minutos"));
                    c.setTipoContenido(rs.getString("tipo_contenido"));
                    doc = c;
                }

                // Propiedades de la clase padre Documento
                doc.setIdDocumento(rs.getInt("id_documento"));
                doc.setIdTipoDoc(rs.getInt("id_tipo_doc"));
                doc.setTitulo(rs.getString("titulo"));
                doc.setAutor(rs.getString("autor"));
                doc.setUbicacionFisica(rs.getString("ubicacion_fisica"));
                doc.setCodigoDeBarras(rs.getString("codigo_de_barras"));
                doc.setEstado(rs.getString("estado"));
                doc.setCantidadEjemplares(rs.getInt("total_ejemplares"));

                lista.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean insertarLibro(Libro libro) {
        String sqlDoc = "INSERT INTO Documento (id_tipo_doc, titulo, autor, ubicacion_fisica, codigo_de_barras, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlLib = "INSERT INTO Libro (id_documento, isbn, editorial, edicion) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS)) {
                psDoc.setInt(1, libro.getIdTipoDoc());
                psDoc.setString(2, libro.getTitulo());
                psDoc.setString(3, libro.getAutor());
                psDoc.setString(4, libro.getUbicacionFisica());
                psDoc.setString(5, libro.getCodigoDeBarras());
                psDoc.setString(6, libro.getEstado());
                psDoc.executeUpdate();

                try (ResultSet keys = psDoc.getGeneratedKeys()) {
                    if (keys.next()) { libro.setIdDocumento(keys.getInt(1)); }
                }
            }

            try (PreparedStatement psLib = conn.prepareStatement(sqlLib)) {
                psLib.setInt(1, libro.getIdDocumento());
                psLib.setString(2, libro.getIsbn());
                psLib.setString(3, libro.getEditorial());
                psLib.setString(4, libro.getEdicion());
                psLib.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertarRevista(Revista revista) {
        String sqlDoc = "INSERT INTO Documento (id_tipo_doc, titulo, autor, ubicacion_fisica, codigo_de_barras, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlRev = "INSERT INTO Revista (id_documento, issn, volumen, mes_publicacion) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS)) {
                psDoc.setInt(1, revista.getIdTipoDoc());
                psDoc.setString(2, revista.getTitulo());
                psDoc.setString(3, revista.getAutor());
                psDoc.setString(4, revista.getUbicacionFisica());
                psDoc.setString(5, revista.getCodigoDeBarras());
                psDoc.setString(6, revista.getEstado());
                psDoc.executeUpdate();

                try (ResultSet keys = psDoc.getGeneratedKeys()) {
                    if (keys.next()) { revista.setIdDocumento(keys.getInt(1)); }
                }
            }

            try (PreparedStatement psRev = conn.prepareStatement(sqlRev)) {
                psRev.setInt(1, revista.getIdDocumento());
                psRev.setString(2, revista.getIssn());
                psRev.setString(3, revista.getVolumen());
                psRev.setString(4, revista.getMesPublicacion());
                psRev.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertarCD(CD cd) {
        String sqlDoc = "INSERT INTO Documento (id_tipo_doc, titulo, autor, ubicacion_fisica, codigo_de_barras, estado) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCd = "INSERT INTO CD (id_documento, duracion_minutos, tipo_contenido) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS)) {
                psDoc.setInt(1, cd.getIdTipoDoc());
                psDoc.setString(2, cd.getTitulo());
                psDoc.setString(3, cd.getAutor());
                psDoc.setString(4, cd.getUbicacionFisica());
                psDoc.setString(5, cd.getCodigoDeBarras());
                psDoc.setString(6, cd.getEstado());
                psDoc.executeUpdate();

                try (ResultSet keys = psDoc.getGeneratedKeys()) {
                    if (keys.next()) { cd.setIdDocumento(keys.getInt(1)); }
                }
            }

            try (PreparedStatement psCd = conn.prepareStatement(sqlCd)) {
                psCd.setInt(1, cd.getIdDocumento());
                psCd.setInt(2, cd.getDuracionMinutos());
                psCd.setString(3, cd.getTipoContenido());
                psCd.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarLibro(Libro libro) {
        String sqlDoc = "UPDATE Documento SET titulo = ?, autor = ?, ubicacion_fisica = ?, codigo_de_barras = ?, estado = ? WHERE id_documento = ?";
        String sqlLib = "UPDATE Libro SET isbn = ?, editorial = ?, edicion = ? WHERE id_documento = ?";

        return ejecutarActualizacionEstructurada(libro, sqlDoc, sqlLib, ps -> {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getEditorial());
            ps.setString(3, libro.getEdicion());
            ps.setInt(4, libro.getIdDocumento());
        });
    }

    public boolean actualizarRevista(Revista revista) {
        String sqlDoc = "UPDATE Documento SET titulo = ?, autor = ?, ubicacion_fisica = ?, codigo_de_barras = ?, estado = ? WHERE id_documento = ?";
        String sqlRev = "UPDATE Revista SET issn = ?, volumen = ?, mes_publicacion = ? WHERE id_documento = ?";

        return ejecutarActualizacionEstructurada(revista, sqlDoc, sqlRev, ps -> {
            ps.setString(1, revista.getIssn());
            ps.setString(2, revista.getVolumen());
            ps.setString(3, revista.getMesPublicacion());
            ps.setInt(4, revista.getIdDocumento());
        });
    }

    public boolean actualizarCD(CD cd) {
        String sqlDoc = "UPDATE Documento SET titulo = ?, autor = ?, ubicacion_fisica = ?, codigo_de_barras = ?, estado = ? WHERE id_documento = ?";
        String sqlCd = "UPDATE CD SET duracion_minutos = ?, tipo_contenido = ? WHERE id_documento = ?";

        return ejecutarActualizacionEstructurada(cd, sqlDoc, sqlCd, ps -> {
            ps.setInt(1, cd.getDuracionMinutos());
            ps.setString(2, cd.getTipoContenido());
            ps.setInt(3, cd.getIdDocumento());
        });
    }

    @FunctionalInterface
    private interface MapeadorStatement { void aplicar(PreparedStatement ps) throws SQLException; }

    private boolean ejecutarActualizacionEstructurada(Documento doc, String sqlPadre, String sqlHijo, MapeadorStatement mapeador) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sqlPadre)) {
                ps.setString(1, doc.getTitulo());
                ps.setString(2, doc.getAutor());
                ps.setString(3, doc.getUbicacionFisica());
                ps.setString(4, doc.getCodigoDeBarras());
                ps.setString(5, doc.getEstado());
                ps.setInt(6, doc.getIdDocumento());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(sqlHijo)) {
                mapeador.aplicar(ps);
                ps.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        }
        return false;
    }

    public boolean eliminar(int idDocumento) {
        String[] sqls = {
                "DELETE FROM Ejemplar WHERE id_documento = ?",
                "DELETE FROM Libro WHERE id_documento = ?",
                "DELETE FROM Revista WHERE id_documento = ?",
                "DELETE FROM CD WHERE id_documento = ?",
                "DELETE FROM Documento WHERE id_documento = ?"
        };
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            for (String sql : sqls) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, idDocumento);
                    ps.executeUpdate();
                }
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
        }
        return false;
    }

    public Documento buscarPorId(int id) {
        String sql = "SELECT d.*, t.Nombre AS nombre_tipo, " +
                "l.isbn, l.editorial, l.edicion, " +
                "r.issn, r.volumen, r.mes_publicacion, " +
                "c.duracion_minutos, c.tipo_contenido " +
                "FROM Documento d " +
                "INNER JOIN TipoDocumento t ON d.id_tipo_doc = t.id_tipo_doc " +
                "LEFT JOIN Libro l ON d.id_documento = l.id_documento " +
                "LEFT JOIN Revista r ON d.id_documento = r.id_documento " +
                "LEFT JOIN CD c ON d.id_documento = c.id_documento " +
                "WHERE d.id_documento = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("nombre_tipo");
                    Documento doc;

                    if ("Libro".equalsIgnoreCase(tipo)) {
                        Libro l = new Libro();
                        l.setIsbn(rs.getString("isbn"));
                        l.setEditorial(rs.getString("editorial"));
                        l.setEdicion(rs.getString("edicion"));
                        doc = l;
                    } else if ("Revista".equalsIgnoreCase(tipo)) {
                        Revista r = new Revista();
                        r.setIssn(rs.getString("issn"));
                        r.setVolumen(rs.getString("volumen"));
                        r.setMesPublicacion(rs.getString("mes_publicacion"));
                        doc = r;
                    } else {
                        CD c = new CD();
                        c.setDuracionMinutos(rs.getInt("duracion_minutos"));
                        c.setTipoContenido(rs.getString("tipo_contenido"));
                        doc = c;
                    }

                    doc.setIdDocumento(rs.getInt("id_documento"));
                    doc.setIdTipoDoc(rs.getInt("id_tipo_doc"));
                    doc.setTitulo(rs.getString("titulo"));
                    doc.setAutor(rs.getString("autor"));
                    doc.setUbicacionFisica(rs.getString("ubicacion_fisica"));
                    doc.setCodigoDeBarras(rs.getString("codigo_de_barras"));
                    doc.setEstado(rs.getString("estado"));
                    return doc;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}