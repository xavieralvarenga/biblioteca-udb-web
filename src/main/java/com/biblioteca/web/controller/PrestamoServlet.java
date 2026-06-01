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
                    int idParaDetalle = Integer.parseInt(request.getParameter("id"));
                    
                    PrestamoDAO daoDetalle = new PrestamoDAO();
                    Prestamos cabecera = daoDetalle.obtenerPrestamoPorId(idParaDetalle);
                    List<Object[]> listaDetalles = daoDetalle.obtenerDetallesPorPrestamo(idParaDetalle);

                    request.setAttribute("prestamoCabecera", cabecera);
                    request.setAttribute("listaDetalles", listaDetalles);
                    
                    request.getRequestDispatcher("/vistas/Prestamos/DetallePrestamo.jsp").forward(request, response);
                    break;
                case "devolver":
                    int idPrestamoDev = Integer.parseInt(request.getParameter("id"));

                    List<Object[]> detallesDevolucion = new PrestamoDAO().obtenerDetallesParaDevolucion(idPrestamoDev);

                    double totalMora = 0;
                    for (Object[] fila : detallesDevolucion) {
                        totalMora += (double) fila[7];
                    }

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

                String idUsuarioStr = request.getParameter("idUsuario");
                if (idUsuarioStr == null || idUsuarioStr.isEmpty()) {
                    throw new Exception("Debes seleccionar un lector.");
                }
                int idUsuario = Integer.parseInt(idUsuarioStr);
                java.time.LocalDate fechaPrestamo = java.time.LocalDate.now(); // La fecha de hoy
                java.time.LocalDate fechaRegreso = java.time.LocalDate.parse(request.getParameter("fechaRegreso"));

                String[] arrayIdsEjemplares = request.getParameterValues("idsEjemplares");

                if (arrayIdsEjemplares == null || arrayIdsEjemplares.length == 0) {
                    throw new Exception("El carrito está vacío. Agrega al menos un material.");
                }
                Usuario lector = new UsuarioDAO().obtenerPorIdDetalle(idUsuario);
                long diasSolicitados = java.time.temporal.ChronoUnit.DAYS.between(fechaPrestamo, fechaRegreso);
                if (diasSolicitados > lector.getMaxDiasPrestamo()) {
                    throw new Exception("Regla excedida: El perfil '" + lector.getNombreRol() +
                            "' solo puede pedir materiales por un máximo de " + lector.getMaxDiasPrestamo() + " días.");
                }

                int librosYaPrestados = new PrestamoDAO().contarLibrosActivosPorUsuario(idUsuario);
                int librosEnCarrito = arrayIdsEjemplares.length;

                if ((librosYaPrestados + librosEnCarrito) > lector.getMaxLibrosPermitidos()) {
                    throw new Exception("Límite superado: Un '" + lector.getNombreRol() + "' solo puede tener " +
                            lector.getMaxLibrosPermitidos() + " libros en total. " +
                            "Actualmente tiene " + librosYaPrestados + " sin devolver y quiere llevarse " + librosEnCarrito + ".");
                }

                List<Integer> idsEjemplaresList = new java.util.ArrayList<>();
                List<java.time.LocalDate> fechasLimitesList = new java.util.ArrayList<>();

                for (String idStr : arrayIdsEjemplares) {
                    idsEjemplaresList.add(Integer.parseInt(idStr));
                    fechasLimitesList.add(fechaRegreso);
                }
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                boolean exito = prestamoDAO.registrarNuevoPrestamo(idUsuario, fechaPrestamo, idsEjemplaresList, fechasLimitesList);

                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/Prestamos?accion=listar&mensaje=prestamo_ok");
                } else {
                    throw new Exception("No se pudo registrar el préstamo en la base de datos.");
                }

            } else if ("devolver".equals(accion)) {

                int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
                String observaciones = request.getParameter("observaciones");

                String tipoPago = request.getParameter("tipoPago");
                boolean pagoCompleto = "completo".equals(tipoPago);

                PrestamoDAO prestamoDAO = new PrestamoDAO();
                boolean exito = prestamoDAO.procesarDevolucionCompleta(idPrestamo, pagoCompleto, observaciones);

                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/Prestamos?accion=listar&mensaje=devolucion_ok");
                } else {
                    throw new Exception("Error interno al procesar la devolución.");
                }

            } else if ("pagarDeuda".equals(accion)) {

                int idPrestamo = Integer.parseInt(request.getParameter("idPrestamo"));
                boolean exito = new PrestamoDAO().pagarDeudaPrestamo(idPrestamo);

                if (exito) {
                    response.sendRedirect(request.getContextPath() + "/Prestamos?accion=listar&mensaje=pago_ok");
                } else {
                    throw new Exception("Error al procesar el pago de la deuda.");
                }
            }

        } catch (Exception e) { 
            System.err.println("Error en el POST de PrestamoServlet: " + e.getMessage());

            request.setAttribute("error", e.getMessage());

            try {
                List<Usuario> listaUsuario = new UsuarioDAO().obtenerTodosLosUsuarios();
                request.setAttribute("listaUsuario", listaUsuario);
                
                List<com.biblioteca.web.model.Ejemplar> listaEjemplares = new com.biblioteca.web.dao.EjemplarDAO().obtenerEjemplaresDisponibles();
                request.setAttribute("listaEjemplares", listaEjemplares);
            } catch (Exception ex) {
                System.err.println("Error recargando modales: " + ex.getMessage());
            }

            request.getRequestDispatcher("/vistas/Prestamos/NuevoPrestamo.jsp").forward(request, response);
        }
    }
}
