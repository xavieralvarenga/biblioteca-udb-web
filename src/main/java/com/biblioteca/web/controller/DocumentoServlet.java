package com.biblioteca.web.controller;

import com.biblioteca.web.dao.DocumentoDAO;
import com.biblioteca.web.model.Documento;
import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.model.Libro;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet encargado de coordinar el inventario físico de ejemplares.
 * Centraliza las peticiones bajo la URL {@code /documentos} gestionando accesos asimétricos según roles.
 * * @author Xavier Larios
 */
@WebServlet("/documentos")
public class DocumentoServlet extends HttpServlet {

    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    /**
     * Resuelve la presentación visual de documentos.
     * Si el rol es 'Encargado' se despacha la vista del CRUD total; si es Alumno/Profesor,
     * se deriva con permisos restringidos hacia la interfaz exclusiva de Consultas.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // Medida perimetral de contingencia por si falla el Filtro global
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<Documento> listaDocumentos = documentoDAO.listarTodos();
        request.setAttribute("documentos", listaDocumentos);

        // Control de bifurcación de Vistas por Regla de Negocio
        if ("Administrador".equalsIgnoreCase(usuario.getNombreRol())) {
            // Carga la interfaz administrativa completa (CRUD)
            request.getRequestDispatcher("/vistas/crud_documentos.jsp").forward(request, response);
        } else {
            // Carga la interfaz de solo consulta pública para Alumnos y Profesores
            request.getRequestDispatcher("/vistas/consulta_documentos.jsp").forward(request, response);
        }
    }

    /**
     * Procesa las operaciones de mutación (Creación, Modificación, Eliminación).
     * Restringe el procesamiento de peticiones exclusivamente a usuarios Administradores.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        // Blindaje: Si un estudiante intenta enviar una petición POST maliciosa (HTTP manipulado)
        if (usuario == null || !"Administrador".equalsIgnoreCase(usuario.getNombreRol())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Privilegios insuficientes.");
            return;
        }

        String accion = request.getParameter("accion");

        if ("registrarLibro".equals(accion)) {
            Libro libro = new Libro();
            libro.setTitulo(request.getParameter("titulo"));
            libro.setAnioPublicacion(Integer.parseInt(request.getParameter("anio")));
            libro.setCantidadEjemplares(Integer.parseInt(request.getParameter("ejemplares")));
            libro.setIsbn(request.getParameter("isbn"));
            libro.setAutor(request.getParameter("autor"));
            libro.setNumeroPaginas(Integer.parseInt(request.getParameter("paginas")));
            libro.setEditorial(request.getParameter("editorial"));

            documentoDAO.insertarLibro(libro);
        }

        response.sendRedirect(request.getContextPath() + "/documentos");
    }
}