<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <title>Consulta de Catálogo - Biblioteca</title>
            <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f8f9fa;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 20px;
                    background: white;
                }

                th,
                td {
                    border: 1px solid #dee2e6;
                    padding: 12px;
                    text-align: left;
                }

                th {
                    background-color: #17a2b8;
                    color: white;
                }

                .btn-menu {
                    display: inline-block;
                    background-color: #6c757d;
                    color: white;
                    padding: 8px 15px;
                    text-decoration: none;
                    border-radius: 4px;
                    font-weight: bold;
                    margin-bottom: 15px;
                }

                .badge {
                    padding: 4px 8px;
                    border-radius: 4px;
                    color: white;
                    font-size: 12px;
                    font-weight: bold;
                }

                .badge-info {
                    background-color: #17a2b8;
                }
            </style>
        </head>

        <body>

            <div
                style="background-color: #e9ecef; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; border-radius: 4px;">
                <span>
                    Usuario Activo: <strong>
                        <c:out value="${sessionScope.usuarioLogueado.nombres}" />
                    </strong>
                    | Rol: <span class="badge badge-info">
                        <c:out value="${sessionScope.usuarioLogueado.nombreRol}" /> (Solo Lectura)
                    </span>
                </span>
                <a href="${pageContext.request.contextPath}/login?accion=logout"
                    style="color: #dc3545; font-weight: bold; text-decoration: none;">Cerrar Sesión</a>
            </div>

            <a href="${pageContext.request.contextPath}/menu" class="btn-menu">← Volver al Menú Principal</a>

            <h2>Catálogo General de Documentos (Consulta)</h2>

            <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                <input type="text" id="buscador" onkeyup="filtrarTabla()"
                    placeholder="Buscar por título, autor, código..."
                    style="flex: 1; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; font-size: 16px;">

                <select id="filtroTipo" onchange="filtrarTabla()"
                    style="padding: 12px; border: 1px solid #ced4da; border-radius: 4px; font-size: 16px; background-color: white; min-width: 200px;">
                    <option value="">Todos los materiales</option>
                    <option value="LIBRO">Solo Libros</option>
                    <option value="REVISTA">Solo Revistas</option>
                    <option value="CD">Solo CDs Multimedia</option>
                </select>
            </div>

            <table id="tablaDocumentos">
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
                            <td>
                                <c:out value="${doc.idDocumento}" />
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${doc.idTipoDoc == 1}"><span
                                            style="color:blue; font-weight:bold;">LIBRO</span></c:when>
                                    <c:when test="${doc.idTipoDoc == 2}"><span
                                            style="color:red; font-weight:bold;">REVISTA</span></c:when>
                                    <c:otherwise><span style="color:purple; font-weight:bold;">CD</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><strong>
                                    <c:out value="${doc.titulo}" />
                                </strong></td>
                            <td>
                                <c:out value="${doc.autor}" />
                            </td>
                            <td>
                                <c:out value="${doc.estado}" />
                            </td>
                            <td>
                                <%-- EVALUACIÓN SEGURA BASADA EN ID DE TIPO PARA EVITAR PROPERTYNOTFOUNDEXCEPTION --%>
                                    <c:choose>
                                        <c:when test="${doc.idTipoDoc == 1}">
                                            <strong>ISBN:</strong>
                                            <c:out value="${doc.isbn}" /> |
                                            <strong>Editorial:</strong>
                                            <c:out value="${doc.editorial}" /> |
                                            <strong>Edición:</strong>
                                            <c:out value="${doc.edicion}" />
                                        </c:when>
                                        <c:when test="${doc.idTipoDoc == 2}">
                                            <strong>ISSN:</strong>
                                            <c:out value="${doc.issn}" /> |
                                            <strong>Volumen:</strong>
                                            <c:out value="${doc.volumen}" /> |
                                            <strong>Mes:</strong>
                                            <c:out value="${doc.mesPublicacion}" />
                                        </c:when>
                                        <c:otherwise>
                                            <strong>Duración:</strong>
                                            <c:out value="${doc.duracionMinutos}" /> min |
                                            <strong>Contenido:</strong>
                                            <c:out value="${doc.tipoContenido}" />
                                        </c:otherwise>
                                    </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <script type="text/javascript">
                function filtrarTabla() {
                    var inputBuscador = document.getElementById("buscador").value.toUpperCase();
                    var filtroTipo = document.getElementById("filtroTipo").value.toUpperCase();

                    var tabla = document.getElementById("tablaDocumentos");
                    var tr = tabla.getElementsByTagName("tr");

                    for (var i = 1; i < tr.length; i++) {
                        var coincideTexto = false;
                        var coincideTipo = false;
                        var tds = tr[i].getElementsByTagName("td");

                        if (tds.length > 0) {
                            var textoTipo = tds[1].textContent || tds[1].innerText;
                            if (filtroTipo === "" || textoTipo.toUpperCase().indexOf(filtroTipo) > -1) {
                                coincideTipo = true;
                            }

                            for (var j = 0; j < tds.length; j++) {
                                if (tds[j]) {
                                    var textoColumna = tds[j].textContent || tds[j].innerText;
                                    if (textoColumna.toUpperCase().indexOf(inputBuscador) > -1) {
                                        coincideTexto = true;
                                        break;
                                    }
                                }
                            }

                            if (coincideTexto && coincideTipo) {
                                tr[i].style.display = "";
                            } else {
                                tr[i].style.display = "none";
                            }
                        }
                    }
                }
            </script>
        </body>

        </html>