package com.biblioteca.web.controller;

import com.biblioteca.web.dao.UsuarioDAO;
import com.biblioteca.web.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        if ("cargarEditar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario uEditar = usuarioDAO.obtenerPorId(id);
            if (uEditar == null) {
                uEditar = usuarioDAO.obtenerPorId(id);
            }
            request.setAttribute("userEditar", uEditar);
        } else if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            usuarioDAO.eliminarUsuario(id);
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&msg=UsuarioEliminado");
            return;
        }

        // Siempre recargamos la lista para mostrarla en la tabla inferior
        List<Usuario> listaUsuarios = usuarioDAO.obtenerTodosLosUsuarios();
        request.setAttribute("usuarios", listaUsuarios);

        request.getRequestDispatcher("/vistas/gestion_usuarios.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {
            procesarRegistro(request, response);
        } else if ("actualizar".equals(accion)) {
            procesarActualizacion(request, response);
        } else if ("restablecer".equals(accion)) {
            procesarRestablecer(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar");
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setIdTipo(Integer.parseInt(request.getParameter("idTipo")));
            nuevoUsuario.setNombres(request.getParameter("nombres"));
            nuevoUsuario.setApellidos(request.getParameter("apellidos"));
            nuevoUsuario.setCarnetDocenteAlumno(request.getParameter("carnet"));

            String passwordInput = request.getParameter("password");
            nuevoUsuario.setPasswordHash(passwordInput != null && !passwordInput.isEmpty() ? passwordInput : "123456");
            nuevoUsuario.setEstadoMora(false);
            nuevoUsuario.setEstado("Activo");

            boolean exito = usuarioDAO.insertarUsuario(nuevoUsuario);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&msg=UsuarioRegistrado");
            } else {
                response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&error=ErrorAlRegistrar");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&error=DatosInvalidos");
        }
    }

    private void procesarActualizacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
            usuario.setIdTipo(Integer.parseInt(request.getParameter("idTipo")));
            usuario.setNombres(request.getParameter("nombres"));
            usuario.setApellidos(request.getParameter("apellidos"));
            usuario.setCarnetDocenteAlumno(request.getParameter("carnet"));
            usuario.setPasswordHash(request.getParameter("password"));
            usuario.setEstado("Activo");

            boolean exito = usuarioDAO.actualizarUsuario(usuario);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&msg=UsuarioActualizado");
            } else {
                response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&error=ErrorAlRegistrar");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&error=DatosInvalidos");
        }
    }

    private void procesarRestablecer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Corregido: Mismo nombre de parámetro que el input del formulario JSP ("carnet")
        String carnet = request.getParameter("carnet");
        String nuevaPassword = request.getParameter("nuevaPassword");

        boolean exito = usuarioDAO.restablecerPassword(carnet, nuevaPassword);

        if (exito) {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&msg=PasswordRestablecida");
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios?accion=listar&error=ErrorRestablecer");
        }
    }
}