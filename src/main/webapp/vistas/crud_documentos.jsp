<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Panel de Control - Inventario de Documentos</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; }
        th, td { border: 1px solid #dee2e6; padding: 12px; text-align: left; }
        th { background-color: #007bff; color: white; }
        .card { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); margin-bottom: 25px; }
        .btn-add { background-color: #28a745; color: white; padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>

    <div style="background-color: #e9ecef; padding: 10px 20px; display: flex; justify-content: space-between; align-items: center;">
        <span>Encargado: <strong>${sessionScope.usuarioLogueado.nombres}</strong></span>
        <a href="${pageContext.request.contextPath}/login?accion=logout" style="color: red; font-weight: bold;">Salir</a>
    </div>

    <h2>Gestión Completa de Ejemplares (Modo Administrador)</h2>

    <div class="card">
        <h3>Ingresar Nuevo Libro</h3>
        <form action="${pageContext.request.contextPath}/documentos?accion=registrarLibro" method="POST">
            <input type="text" name="titulo" placeholder="Título del Libro" required style="margin: 5px; padding: 5px;">
            <input type="number" name="anio" placeholder="Año" required style="margin: 5px; padding: 5px; width: 80px;">
            <input type="number" name="ejemplares" placeholder="Ejemplares" required style="margin: 5px; padding: 5px; width: 80px;">
            <input type="text" name="isbn" placeholder="ISBN" required style="margin: 5px; padding: 5px;">
            <input type="text" name="autor" placeholder="Autor" required style="margin: 5px; padding: 5px;">
            <input type="number" name="paginas" placeholder="Páginas" required style="margin: 5px; padding: 5px; width: 80px;">
            <input type="text" name="editorial" placeholder="Editorial" required style="margin: 5px; padding: 5px;">
            <button type="submit" class="btn-add">Guardar Ejemplar</button>
        </form>
    </div>

    <h3>Catálogo General de Existencias</h3>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Clasificación</th>
                <th>Año</th>
                <th>Disponibles</th>
                <th>Detalles Específicos</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="doc" items="${documentos}">
                <tr>
                    <td>${doc.idDocumento}</td>
                    <td><strong>${doc.titulo}</strong></td>
                    <td>${doc.tipoDocumento}</td>
                    <td>${doc.anioPublicacion}</td>
                    <td>${doc.cantidadEjemplares} u.</td>
                    <td>
                        <c:choose>
                            <c:when test="${doc.tipoDocumento == 'Libro'}">
                                ISBN: ${doc.isbn} | Autor: ${doc.autor} | Editorial: ${doc.editorial}
                            </c:when>
                            <c:when test="${doc.tipoDocumento == 'Revista'}">
                                Edición Nº: ${doc.numeroEdicion} | Periodicidad: ${doc.periodicidad}
                            </c:when>
                        </c:choose>
                    </td>
                    <td>
                        <button style="background: #ffc107; border:none; padding:4px 8px; border-radius:3px; cursor:pointer;">Editar</button>
                        <button style="background: #dc3545; color:white; border:none; padding:4px 8px; border-radius:3px; cursor:pointer;">Eliminar</button>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>