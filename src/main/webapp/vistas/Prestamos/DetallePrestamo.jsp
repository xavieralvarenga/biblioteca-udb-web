<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="es">

    <head>
        <meta charset="utf-8">
        <meta content="width=device-width, initial-scale=1.0" name="viewport">

        <title>Tarjetas - Sistema de Mediateca</title>
        <meta content="" name="description">
        <meta content="" name="keywords">

        <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
        <link href="${pageContext.request.contextPath}/assets/img/apple-touch-icon.png" rel="apple-touch-icon">

        <link href="https://fonts.gstatic.com" rel="preconnect">
        <link
            href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Nunito:300,300i,400,400i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i"
            rel="stylesheet">

        <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/vendor/bootstrap-icons/bootstrap-icons.css"
            rel="stylesheet">
        <link href="${pageContext.request.contextPath}/assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
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
                <button onclick="history.back()" class="btn btn-outline-primary d-flex align-items-center gap-2">
                    <i class="bi bi-arrow-left"></i>
                    <span>Regresar</span>
                </button>
            </div>
        </header>

        <main id="main" class="main">

            <div class="pagetitle">
                <h1>Detalle de prestamo</h1>
                <nav>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="javascript:history.back()">Préstamos</a></li>
                        <li class="breadcrumb-item active">Detalle de préstamo</li>
                    </ol>
                </nav>
            </div>

            <section class="section">
                <div class="row align-items-top">
                    <div class="col-lg-12">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Gestión de préstamos</h5>
                                <p class="card-text">
                                    <button type="button" class="btn btn-primary">
                                        <a href="${pageContext.request.contextPath}/Prestamos?accion=nuevo"
                                            class="btn btn-primary">
                                            <i class="bi bi-plus-circle me-1"></i> Agregar nuevo préstamo
                                        </a>
                                    </button>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>




            <section class="section">
                <div class="row align-items-top">
                    <div class="col-lg-12">
                        <div class="card">
                            <div class="card-body">
                                <h5 class="card-title">Tickets Recientes</h5>

                                <div class="table-responsive">
                                    <table class="table datatable">
                                        <thead>
                                            <tr>
                                                <th scope="col">ID Prestamos</th>
                                                <th scope="col">Carnet / Usuario</th>
                                                <th scope="col">Lector</th>
                                                <th scope="col">Fecha Prestamo</th>
                                                <th scope="col">Cant. Ítems</th>
                                                <th scope="col">Estado</th>
                                                <th scope="col">Acciones</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <th scope="row">1045</th>
                                                <td>AL001 - Andy Alvarado</td>
                                                <td>lector1</td>
                                                <td>2026-05-15</td>
                                                <td>3</td>
                                                <td><span class="badge bg-success">Activo</span></td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm btn-light border-0" type="button"
                                                            data-bs-toggle="dropdown" aria-expanded="false">
                                                            <i class="bi bi-three-dots-vertical"></i>
                                                        </button>

                                                        <ul class="dropdown-menu dropdown-menu-end">
                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-eye text-info me-2"></i> Ver detalle
                                                                    préstamo
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-person-gear text-primary me-2"></i>
                                                                    Cambiar lector
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <hr class="dropdown-divider">
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item text-danger" href="#">
                                                                    <i class="bi bi-trash text-danger me-2"></i> Borrar
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">1044</th>
                                                <td>PR002 - Roberto Sánchez</td>
                                                <td>lector1</td>
                                                <td>2026-05-10</td>
                                                <td>1</td>
                                                <td><span class="badge bg-danger">Con Deuda</span></td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm btn-light border-0" type="button"
                                                            data-bs-toggle="dropdown" aria-expanded="false">
                                                            <i class="bi bi-three-dots-vertical"></i>
                                                        </button>

                                                        <ul class="dropdown-menu dropdown-menu-end">
                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-eye text-info me-2"></i> Ver detalle
                                                                    préstamo
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-person-gear text-primary me-2"></i>
                                                                    Cambiar lector
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <hr class="dropdown-divider">
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item text-danger" href="#">
                                                                    <i class="bi bi-trash text-danger me-2"></i> Borrar
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <th scope="row">1043</th>
                                                <td>AL005 - María López</td>
                                                <td>lector1</td>
                                                <td>2026-04-28</td>
                                                <td>2</td>
                                                <td><span class="badge bg-secondary">Finalizado</span></td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm btn-light border-0" type="button"
                                                            data-bs-toggle="dropdown" aria-expanded="false">
                                                            <i class="bi bi-three-dots-vertical"></i>
                                                        </button>

                                                        <ul class="dropdown-menu dropdown-menu-end">
                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-eye text-info me-2"></i> Ver detalle
                                                                    préstamo
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item" href="#">
                                                                    <i class="bi bi-person-gear text-primary me-2"></i>
                                                                    Cambiar lector
                                                                </a>
                                                            </li>

                                                            <li>
                                                                <hr class="dropdown-divider">
                                                            </li>

                                                            <li>
                                                                <a class="dropdown-item text-danger" href="#">
                                                                    <i class="bi bi-trash text-danger me-2"></i> Borrar
                                                                </a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </main>

        <footer id="footer" class="footer">
            <div class="copyright">
                &copy; Copyright <strong><span>NiceAdmin</span></strong>. All Rights Reserved
            </div>
            <div class="credits">
                Designed by <a href="https://bootstrapmade.com/">BootstrapMade</a>
            </div>
        </footer>

        <a href="#" class="back-to-top d-flex align-items-center justify-content-center">
            <i class="bi bi-arrow-up-short"></i>
        </a>

        <script src="${pageContext.request.contextPath}/assets/vendor/apexcharts/apexcharts.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/chart.js/chart.umd.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/echarts/echarts.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/quill/quill.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/simple-datatables/simple-datatables.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/tinymce/tinymce.min.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/php-email-form/validate.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

    </body>

    </html>