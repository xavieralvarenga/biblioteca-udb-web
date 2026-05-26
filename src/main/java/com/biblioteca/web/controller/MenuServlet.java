package com.biblioteca.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * Servlet encargado de despachar el menú principal de la aplicación.
 * Sirve como punto de entrada centralizado post-autenticación.
 * * @author Xavier Larios
 * @version 1.0
 */
@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    /**
     * Muestra la vista del menú de selección de módulos.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el despacho del JSP.
     * @throws IOException      Si ocurre un error de Entrada/Salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige al archivo de menú ubicado dentro de la subcarpeta segura de vistas
        request.getRequestDispatcher("/vistas/menu.jsp").forward(request, response);
    }
}
