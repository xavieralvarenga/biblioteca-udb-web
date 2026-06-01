<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="utf-8">
            <meta content="width=device-width, initial-scale=1.0" name="viewport">

            <title>Préstamos</title>
            <meta content="" name="description">
            <meta content="" name="keywords">

            <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
            <link href="${pageContext.request.contextPath}/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

            <link href="https://fonts.gstatic.com" rel="preconnect">
            <link
                href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i"
                rel="stylesheet">

            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap-icons/bootstrap-icons.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/boxicons/css/boxicons.min.css"
                rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/quill/quill.snow.css" rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/quill/quill.bubble.css" rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/remixicon/remixicon.css" rel="stylesheet">
            <link href="${pageContext.request.contextPath}/assets/vendor/simple-datatables/style.css" rel="stylesheet">

            <link href="${pageContext.request.contextPath}/assets/css/style.css" rel="stylesheet">

            <style>
                #main,
                #footer {
                    margin-left: 0 !important;
                }

                .header {
                    padding-left: 20px !important;
                }
            </style>
        </head>

        <body>

            <header id="header" class="header fixed-top d-flex align-items-center">

                <div class="d-flex align-items-center">
                    <a href="${pageContext.request.contextPath}/menu"
                        class="btn btn-outline-primary d-flex align-items-center gap-2">
                        <i class="bi bi-arrow-left"></i>
                        <span>Regresar al Menú</span>
                    </a>
                </div>
            </header>


            <main id="main" class="main">
                <div class="pagetitle">
                    <h1>Configuración de Límites de Préstamo</h1>
                    <nav>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="javascript:history.back()">Inicio</a></li>
                            <li class="breadcrumb-item active">Configuración de Límites de Préstamo</li>
                        </ol>
                    </nav>
                </div>

                <section class="section">
                    <div class="row justify-content-center">
                        <div class="col-lg-8">

                            <c:if test="${param.msg == 'ActualizacionExitosa'}">
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    <i class="bi bi-check-circle me-1"></i> ¡Las reglas de préstamo se actualizaron
                                    correctamente!
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"
                                        aria-label="Close"></button>
                                </div>
                            </c:if>

                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Límites Vigentes</h5>

                                    <form action="${pageContext.request.contextPath}/ConfiguracionLimites"
                                        method="POST">
                                        <div class="table-responsive">
                                            <table class="table table-bordered text-center align-middle">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>Perfil de Lector</th>
                                                        <th>Cantidad Máxima de Libros</th>
                                                        <th>Tiempo Máximo (Días)</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="rol" items="${limitesRoles}">
                                                        <tr>
                                                            <td class="fw-bold">
                                                                ${rol[1]}
                                                                <input type="hidden" name="idTipo" value="${rol[0]}">
                                                            </td>
                                                            <td>
                                                                <input type="number" class="form-control text-center"
                                                                    name="maxLibros" value="${rol[2]}" min="1" max="20"
                                                                    required>
                                                            </td>
                                                            <td>
                                                                <div class="input-group">
                                                                    <input type="number"
                                                                        class="form-control text-center" name="maxDias"
                                                                        value="${rol[3]}" min="1" max="60" required>
                                                                    <span class="input-group-text">días</span>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                        <div class="d-grid gap-2 mt-4">
                                            <button type="submit" class="btn btn-primary btn-lg">
                                                <i class="bi bi-save"></i> Guardar Cambios
                                            </button>
                                        </div>
                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </main>
            <footer id="footer" class="footer">
                <div class="copyright">
                    &copy; Copyright <strong><span>Universidad Don Bosco</span></strong>. All Rights Reserved
                </div>
                <div class="credits">
                    Designed by >Grupo de trabajo</a>
                </div>
            </footer>

            <a href="#" class="back-to-top d-flex align-items-center justify-content-center">
                <i class="bi bi-arrow-up-short"></i>
            </a>

            <script src="${pageContext.request.contextPath}/assets/vendor/apexcharts/apexcharts.min.js"></script>
            <script
                src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/chart.js/chart.umd.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/echarts/echarts.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/quill/quill.js"></script>
            <script
                src="${pageContext.request.contextPath}/assets/vendor/simple-datatables/simple-datatables.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/tinymce/tinymce.min.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/php-email-form/validate.js"></script>

            <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

            <script>
                // Limpiador de URL para las alertas
                if (window.history.replaceState) {
                    let urlParams = new URLSearchParams(window.location.search);
                    urlParams.delete('msg');
                    urlParams.delete('error');
                    let nuevaUrl = window.location.pathname + (urlParams.toString() ? "?" + urlParams.toString() : "");
                    window.history.replaceState(null, null, nuevaUrl);
                }
            </script>
        </body>

        </html>