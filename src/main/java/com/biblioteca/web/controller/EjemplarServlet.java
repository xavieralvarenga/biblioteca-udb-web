package com.biblioteca.web.controller;

import com.biblioteca.web.dao.DocumentoDAO;
import com.biblioteca.web.dao.EjemplarDAO;
import com.biblioteca.web.model.Documento;
import com.biblioteca.web.model.Ejemplar;
import com.biblioteca.web.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ejemplares")
public class EjemplarServlet extends HttpServlet {

    private final EjemplarDAO ejemplarDAO = new EjemplarDAO();
    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Proteger la ruta controlando que el usuario esté logueado
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionInvalida");
            return;
        }

        // 2. Obtener parámetros obligatorios
        String accion = request.getParameter("accion");
        String idDocParam = request.getParameter("idDocumento");

        if (idDocParam == null || idDocParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/documentos?error=FaltaIdDocumento");
            return;
        }

        int idDocumento = Integer.parseInt(idDocParam);

        // 3. Capturar y ejecutar la acción de eliminar si se solicita
        if ("eliminar".equals(accion)) {
            String idEjParam = request.getParameter("idEjemplar");
            if (idEjParam != null && !idEjParam.trim().isEmpty()) {
                int idEjemplar = Integer.parseInt(idEjParam);
                ejemplarDAO.eliminar(idEjemplar);
                response.sendRedirect(request.getContextPath() + "/ejemplares?idDocumento=" + idDocumento + "&msg=EjemplarEliminado");
                return;
            }
        }

        // 4. Flujo por Defecto: Cargar los datos del documento padre y listar sus ejemplares físicos
        Documento docPadre = documentoDAO.buscarPorId(idDocumento);
        if (docPadre == null) {
            response.sendRedirect(request.getContextPath() + "/documentos?error=DocumentoNoEncontrado");
            return;
        }

        List<Ejemplar> listaEjemplares = ejemplarDAO.listarPorDocumento(idDocumento);

        // 5. Enviar los datos al scope de la petición para la vista JSP
        request.setAttribute("docPadre", docPadre);
        request.setAttribute("ejemplares", listaEjemplares);

        // Redirección interna a la vista de gestión
        request.getRequestDispatcher("/vistas/gestion_ejemplares.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forzar codificación UTF-8 para evitar problemas con tildes y caracteres especiales
        request.setCharacterEncoding("UTF-8");

        // 1. Validar sesión en peticiones POST
        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null || !"Administrador".equals(usuario.getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=NoAutorizado");
            return;
        }

        // 2. Recuperar parámetros del formulario
        int idDocumento = Integer.parseInt(request.getParameter("idDocumento"));
        String codigoDeBarras = request.getParameter("codigoDeBarras");
        String estado = request.getParameter("estado");

        // 3. Construir el objeto modelo con las columnas reales de tu BD
        Ejemplar nuevoEjemplar = new Ejemplar();
        nuevoEjemplar.setIdDocumento(idDocumento);
        nuevoEjemplar.setCodigoDeBarras(codigoDeBarras);
        nuevoEjemplar.setEstado(estado);

        // 4. Insertar en la Base de Datos a través del DAO
        boolean insertado = ejemplarDAO.insertar(nuevoEjemplar);

        // 5. Redireccionar devolviendo el ID del documento para que la vista vuelva a cargarse correctamente
        if (insertado) {
            response.sendRedirect(request.getContextPath() + "/ejemplares?idDocumento=" + idDocumento + "&msg=EjemplarRegistrado");
        } else {
            response.sendRedirect(request.getContextPath() + "/ejemplares?idDocumento=" + idDocumento + "&error=FalloInsercion");
        }
    }
}