package com.biblioteca.web.controller;

import com.biblioteca.web.dao.DocumentoDAO;
import com.biblioteca.web.model.Documento;
import com.biblioteca.web.model.Libro;
import com.biblioteca.web.model.Revista;
import com.biblioteca.web.model.CD;
import com.biblioteca.web.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Controlador Servlet unificado y polimórfico.
 * Procesa inserciones y actualizaciones dinámicamente según el idTipoDoc.
 * @author Xavier Larios
 * @version 6.0
 */
@WebServlet("/documentos")
public class DocumentoServlet extends HttpServlet {

    private final DocumentoDAO documentoDAO = new DocumentoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=SesionInvalida");
            return;
        }

        String accion = request.getParameter("accion");
        String rol = usuario.getNombreRol();

        boolean esAutorizado = "Administrador".equalsIgnoreCase(rol) || "Encargado (Bibliotecario)".equalsIgnoreCase(rol);

        // --- ACCIÓN: CARGAR DATOS EN EL FORMULARIO PARA EDITAR ---
        if ("cargarEditar".equals(accion)) {
            if (esAutorizado) {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Documento docEditar = documentoDAO.buscarPorId(id);

                    if (docEditar != null) {
                        request.setAttribute("docEditar", docEditar);
                        List<Documento> listaDocumentos = documentoDAO. listarTodos();
                        request.setAttribute("documentos", listaDocumentos);

                        request.getRequestDispatcher("/vistas/crud_documentos.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/documentos?error=DatosInvalidos");
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/documentos?error=AccesoDenegado");
                return;
            }
        }

        // --- ACCIÓN: ELIMINAR ---
        if ("eliminar".equals(accion)) {
            if (esAutorizado) {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    boolean eliminado = documentoDAO.eliminar(id);

                    if (eliminado) {
                        response.sendRedirect(request.getContextPath() + "/documentos?msg=EliminacionExitosa");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/documentos?error=FalloEliminacion");
                    }
                } catch (NumberFormatException e) {
                    response.sendRedirect(request.getContextPath() + "/documentos?error=DatosInvalidos");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/documentos?error=AccesoDenegado");
            }
            return;
        }

        // --- FLUJO PRINCIPAL: LISTAR FILTRADO ---
        if (accion == null || "listar".equals(accion)) {
            String buscar = request.getParameter("buscar");
            String tipo = request.getParameter("tipo");
            String estado = request.getParameter("estado");

            List<Documento> listaDocumentos;

            if ((buscar != null && !buscar.isBlank())
                    || (tipo != null && !tipo.isBlank())
                    || (estado != null && !estado.isBlank())) {

                listaDocumentos = documentoDAO.buscarDocumentos(
                        buscar,
                        tipo,
                        estado
                );

            } else {

                listaDocumentos = documentoDAO.listarTodos();
            }

            request.setAttribute("documentos", listaDocumentos);

            if (esAutorizado) {
                request.getRequestDispatcher("/vistas/crud_documentos.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/vistas/consulta_documentos.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null || !("Administrador".equalsIgnoreCase(usuario.getNombreRol()) || "Encargado (Bibliotecario)".equalsIgnoreCase(usuario.getNombreRol()))) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=AccesoDenegado");
            return;
        }

        String accion = request.getParameter("accion");

        try {
            int idTipoDoc = Integer.parseInt(request.getParameter("idTipoDoc"));

            // --- FLUJO DE INSERCIÓN UNIFICADO ---
            if ("insertar".equals(accion)) {
                boolean resultado = false;

                if (idTipoDoc == 1) { // Libro
                    Libro l = new Libro();
                    mapearCamposComunes(request, l);
                    l.setIsbn(request.getParameter("isbn"));
                    l.setEditorial(request.getParameter("editorial"));
                    l.setEdicion(request.getParameter("edicion"));
                    resultado = documentoDAO.insertarLibro(l);
                }
                else if (idTipoDoc == 2) { // Revista
                    Revista r = new Revista();
                    mapearCamposComunes(request, r);
                    r.setIssn(request.getParameter("issn"));
                    r.setVolumen(request.getParameter("volumen"));
                    r.setMesPublicacion(request.getParameter("mesPublicacion"));
                    resultado = documentoDAO.insertarRevista(r);
                }
                else if (idTipoDoc == 3) { // CD
                    CD c = new CD();
                    mapearCamposComunes(request, c);
                    c.setDuracionMinutos(Integer.parseInt(request.getParameter("duracionMinutos")));
                    c.setTipoContenido(request.getParameter("tipoContenido"));
                    resultado = documentoDAO.insertarCD(c);
                }

                redireccionarConResultado(response, request, resultado, "InsercionExitosa", "FalloPersistencia");
            }

            // --- FLUJO DE ACTUALIZACIÓN UNIFICADO ---
            else if ("actualizar".equals(accion)) {
                int idDocumento = Integer.parseInt(request.getParameter("idDocumento"));
                boolean resultado = false;

                if (idTipoDoc == 1) {
                    Libro l = new Libro();
                    l.setIdDocumento(idDocumento);
                    mapearCamposComunes(request, l);
                    l.setIsbn(request.getParameter("isbn"));
                    l.setEditorial(request.getParameter("editorial"));
                    l.setEdicion(request.getParameter("edicion"));
                    resultado = documentoDAO.actualizarLibro(l);
                }
                else if (idTipoDoc == 2) {
                    Revista r = new Revista();
                    r.setIdDocumento(idDocumento);
                    mapearCamposComunes(request, r);
                    r.setIssn(request.getParameter("issn"));
                    r.setVolumen(request.getParameter("volumen"));
                    r.setMesPublicacion(request.getParameter("mesPublicacion"));
                    resultado = documentoDAO.actualizarRevista(r);
                }
                else if (idTipoDoc == 3) {
                    CD c = new CD();
                    c.setIdDocumento(idDocumento);
                    mapearCamposComunes(request, c);
                    c.setDuracionMinutos(Integer.parseInt(request.getParameter("duracionMinutos")));
                    c.setTipoContenido(request.getParameter("tipoContenido"));
                    resultado = documentoDAO.actualizarCD(c);
                }

                redireccionarConResultado(response, request, resultado, "ActualizacionExitosa", "FalloActualizacion");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/documentos?error=DatosInvalidos");
        }
    }

    /**
     * Helper método para evitar duplicar código al extraer los datos compartidos de la clase padre
     */
    private void mapearCamposComunes(HttpServletRequest request, Documento doc) throws NumberFormatException {
        doc.setIdTipoDoc(Integer.parseInt(request.getParameter("idTipoDoc")));
        doc.setTitulo(request.getParameter("titulo"));
        doc.setAutor(request.getParameter("autor"));
        doc.setUbicacionFisica(request.getParameter("ubicacionFisica"));
        doc.setCodigoDeBarras(request.getParameter("codigoDeBarras"));
        doc.setEstado(request.getParameter("estado"));
    }

    private void redireccionarConResultado(HttpServletResponse response, HttpServletRequest request,
                                           boolean exito, String msgExito, String msgError) throws IOException {
        if (exito) {
            response.sendRedirect(request.getContextPath() + "/documentos?msg=" + msgExito);
        } else {
            response.sendRedirect(request.getContextPath() + "/documentos?error=" + msgError);
        }
    }
}