package com.biblioteca.web.dao;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // 1. Registrar un nuevo usuario
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

    // 2. Restablecer la contraseña de un usuario usando su carnet
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

    /**
     * Busca un usuario en la base de datos a partir de su carnet de identificación.
     * Este método es fundamental para los procesos de autenticación (Login).
     *
     * @param carnet El carnet único del docente o alumno (identificador de usuario).
     * @return Un objeto {@link Usuario} si es encontrado; {@code null} en caso contrario o si ocurre un error.
     */
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
                    usuario.setNombreRol(rs.getString("nombre_rol")); // Atributo extra de conveniencia
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}