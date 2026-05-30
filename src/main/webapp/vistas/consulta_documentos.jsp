<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consulta de Catálogo - Biblioteca</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f8f9fa; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; background: white; }
        th, td { border: 1px solid #dee2e6; padding: 12px; text-align: left; }
        th { background-color: #17a2b8; color: white; }
        .btn-menu { display: inline-block; background-color: #6c757d; color: white; padding: 8px 15px; text-decoration: none; border-radius: 4px; font-weight: bold; margin-bottom: 15px; }
        .badge { padding: 4px 8px; border-radius: 4px; color: white; font-size: 12px; font-weight: bold; }
        .badge-info { background-color: #17a2b8; }
    </style>
</head>
<body>

    <div style="background-color: #e9ecef; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; border-radius: 4px;">
        <span>
            Usuario Activo: <strong><c:out value="${sessionScope.usuarioLogueado.nombres}"/></strong>
            | Rol: <span class="badge badge-info"><c:out value="${sessionScope.usuarioLogueado.nombreRol}"/> (Solo Lectura)</span>
        </span>
        <a href="${pageContext.request.contextPath}/login?accion=logout" style="color: #dc3545; font-weight: bold; text-decoration: none;">Cerrar Sesión</a>
    </div>

    <a href="${pageContext.request.contextPath}/menu" class="btn-menu">← Volver al Menú Principal</a>

    <h2>Catálogo General de Documentos (Consulta)</h2>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tipo de Documento</th>
                <th>Título</th>
                <th>Autor</th>
                <th>Estado</th>
                <th>Propiedades Específicas</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="doc" items="${documentos}">
                <tr>
                    <td><c:out value="${doc.idDocumento}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${doc.idTipoDoc == 1}"><span style="color:blue; font-weight:bold;">LIBRO</span></c:when>
                            <c:when test="${doc.idTipoDoc == 2}"><span style="color:red; font-weight:bold;">REVISTA</span></c:when>
                            <c:otherwise><span style="color:purple; font-weight:bold;">CD</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td><strong><c:out value="${doc.titulo}"/></strong></td>
                    <td><c:out value="${doc.autor}"/></td>
                    <td><c:out value="${doc.estado}"/></td>
                    <td>
                        <%-- EVALUACIÓN SEGURA BASADA EN ID DE TIPO PARA EVITAR PROPERTYNOTFOUNDEXCEPTION --%>
                        <c:choose>
                            <c:when test="${doc.idTipoDoc == 1}">
                                <strong>ISBN:</strong> <c:out value="${doc.isbn}"/> |
                                <strong>Editorial:</strong> <c:out value="${doc.editorial}"/> |
                                <strong>Edición:</strong> <c:out value="${doc.edicion}"/>
                            </c:when>
                            <c:when test="${doc.idTipoDoc == 2}">
                                <strong>ISSN:</strong> <c:out value="${doc.issn}"/> |
                                <strong>Volumen:</strong> <c:out value="${doc.volumen}"/> |
                                <strong>Mes:</strong> <c:out value="${doc.mesPublicacion}"/>
                            </c:when>
                            <c:otherwise>
                                <strong>Duración:</strong> <c:out value="${doc.duracionMinutos}"/> min |
                                <strong>Contenido:</strong> <c:out value="${doc.tipoContenido}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>