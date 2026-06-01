package com.biblioteca.web.controller;

import com.biblioteca.web.dao.UsuarioDAO;
import com.biblioteca.web.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ConfiguracionLimitesServlet", urlPatterns = {"/ConfiguracionLimites"})
public class ConfiguracionLimitesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. SEGURIDAD: Solo Administradores
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        if (usuario == null || !"Administrador".equalsIgnoreCase(usuario.getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/menu?error=AccesoDenegado");
            return;
        }

        // 2. Traer los límites actuales y enviarlos a la vista
        UsuarioDAO dao = new UsuarioDAO();
        List<Object[]> limitesRoles = dao.obtenerLimitesPrestatarios();

        request.setAttribute("limitesRoles", limitesRoles);
        request.getRequestDispatcher("/vistas/Configuracion/LimitesPrestamo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. SEGURIDAD: Solo Administradores
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        if (usuario == null || !"Administrador".equalsIgnoreCase(usuario.getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/menu?error=AccesoDenegado");
            return;
        }

        try {
            // Recibimos los arreglos desde los inputs del formulario
            String[] ids = request.getParameterValues("idTipo");
            String[] libros = request.getParameterValues("maxLibros");
            String[] dias = request.getParameterValues("maxDias");

            UsuarioDAO dao = new UsuarioDAO();
            boolean exitoTotal = true;

            // Recorremos los arreglos para actualizar todos los roles que llegaron
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    int idTipo = Integer.parseInt(ids[i]);
                    int maxLibros = Integer.parseInt(libros[i]);
                    int maxDias = Integer.parseInt(dias[i]);

                    if (!dao.actualizarLimitesPorRol(idTipo, maxLibros, maxDias)) {
                        exitoTotal = false;
                    }
                }
            }

            if (exitoTotal) {
                response.sendRedirect(request.getContextPath() + "/ConfiguracionLimites?msg=ActualizacionExitosa");
            } else {
                response.sendRedirect(request.getContextPath() + "/ConfiguracionLimites?error=FalloActualizacion");
            }

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/ConfiguracionLimites?error=DatosInvalidos");
        }
    }
}