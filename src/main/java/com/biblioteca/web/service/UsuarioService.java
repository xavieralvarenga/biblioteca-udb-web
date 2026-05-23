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

    /**
     * Valida las credenciales de un usuario para permitir el ingreso al sistema.
     * * @param carnet El carnet proporcionado por el usuario.
     * @param password La contraseña en texto plano ingresada en el formulario.
     * @return El objeto {@link Usuario} autenticado si las credenciales son válidas;
     * {@code null} si las credenciales son incorrectas o el usuario no existe.
     */
    public Usuario autenticar(String carnet, String password) {
        if (carnet == null || carnet.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        Usuario usuario = usuarioDAO.obtenerPorCarnet(carnet);

        if (usuario != null) {
            // Nota: En producción, comparar el hash usando BCrypt.checkpw(password, usuario.getPasswordHash())
            if (usuario.getPasswordHash().equals(password)) {
                return usuario;
            }
        }
        return null;
    }
}
