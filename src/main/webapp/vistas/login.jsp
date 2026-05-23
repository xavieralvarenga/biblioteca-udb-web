<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Biblioteca Don Bosco - Login</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #e9ecef; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-card { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 100%; max-width: 380px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input { width: 100%; padding: 10px; box-sizing: border-box; border: 1px solid #ced4da; border-radius: 4px; }
        button { background-color: #28a745; color: white; padding: 10px; border: none; border-radius: 4px; width: 100%; font-size: 16px; cursor: pointer; }
        button:hover { background-color: #218838; }
        .alert { padding: 10px; margin-bottom: 15px; border-radius: 4px; font-size: 14px; font-weight: bold; text-align: center; }
        .alert-danger { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .alert-info { background-color: #d1ecf1; color: #0c5460; border: 1px solid #bee5eb; }
    </style>
</head>
<body>

<div class="login-card">
    <h2 style="text-align: center; margin-top: 0;">Biblioteca Don Bosco</h2>
    <p style="text-align: center; color: #6c757d;">Ingreso al Sistema</p>

    <c:if test="${param.error == 'CredencialesIncorrectas'}">
        <div class="alert alert-danger">Carnet o contraseña incorrectos.</div>
    </c:if>
    <c:if test="${param.msg == 'SesionCerrada'}">
        <div class="alert alert-info">Sesión cerrada de forma segura.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="POST">
        <div class="form-group">
            <label for="carnet">Carnet de Usuario:</label>
            <input type="text" id="carnet" name="carnet" placeholder="Ej: AA23001" required>
        </div>

        <div class="form-group">
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <button type="submit">Iniciar Sesión</button>
    </form>
</div>

</body>
</html>