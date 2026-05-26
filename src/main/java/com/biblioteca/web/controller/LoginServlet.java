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
 * Intercepta las solicitudes de autenticación mapeadas en la URL {@code /login}.
 * * @author Xavier Larios
 * @version 1.1
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Muestra la interfaz del formulario de Login o procesa la destrucción de la sesión activa (Logout).
     * Ajustado para localizar el archivo login.jsp en la raíz de la carpeta webapp.
     *
     * @param request La solicitud HTTP entrante.
     * @param response La respuesta HTTP saliente.
     * @throws ServletException Si ocurre un error en la transición interna del contenedor web.
     * @throws IOException Si se genera un fallo de Entrada/Salida durante las redirecciones.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        // Manejo del Cierre de Sesión (Logout)
        if ("logout".equals(accion)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Destruye por completo los datos en memoria de la sesión
            }
            // Redirección hacia login.jsp ubicado en la raíz de la web app
            response.sendRedirect(request.getContextPath() + "/login.jsp?msg=SesionCerrada");
            return;
        }

        // Despacha la petición a login.jsp en la raíz de webapp
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    /**
     * Procesa los parámetros de autenticación enviados desde el formulario mediante el método POST.
     * Realiza una redirección condicional hacia la zona administrativa si el usuario posee rol de Encargado.
     *
     * @param request La solicitud HTTP contenedora de las credenciales del usuario.
     * @param response La respuesta HTTP destinada al redireccionamiento.
     * @throws ServletException Si ocurre un fallo interno del componente.
     * @throws IOException Si ocurre un error en la ruta de redirección.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String carnet = request.getParameter("carnet");
        String password = request.getParameter("password");

        Usuario usuarioAutenticado = usuarioService.autenticar(carnet, password);

        if (usuarioAutenticado != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado);

            // Redirección condicional según las reglas del Módulo de Encargados
            if ("Administrador".equalsIgnoreCase(usuarioAutenticado.getNombreRol())) {
                response.sendRedirect(request.getContextPath() + "/usuarios");
            } else {
                // Estudiantes o profesores que no son administradores (Próxima fase)
                response.sendRedirect(request.getContextPath() + "/vistas/index_general.jsp");
            }
        } else {
            // Credenciales inválidas, regresa a login.jsp en la raíz con bandera de error
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=CredencialesIncorrectas");
        }
    }
}