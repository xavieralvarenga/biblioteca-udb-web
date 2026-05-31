package com.biblioteca.web.filter;

import com.biblioteca.web.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro de seguridad perimetral encargado de verificar la existencia de una sesión
 * activa antes de permitir el acceso a los recursos protegidos del sistema.
 * * @author Xavier Larios
 * @version 2.0
 */
// 1. Asegúrate de incluir '/menu' y asegurar la subcarpeta '/vistas/*'
@WebFilter(urlPatterns = {"/menu", "/documentos", "/usuarios","/Prestamos", "/ConfiguracionMora", "/vistas/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización opcional si se requiere
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        HttpSession session = httpRequest.getSession(false);

        // 2. Definir rutas públicas que NO necesitan autenticación para evitar bucles
        boolean esPaginaLogin = requestURI.endsWith("login.jsp");
        boolean esServletLogin = requestURI.endsWith("/login");
        boolean esRecursoEstatico = requestURI.contains("/assets/") || requestURI.contains("/css/") || requestURI.contains("/js/") || requestURI.contains("/imagenes/");
        

        // Verificar si el usuario ya inició sesión
        Usuario usuarioLogueado = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuarioLogueado != null || esPaginaLogin || esServletLogin || esRecursoEstatico) {
            // Si está logueado o intenta entrar al login legítimamente, se le permite el paso
            chain.doFilter(request, response);
        } else {
            // Si no está logueado y quiere forzar la URL (ej: /menu o /documentos), directo al login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=AccesoDenegado");
        }
    }

    @Override
    public void destroy() {
        // Limpieza de recursos si es necesario
    }
}