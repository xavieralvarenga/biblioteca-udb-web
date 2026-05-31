package com.biblioteca.web.service;

import com.biblioteca.web.dao.UsuarioDAO;
import com.biblioteca.web.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Autentica un usuario en el sistema verificando sus credenciales.
     * Garantiza la carga completa del perfil incluyendo su rol operativo.
     */
    public Usuario autenticar(String carnet, String password) {
        // 1. Buscamos el usuario por su carnet usando el método que hace INNER JOIN con TipoUsuario
        Usuario usuario = usuarioDAO.obtenerPorCarnet(carnet);

        // 2. Si el usuario existe, validamos la contraseña
        if (usuario != null) {
            // Nota: Si estás usando hashing en base de datos, aquí usarías BCrypt.checkpw
            if (usuario.getPasswordHash().equals(password)) {

                // === AQUÍ ESTABA EL TRUCO ===
                // Nos aseguramos de que el nombre del rol no viaje vacío a la sesión.
                // Si por alguna razón de la consulta viene nulo, lo preparamos dinámicamente.
                if (usuario.getNombreRol() == null || usuario.getNombreRol().trim().isEmpty()) {
                    if (usuario.getIdTipo() == 3) {
                        usuario.setNombreRol("Encargado (Bibliotecario)");
                    } else if (usuario.getIdTipo() == 2) {
                        usuario.setNombreRol("Profesor");
                    } else {
                        usuario.setNombreRol("Estudiante");
                    }
                }

                return usuario; // Retorna el usuario completamente estructurado
            }
        }

        return null; // Credenciales inválidas o usuario inactivo
    }

    // Tus otros métodos del Service (registrarUsuario, cambiarPassword, etc.)
    public boolean registrarUsuario(Usuario u) {
        return usuarioDAO.insertarUsuario(u);
    }

    public boolean cambiarPassword(String carnet, String nuevaPassword) {
        return usuarioDAO.restablecerPassword(carnet, nuevaPassword);
    }
}