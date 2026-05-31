package com.biblioteca.web.dao;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        // JOIN con TipoUsuario para traer el nombre del rol (Estudiante, Profesor, Encargado, etc.)
        String sql = "SELECT u.*, t.nombre_rol FROM Usuarios u " +
                "INNER JOIN TipoUsuario t ON u.id_tipo = t.id_tipo " +
                "WHERE u.Estado = 'Activo'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("ID_Usuario"));
                usuario.setIdTipo(rs.getInt("id_tipo"));
                usuario.setNombres(rs.getString("Nombres"));
                usuario.setApellidos(rs.getString("Apellidos"));
                usuario.setCarnetDocenteAlumno(rs.getString("carnet_docente_alumno"));
                usuario.setPasswordHash(rs.getString("password_hash"));
                usuario.setEstadoMora(rs.getBoolean("estado_mora"));
                usuario.setEstado(rs.getString("Estado"));
                usuario.setNombreRol(rs.getString("nombre_rol"));

                lista.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Usuario obtenerPorId(int idUsuario) {
        String sql = "SELECT u.*, t.nombre_rol FROM Usuarios u " +
                "INNER JOIN TipoUsuario t ON u.id_tipo = t.id_tipo " +
                "WHERE u.ID_Usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("ID_Usuario"));
                    usuario.setIdTipo(rs.getInt("id_tipo"));
                    usuario.setNombres(rs.getString("Nombres"));
                    usuario.setApellidos(rs.getString("Apellidos"));
                    usuario.setCarnetDocenteAlumno(rs.getString("carnet_docente_alumno"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setEstadoMora(rs.getBoolean("estado_mora"));
                    usuario.setEstado(rs.getString("Estado"));
                    usuario.setNombreRol(rs.getString("nombre_rol"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (id_tipo, Nombres, Apellidos, carnet_docente_alumno, password_hash, estado_mora, Estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdTipo());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getCarnetDocenteAlumno());
            ps.setString(5, usuario.getPasswordHash());
            ps.setBoolean(6, usuario.isEstadoMora());
            ps.setString(7, usuario.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET id_tipo=?, Nombres=?, Apellidos=?, carnet_docente_alumno=?, password_hash=?, Estado=? WHERE ID_Usuario=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuario.getIdTipo());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getCarnetDocenteAlumno());
            ps.setString(5, usuario.getPasswordHash());
            ps.setString(6, usuario.getEstado());
            ps.setInt(7, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        // Opción segura: Cambio de estado a 'Inactivo' para no romper registros de préstamos históricos
        String sql = "UPDATE Usuarios SET Estado = 'Inactivo' WHERE ID_Usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean restablecerPassword(String carnet, String nuevoPasswordHash) {
        String sql = "UPDATE Usuarios SET password_hash = ? WHERE carnet_docente_alumno = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoPasswordHash);
            ps.setString(2, carnet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerPorCarnet(String carnet) {
        String sql = "SELECT u.*, t.nombre_rol FROM Usuarios u " +
                "INNER JOIN TipoUsuario t ON u.id_tipo = t.id_tipo " +
                "WHERE u.carnet_docente_alumno = ? AND u.Estado = 'Activo'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carnet);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("ID_Usuario"));
                    usuario.setIdTipo(rs.getInt("id_tipo"));
                    usuario.setNombres(rs.getString("Nombres"));
                    usuario.setApellidos(rs.getString("Apellidos"));
                    usuario.setCarnetDocenteAlumno(rs.getString("carnet_docente_alumno"));
                    usuario.setPasswordHash(rs.getString("password_hash"));
                    usuario.setEstadoMora(rs.getBoolean("estado_mora"));
                    usuario.setEstado(rs.getString("Estado"));
                    usuario.setNombreRol(rs.getString("nombre_rol"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}