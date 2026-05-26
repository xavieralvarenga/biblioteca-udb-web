<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Biblioteca - Búsqueda de Ejemplares</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f1f3f5; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; }
        th, td { border: 1px solid #dee2e6; padding: 12px; text-align: left; }
        th { background-color: #6c757d; color: white; }
    </style>
</head>
<body>

    <div style="background-color: #e9ecef; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center;">
        <span>Usuario: <strong>${sessionScope.usuarioLogueado.nombres}</strong> (${sessionScope.usuarioLogueado.nombreRol})</span>
        <a href="${pageContext.request.contextPath}/login?accion=logout" style="color: blue;">Cerrar Aplicación</a>
    </div>

    <h2>Módulo de Consultas Públicas</h2>
    <p>Explore los recursos didácticos disponibles para reserva y préstamo en sala.</p>

    <table>
        <thead>
            <tr>
                <th>Título del Documento</th>
                <th>Tipo</th>
                <th>Año de Edición</th>
                <th>Unidades Disponibles</th>
                <th>Referencias Técnicas</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="doc" items="${documentos}">
                <tr>
                    <td><strong>${doc.titulo}</strong></td>
                    <td>${doc.tipoDocumento}</td>
                    <td>${doc.anioPublicacion}</td>
                    <td>
                        <c:choose>
                            <c:when test="${doc.cantidadEjemplares > 0}">
                                <span style="color: green; font-weight: bold;">Disponible (${doc.cantidadEjemplares})</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: red; font-weight: bold;">Agotado temporalmente</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${doc.tipoDocumento == 'Libro'}">
                                Autor: ${doc.autor} | Editorial: ${doc.editorial} | ISBN: ${doc.isbn}
                            </c:when>
                            <c:when test="${doc.tipoDocumento == 'Revista'}">
                                Organismo: ${doc.organismoResponsable} | Frecuencia: ${doc.periodicidad}
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>