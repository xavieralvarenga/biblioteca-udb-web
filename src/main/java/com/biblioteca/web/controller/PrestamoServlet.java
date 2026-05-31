package com.biblioteca.web.controller;

import com.biblioteca.web.dao.UsuarioDAO;
import com.biblioteca.web.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.biblioteca.web.dao.PrestamoDAO;
import com.biblioteca.web.model.Prestamos;

@WebServlet(name = "PrestamoServlet", urlPatterns = {"/Prestamos"})
public class PrestamoServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "listar";

        try {
            switch (accion) {
                case "nuevo":
                    List<Usuario> listaUsuario = new UsuarioDAO().obtenerTodosLosUsuarios();
                    request.setAttribute("listaUsuario", listaUsuario);
                    List<com.biblioteca.web.model.Ejemplar> listaEjemplares = new com.biblioteca.web.dao.EjemplarDAO().obtenerEjemplaresDisponibles();
                    request.setAttribute("listaEjemplares", listaEjemplares);
                    request.getRequestDispatcher("/vistas/Prestamos/NuevoPrestamo.jsp").forward(request, response);
                    break;
                case "detalle":
                    request.getRequestDispatcher("/vistas/Prestamos/DetallePrestamo.jsp").forward(request, response);
                    break;
                case "devolver":
                    // 1. Atrapamos el ID del préstamo de la URL
                    int idPrestamoDev = Integer.parseInt(request.getParameter("id"));
                    
                    // 2. Traemos los detalles y la mora desde el DAO
                    List<Object[]> detallesDevolucion = new PrestamoDAO().obtenerDetallesParaDevolucion(idPrestamoDev);
                    
                    // 3. Calculamos el total de la mora sumando el valor de cada libro (índice 7 del Object[])
                    double totalMora = 0;
                    for(Object[] fila : detallesDevolucion) {
                        totalMora += (double) fila[7];
                    }
                    
                    // 4. Empaquetamos todo para la vista
                    request.setAttribute("detallesDevolucion", detallesDevolucion);
                    request.setAttribute("idPrestamo", idPrestamoDev);
                    request.setAttribute("totalMora", totalMora);
                    
                    request.getRequestDispatcher("/vistas/Prestamos/DevolucionPrestamo.jsp").forward(request, response);
                    break;
                case "listar":
                default:
                    List<Prestamos> listaDePrestamos = new PrestamoDAO().obtenerTodosLosPrestamos();
                    request.setAttribute("listaDePrestamos", listaDePrestamos);
                    request.getRequestDispatcher("/vistas/Prestamos/index.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Error en el GET de PrestamoServlet: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/Views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("guardar".equals(accion)) {
                
                // 1. Atrapamos el ID del Lector (Viene del input hidden)
                String idUsuarioStr = request.getParameter("idUsuario");
                if (idUsuarioStr == null || idUsuarioStr.isEmpty()) {
                    throw new Exception("Debes seleccionar un lector.");
                }
                int idUsuario = Integer.parseInt(idUsuarioStr);

                // 2. Definimos las fechas
                java.time.LocalDate fechaPrestamo = java.time.LocalDate.now(); // La fecha de hoy
                java.time.LocalDate fechaRegreso = java.time.LocalDate.parse(request.getParameter("fechaRegreso"));

                // 3. Atrapamos TODOS los libros del carrito
                // Usamos getParameterValues porque hay múltiples inputs con el name="idsEjemplares"
                String[] arrayIdsEjemplares = request.getParameterValues("idsEjemplares");
                
                if (arrayIdsEjemplares == null || arrayIdsEjemplares.length == 0) {
                    throw new Exception("El carrito está vacío. Agrega al menos un material.");
                }

                // 4. Convertimos los Arrays a las Listas que espera tu DAO
                List<Integer> idsEjemplaresList = new java.util.ArrayList<>();
                List<java.time.LocalDate> fechasLimitesList = new java.util.ArrayList<>();

                for (String idStr : arrayIdsEjemplares) {
                    idsEjemplaresList.add(Integer.parseInt(idStr));
                    // Como en la web pedimos una sola fecha global para todo el carrito, 
                    // la repetimos para cada libro en la lista
                    fechasLimitesList.add(fechaRegreso); 
                }

                // 5. Enviamos todo a la base de datos a través del DAO
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                boolean exito = prestamoDAO.registrarNuevoPrestamo(idUsuario, fechaPrestamo, idsEjemplaresList, fechasLimitesList);

                // 6. Redirigimos según el resultado
                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/Prestamos?accion=listar&mensaje=prestamo_ok");
                } else {
                    throw new Exception("No se pudo registrar el préstamo en la base de datos.");
                }

            } else if ("devolver".equals(accion)) {
                // 1. Atrapamos los datos del formulario de pago
                int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
                double montoPagado = Double.parseDouble(request.getParameter("montoPagado"));
                String observaciones = request.getParameter("observaciones");

                // 2. Enviamos al DAO
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                boolean exito = prestamoDAO.procesarDevolucionCompleta(idPrestamo, montoPagado, observaciones);

                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/Prestamos?accion=listar&mensaje=devolucion_ok");
                } else {
                    throw new Exception("Error interno al procesar la devolución en la base de datos.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error en el POST de PrestamoServlet: " + e.getMessage());
            // Si hay error, lo mandamos de regreso al formulario con el mensaje
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/vistas/Prestamos/NuevoPrestamo.jsp").forward(request, response);
        }
    }
}