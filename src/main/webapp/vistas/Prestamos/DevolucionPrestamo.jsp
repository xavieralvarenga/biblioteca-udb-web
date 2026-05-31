<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="utf-8">
            <title>Procesar Devolución - Sistema de Mediateca</title>
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
                    <h1>Procesar Devolución (Ticket #${idPrestamo})</h1>
                </div>

                <section class="section">
                    <div class="row">
                        <div class="col-lg-8">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Detalle de Materiales y Mora</h5>

                                    <table class="table table-bordered">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Cód. Barras</th>
                                                <th>Título</th>
                                                <th>Fecha Límite</th>
                                                <th>Retraso (Días)</th>
                                                <th>Subtotal Mora</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${detallesDevolucion}">
                                                <tr>
                                                    <td>${item[2]}</td>
                                                    <td>${item[3]}</td>
                                                    <td>${item[4]}</td>

                                                    <td class="${item[5] > 0 ? 'text-danger fw-bold' : 'text-success'}">
                                                        ${item[5]} días
                                                    </td>

                                                    <td class="${item[7] > 0 ? 'text-danger fw-bold' : ''}">
                                                        $ ${item[7]}
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Resumen de Cobro</h5>

                                    <form action="${pageContext.request.contextPath}/Prestamos" method="POST">
                                        <input type="hidden" name="accion" value="devolver">
                                        <input type="hidden" name="idPrestamo" value="${idPrestamo}">

                                        <div class="mb-3">
                                            <label class="form-label fw-bold">Total Mora Generada:</label>
                                            <input type="text" class="form-control text-danger fw-bold fs-4"
                                                value="$ ${totalMora}" readonly>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Monto a Pagar ($):</label>
                                            <input type="number" step="0.01" min="0" class="form-control"
                                                name="montoPagado" value="${totalMora == 0 ? '0' : ''}" required
                                                ${totalMora==0 ? 'readonly' : '' } placeholder="Ej. 1.50">
                                        </div>

                                        <div class="mb-4">
                                            <label class="form-label">Observaciones sobre el estado físico:</label>
                                            <textarea class="form-control" name="observaciones" rows="3"
                                                placeholder="Libros en buen estado..."></textarea>
                                        </div>

                                        <div class="d-grid gap-2">
                                            <button type="submit" class="btn btn-success btn-lg">
                                                <i class="bi bi-check-circle"></i> Confirmar Devolución
                                            </button>
                                        </div>
                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
        </body>

        </html>