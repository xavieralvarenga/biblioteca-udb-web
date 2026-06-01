<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <title>Gestión de Ejemplares</title>
            <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f8f9fa;
                }

                .card {
                    background: white;
                    padding: 20px;
                    border-radius: 5px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
                    margin-bottom: 25px;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    background: white;
                }

                th,
                td {
                    border: 1px solid #dee2e6;
                    padding: 12px;
                    text-align: left;
                }

                th {
                    background-color: #007bff;
                    color: white;
                }

                .btn {
                    padding: 8px 15px;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-weight: bold;
                    text-decoration: none;
                    color: white;
                }

                .btn-primary {
                    background-color: #007bff;
                }

                .btn-secondary {
                    background-color: #6c757d;
                }
            </style>
        </head>

        <body>

            <a href="${pageContext.request.contextPath}/documentos" class="btn btn-secondary">← Volver al Inventario</a>
            <br><br>

            <h2>Ejemplares de: <span style="color:#007bff;">
                    <c:out value="${docPadre.titulo}" />
                </span></h2>

            <div class="card">
                <h3>Registrar Nuevo Ejemplar Físico</h3>
                <form action="${pageContext.request.contextPath}/ejemplares" method="POST"
                    style="display: flex; gap: 15px; align-items: flex-end;">
                    <input type="hidden" name="idDocumento" value="${docPadre.idDocumento}">

                    <div>
                        <label style="display:block; margin-bottom:5px;">Código de Barras Único:</label>
                        <input type="text" name="codigoDeBarras" placeholder="Ej: BARRA-00123" required
                            style="padding:8px; border:1px solid #ccc; border-radius:4px;">
                    </div>

                    <div>
                        <label style="display:block; margin-bottom:5px;">Estado Inicial:</label>
                        <select name="estado" style="padding:8px; border:1px solid #ccc; border-radius:4px;">
                            <option value="Disponible">Disponible</option>
                            <option value="En Mantenimiento">En Mantenimiento</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">Guardar Ejemplar</button>
                </form>
            </div>

            <h3>Lista de Ejemplares Físicos</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID Ejemplar</th>
                        <th>Código de Barras del Ejemplar</th>
                        <th>Estado Actual</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="ej" items="${ejemplares}">
                        <tr>
                            <td>
                                <c:out value="${ej.idEjemplar}" />
                            </td>
                            <td><strong>
                                    <c:out value="${ej.codigoDeBarras}" />
                                </strong></td>
                            <td>
                                <c:out value="${ej.estado}" />
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ejemplares?accion=eliminar&idEjemplar=${ej.idEjemplar}&idDocumento=${ej.idDocumento}"
                                    onclick="return confirm('¿Dar de baja este ejemplar?');"
                                    style="color:#dc3545; font-weight:bold; text-decoration:none;">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty ejemplares}">
                        <tr>
                            <td colspan="4" style="text-align:center; color:#999;">No hay ejemplares registrados para
                                este documento.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

        </body>

        </html>