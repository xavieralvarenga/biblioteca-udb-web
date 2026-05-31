<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%-- 1. PRIMERO SE IMPORTA LA LIBRERÍA --%>
        <%@ taglib uri="jakarta.tags.core" prefix="c" %>

            <%-- 2. SEGUNDO SE EVALÚA LA SEGURIDAD DE LA SESIÓN --%>
                <c:if test="${empty sessionScope.usuarioLogueado}">
                    <c:redirect url="/login.jsp?error=SesionInvalida" />
                </c:if>

                <!DOCTYPE html>
                <html lang="es">

                <head>
                    <meta charset="UTF-8">
                    <title>Biblioteca Don Bosco - Menú Principal</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f6f9;
                            margin: 0;
                            padding: 0;
                        }

                        .navbar {
                            background-color: #343a40;
                            color: white;
                            padding: 15px 30px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                        }

                        .navbar a {
                            color: #dc3545;
                            text-decoration: none;
                            font-weight: bold;
                            background-color: white;
                            padding: 6px 12px;
                            border-radius: 4px;
                            border: 1px solid transparent;
                        }

                        .navbar a:hover {
                            background-color: #f8f9fa;
                            border-color: #dc3545;
                        }

                        .container {
                            max-width: 900px;
                            margin: 50px auto;
                            padding: 0 20px;
                            text-align: center;
                        }

                        .welcome-text {
                            color: #212529;
                            margin-bottom: 40px;
                        }

                        .menu-grid {
                            display: flex;
                            flex-wrap: wrap;
                            justify-content: center;
                            gap: 25px;
                        }

                        .menu-card {
                            background: white;
                            border: 1px solid #dee2e6;
                            border-radius: 8px;
                            width: 240px;
                            padding: 25px;
                            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
                            transition: transform 0.2s;
                            text-align: center;
                        }

                        .menu-card:hover {
                            transform: translateY(-5px);
                            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
                        }

                        .menu-card h3 {
                            margin-top: 0;
                            color: #007bff;
                        }

                        .menu-card p {
                            color: #6c757d;
                            font-size: 14px;
                            min-height: 40px;
                        }

                        .btn-enter {
                            display: inline-block;
                            background-color: #007bff;
                            color: white;
                            padding: 10px 20px;
                            text-decoration: none;
                            border-radius: 4px;
                            font-weight: bold;
                        }

                        .btn-enter:hover {
                            background-color: #0056b3;
                        }
                    </style>
                </head>

                <body>

                    <div class="navbar">
                        <div>
                            <strong>Sistema Bibliotecario Don Bosco</strong> |
                            <span>
                                <c:out
                                    value="${sessionScope.usuarioLogueado.nombres} ${sessionScope.usuarioLogueado.apellidos}" />
                            </span>
                            <span style="color: #adb5bd; font-size: 13px; margin-left: 10px;">[
                                <c:out value="${sessionScope.usuarioLogueado.nombreRol}" />]
                            </span>
                        </div>
                        <a href="${pageContext.request.contextPath}/login?accion=logout">Cerrar Sesión</a>
                    </div>

                    <div class="container">
                        <div class="welcome-text">
                            <h2>¿A qué módulo deseas dirigirte hoy?</h2>
                            <p>Selecciona una de las opciones habilitadas para tu perfil de usuario.</p>
                        </div>

                        <div class="menu-grid">

                            <%-- MÓDULO 1: CONTROL DE USUARIOS (Exclusivo para Encargados) --%>
                                <c:if test="${sessionScope.usuarioLogueado.nombreRol == 'Encargado (Bibliotecario)'}">
                                    <div class="menu-card">
                                        <h3>Gestión de Usuarios</h3>
                                        <p>Administración de cuentas, registros de nuevos encargados, estudiantes y
                                            profesores.</p>
                                        <a href="${pageContext.request.contextPath}/usuarios"
                                            class="btn-enter">Ingresar</a>
                                    </div>
                                </c:if>

                                <%-- MÓDULO 2: INVENTARIO DE DOCUMENTOS --%>
                                    <div class="menu-card">
                                        <h3>Catálogo de Ejemplares</h3>
                                        <c:choose>
                                            <c:when
                                                test="${sessionScope.usuarioLogueado.nombreRol == 'Encargado (Bibliotecario)'}">
                                                <p>Altas, modificaciones, bajas y visualización del stock físico de
                                                    documentos.</p>
                                            </c:when>
                                            <c:otherwise>
                                                <p>Consulta pública de Libros, Revistas y recursos disponibles para
                                                    préstamo.</p>
                                            </c:otherwise>
                                        </c:choose>
                                        <a href="${pageContext.request.contextPath}/documentos"
                                            class="btn-enter">Ingresar</a>
                                    </div>

                                    <%-- MÓDULO 3: PRÉSTAMOS Y DEVOLUCIONES --%>
                                        <div class="menu-card">
                                            <h3 style="color: #6c757d;">Préstamos y Moras</h3>
                                            <p>Gestión operativa de préstamos activos, fechas límites y alertas
                                                algorítmicas de cobro.</p>
                                            <a href="${pageContext.request.contextPath}/Prestamos?accion=listar"
                                                class="btn-enter">Agregar</a>
                                        </div>

                        </div>
                    </div>

                </body>

                </html>