<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="utf-8">
            <title>Detalle del Préstamo - Sistema de Mediateca</title>
            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap-icons/bootstrap-icons.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">
        </head>

        <body>

            <header id="header" class="header fixed-top d-flex align-items-center" style="padding-left: 20px;">
                <button onclick="history.back()" class="btn btn-outline-primary d-flex align-items-center gap-2">
                    <i class="bi bi-arrow-left"></i><span>Regresar a la Lista</span>
                </button>
            </header>

            <main id="main" class="main" style="margin-left: 0;">
                <div class="pagetitle mb-4">
                    <h1>Detalle del Préstamo (Ticket #${prestamoCabecera.idPrestamo})</h1>
                </div>

                <section class="section">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title"><i class="bi bi-person-badge"></i> Datos del Lector</h5>

                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item"><strong>Carnet:</strong>
                                            ${prestamoCabecera.carnetDocenteAlumno}</li>
                                        <li class="list-group-item"><strong>Nombre:</strong> ${prestamoCabecera.nombres}
                                        </li>
                                        <li class="list-group-item"><strong>Fecha de Préstamo:</strong>
                                            ${prestamoCabecera.fechaPrestamo}</li>
                                        <li class="list-group-item">
                                            <strong>Estado General:</strong>
                                            <c:choose>
                                                <c:when test="${prestamoCabecera.estadoGeneral == 'ACTIVO'}">
                                                    <span class="badge bg-success">Activo</span>
                                                </c:when>
                                                <c:when test="${prestamoCabecera.estadoGeneral == 'PARCIAL'}">
                                                    <span class="badge bg-info text-dark">Parcial</span>
                                                </c:when>
                                                <c:when test="${prestamoCabecera.estadoGeneral == 'CONDEUDA'}">
                                                    <span class="badge bg-warning text-dark">Con Deuda</span>
                                                </c:when>
                                                <c:when test="${prestamoCabecera.estadoGeneral == 'FINALIZADO'}">
                                                    <span class="badge bg-secondary">Finalizado</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span
                                                        class="badge bg-secondary">${prestamoCabecera.estadoGeneral}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-8">
                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title"><i class="bi bi-journal-bookmark"></i> Materiales Prestados
                                    </h5>

                                    <div class="table-responsive">
                                        <table class="table table-hover table-bordered text-center align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>ID Detalle</th>
                                                    <th>Cód. Barras</th>
                                                    <th>Título</th>
                                                    <th>Fecha Límite</th>
                                                    <th>Estado Material</th>
                                                    <th>Estado Pago</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${listaDetalles}">
                                                    <tr>
                                                        <th scope="row">${item[0]}</th>
                                                        <td><span
                                                                class="badge bg-light text-dark border">${item[1]}</span>
                                                        </td>
                                                        <td class="text-start">${item[2]}</td>
                                                        <td>${item[3]}</td>
                                                        <td>
                                                            <span
                                                                class="badge ${item[4] == 'Devuelto' ? 'bg-secondary' : 'bg-primary'}">
                                                                ${item[4]}
                                                            </span>
                                                        </td>
                                                        <td>
                                                            <span
                                                                class="badge ${item[5] == 'Pendiente' ? 'bg-danger' : (item[5] == 'Sin Mora' ? 'bg-success' : 'bg-info')}">
                                                                ${item[5]}
                                                            </span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
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