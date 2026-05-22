package com.biblioteca.web.dao;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
}