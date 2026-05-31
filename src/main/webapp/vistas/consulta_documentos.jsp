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
        .search-container {
                background: white;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
                align-items: center;
            }

            .search-container input,
            .search-container select {
                padding: 10px;
                border: 1px solid #ced4da;
                border-radius: 6px;
                font-size: 14px;
                outline: none;
                transition: 0.2s;
            }

            .search-container input:focus,
            .search-container select:focus {
                border-color: #17a2b8;
                box-shadow: 0 0 4px rgba(23,162,184,0.4);
            }

            .search-container button {
                padding: 10px 15px;
                background-color: #17a2b8;
                border: none;
                color: white;
                border-radius: 6px;
                cursor: pointer;
                font-weight: bold;
                transition: 0.2s;
            }

            .search-container button:hover {
                background-color: #138496;
            }

            .search-container .clear-btn {
                background-color: #6c757d;
            }

            .search-container .clear-btn:hover {
                background-color: #5a6268;
            }
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

    <form method="get" action="${pageContext.request.contextPath}/documentos"
          class="search-container">

        <input type="hidden" name="accion" value="listar">

        <input type="text"
               name="buscar"
               placeholder="Buscar por título o autor..."
               value="${param.buscar}">

        <select name="tipo">
            <option value="">Todos los tipos</option>
            <option value="1" ${param.tipo=='1' ? 'selected' : ''}>Libro</option>
            <option value="2" ${param.tipo=='2' ? 'selected' : ''}>Revista</option>
            <option value="3" ${param.tipo=='3' ? 'selected' : ''}>CD</option>
        </select>

        <select name="estado">
            <option value="">Todos los estados</option>
            <option value="Disponible" ${param.estado=='Disponible' ? 'selected' : ''}>Disponible</option>
            <option value="Prestado" ${param.estado=='Prestado' ? 'selected' : ''}>Prestado</option>
            <option value="Dañado" ${param.estado=='Dañado' ? 'selected' : ''}>Dañado</option>
            <option value="Extraviado" ${param.estado=='Extraviado' ? 'selected' : ''}>Extraviado</option>
        </select>

        <button type="submit">Buscar</button>

        <a href="${pageContext.request.contextPath}/documentos"
           class="clear-btn"
           style="text-decoration:none; padding:10px 15px; border-radius:6px; color:white; display:inline-block;">
            Limpiar
        </a>

    </form>

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