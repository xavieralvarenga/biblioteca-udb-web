package com.biblioteca.web.controller;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    // Redirige al formulario JSP cuando entran por GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/vistas/gestion_usuarios.jsp").forward(request, response);
    }

    // Procesa los formularios enviados por POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("registrar".equals(accion)) {
            procesarRegistro(request, response);
        } else if ("restablecer".equals(accion)) {
            procesarRestablecer(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios?error=AccionNoValida");
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
            nuevoUsuario.setPasswordHash(request.getParameter("password")); // Temporalmente plano

            boolean exito = usuarioService.registrarUsuario(nuevoUsuario);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/usuarios?msg=UsuarioRegistrado");
            } else {
                response.sendRedirect(request.getContextPath() + "/usuarios?error=ErrorAlRegistrar");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/usuarios?error=DatosInvalidos");
        }
    }

    private void procesarRestablecer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String carnet = request.getParameter("carnet");
        String nuevaPassword = request.getParameter("nuevaPassword");

        boolean exito = usuarioService.cambiarPassword(carnet, nuevaPassword);

        if (exito) {
            response.sendRedirect(request.getContextPath() + "/usuarios?msg=PasswordRestablecida");
        } else {
            response.sendRedirect(request.getContextPath() + "/usuarios?error=ErrorRestablecer");
        }
    }
}