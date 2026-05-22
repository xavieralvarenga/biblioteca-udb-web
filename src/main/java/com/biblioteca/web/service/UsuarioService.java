package com.biblioteca.web.service;

import com.biblioteca.web.dao.UsuarioDAO;
import com.biblioteca.web.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Lógica para registrar usuario
    public boolean registrarUsuario(Usuario usuario) {
        // Regla de negocio inicial: forzar que todo usuario nuevo empiece activo y sin mora
        usuario.setEstado("Activo");
        usuario.setEstadoMora(false);

        return usuarioDAO.insertarUsuario(usuario);
    }

    // Lógica para restablecer contraseña
    public boolean cambiarPassword(String carnet, String nuevaPassword) {
        if (carnet == null || carnet.trim().isEmpty() || nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            return false;
        }

        return usuarioDAO.restablecerPassword(carnet, nuevaPassword);
    }
}
