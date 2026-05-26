package com.biblioteca.web.controller;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Controlador Servlet encargado de gestionar el inicio y cierre de sesión de la aplicación.
 * Redirige a los usuarios autenticados hacia el menú principal de selección.
 * * @author Xavier Larios
 * @version 1.2
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=SesionCerrada");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carnet = request.getParameter("carnet");
        String password = request.getParameter("password");

        Usuario usuarioAutenticado = usuarioService.autenticar(carnet, password);

        if (usuarioAutenticado != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado);

            // TODO: Redirección unificada hacia el controlador del menú principal
            response.sendRedirect(request.getContextPath() + "/menu");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=CredencialesIncorrectas");
        }
    }
}