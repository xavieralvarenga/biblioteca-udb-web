<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="utf-8">
            <title>Configuración de Mora - Sistema de Mediateca</title>
            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap-icons/bootstrap-icons.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
        </head>

        <body>

            <header id="header" class="header fixed-top d-flex align-items-center" style="padding-left: 20px;">
                <button onclick="history.back()" class="btn btn-outline-primary d-flex align-items-center gap-2">
                    <i class="bi bi-arrow-left"></i><span>Regresar</span>
                </button>
            </header>

            <main id="main" class="main" style="margin-left: 0;">
                <div class="pagetitle">
                    <h1>Configuración de Mora Anual</h1>
                </div>

                <c:if test="${param.mensaje == 'exito'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="bi bi-check-circle me-1"></i> Tarifa de mora guardada correctamente.
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                    <script> window.history.replaceState(null, null, window.location.pathname); </script>
                </c:if>

                <section class="section">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Configurar Tarifa</h5>
                                    <form action="${pageContext.request.contextPath}/ConfiguracionMora" method="POST">
                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Año:</label>
                                            <input type="number" class="form-control" name="anio" value="${anioActual}"
                                                required>
                                        </div>
                                        <div class="mb-4">
                                            <label class="form-label fw-bold">Tarifa Diaria ($):</label>
                                            <input type="number" step="0.01" min="0" class="form-control" name="tarifa"
                                                placeholder="Ej. 0.50" required>
                                        </div>
                                        <div class="d-grid">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bi bi-save"></i> Guardar Configuración
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-8">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Historial de Tarifas</h5>
                                    <table class="table table-bordered text-center">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Año</th>
                                                <th>Tarifa Diaria Asignada</th>
                                                <th>Estado</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="mora" items="${listaMoras}">
                                                <tr>
                                                    <td class="fw-bold">${mora.anio}</td>
                                                    <td>$ ${mora.tarifaDiaria}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${mora.anio == anioActual}">
                                                                <span class="badge bg-success">En vigencia</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">Histórico</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
            <script
                src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>