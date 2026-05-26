package com.biblioteca.web.filter;

import com.biblioteca.web.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro de seguridad perimetral encargado de restringir el ingreso no autorizado
 * al Módulo de Encargados y a las vistas protegidas del sistema.
 * * @author Xavier Larios
 * @version 1.1
 */
@WebFilter(urlPatterns = {"/usuarios", "/menu", "/documentos", "/vistas/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * Intercepta las solicitudes web para validar el estado de la sesión.
     * Si no se encuentra un usuario logueado en el sistema, deniega el paso
     * redirigiéndolo de forma mandatoria hacia el formulario raíz login.jsp.
     *
     * @param request La solicitud entrante al servidor.
     * @param response La respuesta saliente del servidor.
     * @param chain La cadena de ejecución de filtros de Tomcat.
     * @throws IOException Si ocurre un fallo en el pipeline de comunicación.
     * @throws ServletException Si ocurre una excepción interna de los servlets.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            // Intercepta e impide el acceso, redirigiendo a la raíz del proyecto
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=InicieSesion");
        } else {
            // Permite continuar el flujo ordinario hacia el recurso web solicitado
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}
}