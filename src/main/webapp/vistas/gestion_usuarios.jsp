<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--
    IMPORTACIÓN DE LIBRERÍAS DE ETIQUETAS (JSTL):
    Se importa el núcleo de las Jakarta Tags Standard Tag Library (c:) para
    manejar lógica estructural (condicionales c:if y selectores c:choose)
    sin incrustar código Java plano (scriptlets) en la vista.
--%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Don Bosco - Gestión de Usuarios</title>
    <%--
        ESTILOS CSS INTEGRADOS:
        Define el diseño visual de la interfaz. Utiliza un contenedor centrado,
        estiliza los formularios para que ocupen el ancho disponible y asigna
        colores corporativos y semánticos (verde para éxito, rojo para errores).
    --%>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; background-color: #f4f6f9; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; border-radius: 4px; }
        button { background-color: #0056b3; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; width: 100%; font-size: 16px; }
        button:hover { background-color: #004085; }
        .alert { padding: 10px; margin-bottom: 20px; border-radius: 4px; font-weight: bold; }
        .alert-success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .alert-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        hr { margin: 30px 0; border: 0; border-top: 1px solid #ccc; }
    </style>
</head>
<body>

<div class="container">
    <h2>Módulo de Encargados - Control de Usuarios</h2>

    <%-- =========================================================================
         SISTEMA DINÁMICO DE NOTIFICACIONES (ALERTAS DE ÉXITO)
         Evalúa si existe el parámetro 'msg' en la URL (por ejemplo, enviado desde
         el Servlet mediante response.sendRedirect("vista.jsp?msg=UsuarioRegistrado"))
         ========================================================================= --%>
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success">
            <c:choose>
                <%-- Filtra el mensaje específico para mostrar la traducción amigable al usuario --%>
                <c:when test="${param.msg == 'UsuarioRegistrado'}">¡Usuario ingresado con éxito al sistema!</c:when>
                <c:when test="${param.msg == 'PasswordRestablecida'}">La contraseña ha sido restablecida correctamente.</c:when>
            </c:choose>
        </div>
    </c:if>

    <%-- =========================================================================
         SISTEMA DINÁMICO DE NOTIFICACIONES (ALERTAS DE ERROR)
         Evalúa si el servlet interceptó una falla y redirigió con el parámetro 'error'.
         Evita que se expongan excepciones internas del servidor o de la base de datos.
         ========================================================================= --%>
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger">
            <c:choose>
                <c:when test="${param.error == 'ErrorAlRegistrar'}">Hubo un problema al guardar el usuario en la Base de Datos.</c:when>
                <c:when test="${param.error == 'ErrorRestablecer'}">No se pudo restablecer la contraseña. Verifique el carnet.</c:when>
                <c:when test="${param.error == 'DatosInvalidos'}">Los datos ingresados tienen un formato incorrecto.</c:when>
                <c:when test="${param.error == 'AccionNoValida'}">Acción no permitida.</c:when>
            </c:choose>
        </div>
    </c:if>

    <%-- =========================================================================
         FORMULARIO 1: REGISTRO DE NUEVOS USUARIOS
         Envia los datos por POST hacia el Servlet mapeado en '/usuarios'.
         Incluye el parámetro 'accion=registrar' en el query string para que el
         controlador identifique el bloque lógico a ejecutar.
         ========================================================================= --%>
    <h3>1. Registrar Nuevo Usuario</h3>
    <form action="${pageContext.request.contextPath}/usuarios?accion=registrar" method="POST">
        <div class="form-group">
            <label for="nombres">Nombres:</label>
            <input type="text" id="nombres" name="nombres" required>
        </div>

        <div class="form-group">
            <label for="apellidos">Apellidos:</label>
            <input type="text" id="apellidos" name="apellidos" required>
        </div>

        <%-- Este campo sirve como identificador único o llave de negocio (Carnet de biblioteca) --%>
        <div class="form-group">
            <label for="carnet">Carnet (Docente / Alumno):</label>
            <input type="text" id="carnet" name="carnet" required>
        </div>

        <div class="form-group">
            <label for="password">Contraseña Inicial:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <%-- Selector de Roles: Los valores numéricos (1, 2, 3) corresponden a las IDs de los roles en la base de datos --%>
        <div class="form-group">
            <label for="idTipo">Privilegios / Rol de Usuario:</label>
            <select id="idTipo" name="idTipo" required>
                <option value="1">Estudiante</option>
                <option value="2">Profesor</option>
                <option value="3">Encargado (Bibliotecario)</option>
            </select>
        </div>

        <button type="submit">Guardar y Asignar Privilegios</button>
    </form>

    <hr>

    <%-- =========================================================================
         FORMULARIO 2: RESTABLECIMIENTO DE CREDENCIALES
         Dirigido al mismo servlet controlador, pero cambia el parámetro por 'accion=restablecer'.
         Permite a los encargados resetear el acceso a usuarios que olvidaron su clave.
         ========================================================================= --%>
    <h3>2. Restablecer Contraseña de Usuarios</h3>
    <form action="${pageContext.request.contextPath}/usuarios?accion=restablecer" method="POST">
        <div class="form-group">
            <label for="carnetRestablecer">Carnet del Usuario:</label>
            <input type="text" id="carnetRestablecer" name="carnet" placeholder="Ej: AB12345" required>
        </div>

        <div class="form-group">
            <label for="nuevaPassword">Nueva Contraseña Temporal:</label>
            <input type="password" id="nuevaPassword" name="nuevaPassword" required>
        </div>

        <button type="submit" style="background-color: #dc3545;">Restablecer Contraseña</button>
    </form>
</div>

<%--
    Módulo de Encargados - Barra de Control de Sesión Activa
    Renderiza la información de auditoría del usuario actual en sesión y el mecanismo de salida.
--%>
<div style="background-color: #e9ecef; padding: 10px 20px; margin-bottom: 20px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center;">
    <span>
        Bienvenido(a),
        <strong>
            <c:out value="${sessionScope.usuarioLogueado.nombres} ${sessionScope.usuarioLogueado.apellidos}" default="Encargado" />
        </strong>
        (<c:out value="${sessionScope.usuarioLogueado.nombreRol}" default="Personal" />)
    </span>
    <a href="${pageContext.request.contextPath}/login?accion=logout"
       style="background-color: #dc3545; color: white; padding: 6px 12px; text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 14px;">
       Cerrar Sesión
    </a>
</div>

</body>
</html>