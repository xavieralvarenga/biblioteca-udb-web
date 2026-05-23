package com.biblioteca.web.controller;

import com.biblioteca.web.model.Usuario;
import com.biblioteca.web.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controlador Servlet encargado de gestionar el inicio y cierre de sesión de la aplicación.
 * Mapea las solicitudes de autenticación en la URL {@code /login}.
 * * @author Xavier Larios
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Procesa las peticiones GET para el control de sesiones, específicamente la acción de logout.
     * * @param request  la solicitud HTTP que contiene el parámetro opcional de acción.
     * @param response la respuesta HTTP para ejecutar la redirección al login.
     * @throws ServletException si ocurre un error en la transición del servlet.
     * @throws IOException      si el archivo de destino no es accesible.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if ("logout".equals(accion)) {
            // Recupera la sesión actual si existe, sin crear una nueva (false)
            HttpSession session = request.getSession(false);
            if (session != null) {
                // Remueve todos los atributos y destruye la sesión por completo en el servidor
                session.invalidate();
            }
            // Redirige al login anexando un mensaje de éxito para que sea capturado en la interfaz
            response.sendRedirect(request.getContextPath() + "/vistas/login.jsp?msg=SesionCerrada");
            return;
        }

        request.getRequestDispatcher("/vistas/login.jsp").forward(request, response);
    }

    /**
     * Procesa los datos del formulario de autenticación enviados mediante POST.
     *
     * @param request La solicitud HTTP con los parámetros de carnet y contraseña.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error interno.
     * @throws IOException Si ocurre un error de redirección.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carnet = request.getParameter("carnet");
        String password = request.getParameter("password");

        Usuario usuarioAutenticado = usuarioService.autenticar(carnet, password);

        if (usuarioAutenticado != null) {
            // Se crea la sesión HTTP y guardamos el objeto usuario en memoria
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado);

            // Redirección condicional según las reglas del requerimiento (Módulo Encargados)
            if ("Administrador".equalsIgnoreCase(usuarioAutenticado.getNombreRol())) {
                response.sendRedirect(request.getContextPath() + "/usuarios"); // Va a la zona de administración
                System.out.println("Usuario " + usuarioAutenticado.getNombres() + " " + usuarioAutenticado.getApellidos() + " ha iniciado sesión como Administrador.");
            } else {
                // Si entra un estudiante o profesor, lo mandamos al módulo de consultas (A crear en el futuro)
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                System.out.println("Usuario con datos erroneos ha intentado iniciar sesión: " + carnet);
            }
        } else {
            // Credenciales incorrectas, regresa al login con bandera de error
            response.sendRedirect(request.getContextPath() + "/vistas/login.jsp?error=CredencialesIncorrectas");
        }
    }


}