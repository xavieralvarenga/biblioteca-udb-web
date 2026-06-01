<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <title>Biblioteca Don Bosco - Gestión de Usuarios</title>
            <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 30px;
                    background-color: #f4f6f9;
                }

                .container {
                    max-width: 900px;
                    margin: 0 auto;
                    background: white;
                    padding: 20px;
                    border-radius: 8px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                }

                .form-group {
                    margin-bottom: 15px;
                }

                .form-group label {
                    display: block;
                    margin-bottom: 5px;
                    font-weight: bold;
                }

                .form-group input,
                .form-group select {
                    width: 100%;
                    padding: 8px;
                    box-sizing: border-box;
                    border: 1px solid #ccc;
                    border-radius: 4px;
                }

                .btn {
                    padding: 10px 15px;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-size: 16px;
                    font-weight: bold;
                    color: white;
                }

                .btn-primary {
                    background-color: #0056b3;
                    width: 100%;
                }

                .btn-warning {
                    background-color: #ffc107;
                    color: black;
                    width: 100%;
                }

                .btn-danger {
                    background-color: #dc3545;
                }

                .alert {
                    padding: 10px;
                    margin-bottom: 20px;
                    border-radius: 4px;
                    font-weight: bold;
                }

                .alert-success {
                    background-color: #d4edda;
                    color: #155724;
                    border: 1px solid #c3e6cb;
                }

                .alert-danger {
                    background-color: #f8d7da;
                    color: #721c24;
                    border: 1px solid #f5c6cb;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 20px;
                }

                th,
                td {
                    border: 1px solid #dee2e6;
                    padding: 10px;
                    text-align: left;
                }

                th {
                    background-color: #0056b3;
                    color: white;
                }

                hr {
                    margin: 30px 0;
                    border: 0;
                    border-top: 1px solid #ccc;
                }
            </style>
        </head>

        <body>

            <div class="container">

                <%-- Barra de Control de Sesión Activa --%>
                    <div
                        style="background-color: #e9ecef; padding: 10px 20px; margin-bottom: 20px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center;">
                        <span>
                            Bienvenido(a), <strong>
                                <c:out
                                    value="${sessionScope.usuarioLogueado.nombres} ${sessionScope.usuarioLogueado.apellidos}"
                                    default="Encargado" />
                            </strong>
                            (
                            <c:out value="${sessionScope.usuarioLogueado.nombreRol}" default="Personal" />)
                        </span>
                        <a href="${pageContext.request.contextPath}/menu"
                            style="text-decoration: none; font-weight: bold; color: #6c757d;">← Volver al Menú</a>
                    </div>

                    <h2>Módulo de Encargados - Control de Usuarios</h2>

                    <%-- Mensajes de Notificación --%>
                        <c:if test="${not empty param.msg}">
                            <div class="alert alert-success">
                                <c:choose>
                                    <c:when test="${param.msg == 'UsuarioRegistrado'}">¡Usuario ingresado con éxito!
                                    </c:when>
                                    <c:when test="${param.msg == 'UsuarioActualizado'}">¡Usuario modificado
                                        correctamente!</c:when>
                                    <c:when test="${param.msg == 'UsuarioEliminado'}">El usuario ha sido dado de baja.
                                    </c:when>
                                    <c:when test="${param.msg == 'PasswordRestablecida'}">La contraseña ha sido
                                        restablecida correctamente.</c:when>
                                </c:choose>
                            </div>
                        </c:if>

                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger">
                                <c:choose>
                                    <c:when test="${param.error == 'ErrorAlRegistrar'}">Hubo un problema al procesar la
                                        solicitud en la Base de Datos.</c:when>
                                    <c:when test="${param.error == 'ErrorRestablecer'}">No se pudo restablecer la
                                        contraseña. Verifique el carnet.</c:when>
                                    <c:when test="${param.error == 'DatosInvalidos'}">Los datos ingresados tienen un
                                        formato incorrecto.</c:when>
                                    <c:when test="${param.error == 'AccionNoValida'}">Acción no permitida.</c:when>
                                </c:choose>
                            </div>
                        </c:if>

                        <%-- Formulario dinámico Registro / Edición --%>
                            <h3>
                                <c:out
                                    value="${not empty userEditar ? 'Modificar Usuario Seleccionado' : '1. Registrar Nuevo Usuario'}" />
                            </h3>
                            <form
                                action="${pageContext.request.contextPath}/usuarios?accion=${not empty userEditar ? 'actualizar' : 'registrar'}"
                                method="POST">
                                <input type="hidden" name="idUsuario" value="${userEditar.idUsuario}">

                                <div class="form-group">
                                    <label>Nombres:</label>
                                    <input type="text" name="nombres" value="<c:out value='${userEditar.nombres}'/>"
                                        required>
                                </div>

                                <div class="form-group">
                                    <label>Apellidos:</label>
                                    <input type="text" name="apellidos" value="<c:out value='${userEditar.apellidos}'/>"
                                        required>
                                </div>

                                <div class="form-group">
                                    <label>Carnet (Docente / Alumno):</label>
                                    <input type="text" name="carnet"
                                        value="<c:out value='${userEditar.carnetDocenteAlumno}'/>" required>
                                </div>

                                <div class="form-group">
                                    <label>Contraseña / Credencial Hash:</label>
                                    <input type="password" name="password"
                                        value="<c:out value='${userEditar.passwordHash}'/>" required>
                                </div>

                                <div class="form-group">
                                    <label>Privilegios / Rol de Usuario:</label>
                                    <select name="idTipo" required>
                                        <option value="1" ${userEditar.idTipo==1 ? 'selected' : '' }>Administrador
                                        </option>
                                        <option value="2" ${userEditar.idTipo==2 ? 'selected' : '' }>Profesor</option>
                                        <option value="3" ${userEditar.idTipo==3 ? 'selected' : '' }>Estudiante</option>
                                    </select>
                                </div>

                                <button type="submit"
                                    class="btn ${not empty userEditar ? 'btn-warning' : 'btn-primary'}">
                                    ${not empty userEditar ? 'Guardar Cambios' : 'Guardar y Asignar Privilegios'}
                                </button>
                                <c:if test="${not empty userEditar}">
                                    <a href="${pageContext.request.contextPath}/usuarios?accion=listar"
                                        style="display:block; text-align:center; margin-top:10px; color:#6c757d;">Cancelar
                                        Modificación</a>
                                </c:if>
                            </form>

                            <hr>

                            <%-- Tabla del Listado General --%>
                                <h3>Usuarios Activos en el Sistema</h3>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Nombre Completo</th>
                                            <th>Carnet</th>
                                            <th>Rol / Tipo</th>
                                            <th>Acciones</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="u" items="${usuarios}">
                                            <tr>
                                                <td>
                                                    <c:out value="${u.idUsuario}" />
                                                </td>
                                                <td>
                                                    <c:out value="${u.nombres}" />
                                                    <c:out value="${u.apellidos}" />
                                                </td>
                                                <td><strong>
                                                        <c:out value="${u.carnetDocenteAlumno}" />
                                                    </strong></td>
                                                <td>
                                                    <c:out value="${u.nombreRol}" />
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/usuarios?accion=cargarEditar&id=${u.idUsuario}"
                                                        style="color: #ffc107; font-weight: bold; text-decoration: none; margin-right: 10px;">Editar</a>
                                                    <a href="${pageContext.request.contextPath}/usuarios?accion=eliminar&id=${u.idUsuario}"
                                                        onclick="return confirm('¿Dar de baja a este usuario?');"
                                                        style="color: #dc3545; font-weight: bold; text-decoration: none;">Dar
                                                        de Baja</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty usuarios}">
                                            <tr>
                                                <td colspan="5" style="text-align: center; color: #999;">No hay usuarios
                                                    activos registrados.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>

                                <hr>

                                <%-- Formulario 2: Restablecimiento de Credenciales --%>
                                    <h3>2. Restablecer Contraseña Rápidamente</h3>
                                    <form action="${pageContext.request.contextPath}/usuarios?accion=restablecer"
                                        method="POST">
                                        <div class="form-group">
                                            <label>Carnet del Usuario:</label>
                                            <input type="text" name="carnet" placeholder="Ej: AB12345" required>
                                        </div>

                                        <div class="form-group">
                                            <label>Nueva Contraseña Temporal:</label>
                                            <input type="password" name="nuevaPassword" required>
                                        </div>

                                        <button type="submit" class="btn btn-danger">Restablecer Contraseña</button>
                                    </form>
            </div>

        </body>

        </html>