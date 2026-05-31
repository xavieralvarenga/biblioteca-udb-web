package com.biblioteca.web.controller;

import com.biblioteca.web.dao.MoraAnualDAO;
import com.biblioteca.web.model.MoraAnual;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MoraServlet", urlPatterns = {"/ConfiguracionMora"})
public class MoraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Traemos la lista del DAO
        MoraAnualDAO moraDAO = new MoraAnualDAO();
        List<MoraAnual> listaMoras = moraDAO.listarTodas();

        // Enviamos la fecha actual para que el formulario sugiera el año en curso
        request.setAttribute("anioActual", java.time.LocalDate.now().getYear());
        request.setAttribute("listaMoras", listaMoras);

        request.getRequestDispatcher("/vistas/Prestamos/ConfiguracionMora.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int anio = Integer.parseInt(request.getParameter("anio"));
            double tarifa = Double.parseDouble(request.getParameter("tarifa"));

            MoraAnual mora = new MoraAnual();
            mora.setAnio(anio);
            mora.setTarifaDiaria(tarifa);

            boolean exito = new MoraAnualDAO().guardarOActualizar(mora);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/Prestamos?mensaje=exito");
            } else {
                response.sendRedirect(request.getContextPath() + "/Prestamos?mensaje=error");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/Prestamos?mensaje=error");
        }
    }
}