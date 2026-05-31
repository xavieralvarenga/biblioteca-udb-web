<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>

        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="utf-8">
            <meta content="width=device-width, initial-scale=1.0" name="viewport">

            <title>Nuevo Préstamo - Sistema de Mediateca</title>

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
                <button onclick="history.back()" class="btn btn-outline-primary d-flex align-items-center gap-2">
                    <i class="bi bi-arrow-left"></i>
                    <span>Regresar</span>
                </button>
            </header>

            <main id="main" class="main">
                <div class="pagetitle">
                    <h1>Agregar nuevo Préstamo</h1>
                    <nav>
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a
                                    href="${pageContext.request.contextPath}/prestamos?accion=listar">Préstamos</a></li>
                            <li class="breadcrumb-item active">Nuevo préstamo</li>
                        </ol>
                    </nav>
                </div>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="bi bi-exclamation-octagon me-1"></i>
                        <strong>¡No se pudo procesar!</strong> ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <section class="section">
                    <form action="${pageContext.request.contextPath}/Prestamos" method="POST" id="formPrestamo">
                        <input type="hidden" name="accion" value="guardar">

                        <input type="hidden" id="idUsuarioSeleccionado" name="idUsuario" required>

                        <div class="row">
                            <div class="col-lg-12">
                                <div class="card">
                                    <div class="card-body">
                                        <h5 class="card-title">1. Datos del Lector</h5>

                                        <div class="row mb-3 align-items-center">
                                            <label class="col-sm-2 col-form-label fw-bold">Lector Seleccionado:</label>
                                            <div class="col-sm-6">
                                                <input type="text" class="form-control" id="nombreUsuarioMostrado"
                                                    placeholder="Ningún lector seleccionado..." readonly required>
                                            </div>
                                            <div class="col-sm-4">
                                                <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                                    data-bs-target="#modalUsuarios">
                                                    <i class="bi bi-search"></i> Buscar Lector
                                                </button>
                                            </div>
                                        </div>

                                        <hr>

                                        <div class="d-flex justify-content-between align-items-center mb-3">
                                            <h5 class="card-title mb-0">2. Materiales a Prestar (Carrito)</h5>
                                            <button type="button" class="btn btn-success" data-bs-toggle="modal"
                                                data-bs-target="#modalMateriales">
                                                <i class="bi bi-plus-circle"></i> Agregar Material
                                            </button>
                                        </div>

                                        <table class="table table-bordered">
                                            <thead class="table-light">
                                                <tr>
                                                    <th scope="col">Cód. Barras</th>
                                                    <th scope="col">Título</th>
                                                    <th scope="col">Tipo</th>
                                                    <th scope="col" class="text-center">Quitar</th>
                                                </tr>
                                            </thead>
                                            <tbody id="carritoBody">
                                                <tr id="filaVacia">
                                                    <td colspan="4" class="text-center text-muted">El carrito está
                                                        vacío.
                                                        Agrega materiales para continuar.</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                        <div class="col-lg-12">

                                            <div class="card">
                                                <div class="card-body">
                                                    <label for="">Fecha de regreso: </label>
                                                    <input type="date" class="form-control" id="fechaRegreso"
                                                        name="fechaRegreso" required>
                                                </div>
                                            </div>
                                        </div>



                                        <div class="text-end mt-4">
                                            <button type="submit" class="btn btn-lg btn-primary">
                                                <i class="bi bi-save"></i> Guardar Préstamo
                                            </button>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </section>
            </main>

            <div class="modal fade" id="modalUsuarios" tabindex="-1">
                <div class="modal-dialog modal-xl">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Seleccionar Lector</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <table class="table datatable" id="tablaLectores">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Carnet</th>
                                        <th>Nombre Completo</th>
                                        <th>Rol</th>
                                        <th>Mora</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="usuario" items="${listaUsuario}">
                                        <tr>
                                            <td>${usuario.idUsuario}</td>
                                            <td>${usuario.carnetDocenteAlumno}</td>
                                            <td>${usuario.nombres} ${usuario.apellidos}</td>
                                            <td>${usuario.nombreRol}</td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${usuario.estadoMora}">
                                                        <span class="badge bg-danger">Sí</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success">No</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${usuario.estadoMora}">
                                                        <button type="button" class="btn btn-sm btn-outline-secondary"
                                                            disabled title="Bloqueado por mora">
                                                            <i class="bi bi-x-circle"></i> Seleccionar
                                                        </button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <button type="button" class="btn btn-sm btn-outline-success"
                                                            onclick="seleccionarLector(${usuario.idUsuario}, '${usuario.carnetDocenteAlumno} - ${usuario.nombres} ${usuario.apellidos}')">
                                                            <i class="bi bi-check2-circle"></i> Seleccionar
                                                        </button>
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

            <div class="modal fade" id="modalMateriales" tabindex="-1">
                <div class="modal-dialog modal-xl">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Seleccionar Material (Ejemplar)</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <table class="table datatable" id="tablaMateriales">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Cód. Barras</th>
                                        <th>Título</th>
                                        <th>Autor</th>
                                        <th>Tipo</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="ejemplar" items="${listaEjemplares}">
                                        <tr>
                                            <td>${ejemplar.idEjemplar}</td>
                                            <td>${ejemplar.codigoDeBarras}</td>
                                            <td>${ejemplar.tituloDocumento}</td>
                                            <td>${ejemplar.autorDocumento}</td>
                                            <td>${ejemplar.tipoDocumento}</td>
                                            <td>
                                                <button type="button" class="btn btn-sm btn-primary"
                                                    id="btn-agregar-${ejemplar.idEjemplar}"
                                                    onclick="agregarAlCarrito(${ejemplar.idEjemplar}, '${ejemplar.codigoDeBarras}', '${ejemplar.tituloDocumento}', '${ejemplar.tipoDocumento}')">
                                                    <i class="bi bi-cart-plus"></i> Agregar
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>

            <script
                src="${pageContext.request.contextPath}/assets/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
            <script
                src="${pageContext.request.contextPath}/assets/vendor/simple-datatables/simple-datatables.js"></script>
            <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

            <script>
                // 1. Función para Seleccionar al Lector
                function seleccionarLector(idUsuario, nombreMostrar) {
                    document.getElementById('idUsuarioSeleccionado').value = idUsuario;
                    document.getElementById('nombreUsuarioMostrado').value = nombreMostrar;

                    var modalEl = document.getElementById('modalUsuarios');
                    var modalObj = bootstrap.Modal.getInstance(modalEl);
                    modalObj.hide();
                }

                // 2. Función para Agregar al Carrito de Materiales
                function agregarAlCarrito(idEjemplar, codigoBarras, titulo, tipo) {

                    // Validar que no se haya agregado ya
                    if (document.getElementById('fila-mat-' + idEjemplar)) {
                        return; // Evita duplicados
                    }

                    // Quitar el mensaje de "Carrito Vacío"
                    var filaVacia = document.getElementById('filaVacia');
                    if (filaVacia) { filaVacia.remove(); }

                    var tbody = document.getElementById('carritoBody');

                    // Crear una nueva fila
                    var tr = document.createElement('tr');
                    tr.id = 'fila-mat-' + idEjemplar;

                    // SOLUCIÓN: Usamos concatenación clásica (+) para que JSP no borre las variables
                    tr.innerHTML =
                        '<td>' + codigoBarras + '<input type="hidden" name="idsEjemplares" value="' + idEjemplar + '"></td>' +
                        '<td>' + titulo + '</td>' +
                        '<td>' + tipo + '</td>' +
                        '<td class="text-center">' +
                        '<button type="button" class="btn btn-sm btn-danger" onclick="quitarDelCarrito(' + idEjemplar + ')">' +
                        '<i class="bi bi-trash"></i>' +
                        '</button>' +
                        '</td>';

                    // Insertar en la tabla
                    tbody.appendChild(tr);

                    // Cambiar el botón del Modal a "En el carrito" (Gris)
                    var btn = document.getElementById('btn-agregar-' + idEjemplar);
                    if (btn) {
                        btn.classList.remove('btn-primary');
                        btn.classList.add('btn-secondary');
                        btn.disabled = true;
                        btn.innerHTML = '<i class="bi bi-check-circle"></i> En el carrito';
                    }
                }

                // 3. Función para Quitar del Carrito
                function quitarDelCarrito(idEjemplar) {

                    // Borrar la fila
                    document.getElementById('fila-mat-' + idEjemplar).remove();

                    // Si el carrito queda vacío, mostrar mensaje
                    var tbody = document.getElementById('carritoBody');
                    if (tbody.children.length === 0) {
                        tbody.innerHTML = '<tr id="filaVacia"><td colspan="4" class="text-center text-muted">El carrito está vacío. Agrega materiales para continuar.</td></tr>';
                    }

                    // Restaurar el botón del Modal a "Agregar" (Azul)
                    var btn = document.getElementById('btn-agregar-' + idEjemplar);
                    if (btn) {
                        btn.classList.remove('btn-secondary');
                        btn.classList.add('btn-primary');
                        btn.disabled = false;
                        btn.innerHTML = '<i class="bi bi-cart-plus"></i> Agregar';
                    }
                }

                // ==========================================
                // CONFIGURACIÓN DE DATATABLES EN ESPAÑOL
                // ==========================================
                document.addEventListener("DOMContentLoaded", () => {
                    const opcionesTabla = {
                        searchable: true,
                        fixedHeight: false,
                        perPage: 5,
                        labels: {
                            placeholder: "Buscar por nombre, carnet, título...",
                            perPage: "registros por página",
                            noRows: "No se encontraron coincidencias",
                            info: "Mostrando del {start} al {end} de {rows} registros totales"
                        }
                    };

                    const tablaLectores = document.getElementById('tablaLectores');
                    if (tablaLectores) {
                        new simpleDatatables.DataTable(tablaLectores, opcionesTabla);
                    }

                    const tablaMateriales = document.getElementById('tablaMateriales');
                    if (tablaMateriales) {
                        new simpleDatatables.DataTable(tablaMateriales, opcionesTabla);
                    }
                });
                // ==========================================
                // CALCULAR FECHA DE REGRESO (8 DÍAS)
                // ==========================================
                const inputFechaRegreso = document.getElementById('fechaRegreso');

                if (inputFechaRegreso) {
                    // 1. Obtener la fecha actual
                    let fecha = new Date();

                    // 2. Sumarle 8 días
                    fecha.setDate(fecha.getDate() + 8);

                    // 3. Extraer año, mes y día
                    let anio = fecha.getFullYear();
                    // Los meses en JavaScript empiezan en 0, por eso se suma 1. 
                    // padStart(2, '0') asegura que siempre tenga 2 dígitos (ej: '05' en vez de '5')
                    let mes = String(fecha.getMonth() + 1).padStart(2, '0');
                    let dia = String(fecha.getDate()).padStart(2, '0');

                    // 4. Armar el formato YYYY-MM-DD y asignarlo al input
                    inputFechaRegreso.value = anio + '-' + mes + '-' + dia;

                    // Opcional: Bloquear fechas anteriores para que el bibliotecario no ponga una fecha pasada por error
                    let hoy = new Date();
                    let mesHoy = String(hoy.getMonth() + 1).padStart(2, '0');
                    let diaHoy = String(hoy.getDate()).padStart(2, '0');
                    inputFechaRegreso.min = hoy.getFullYear() + '-' + mesHoy + '-' + diaHoy;
                }


                // ==========================================
                // VALIDAR EL FORMULARIO ANTES DE ENVIAR
                // ==========================================
                document.getElementById('formPrestamo').addEventListener('submit', function (evento) {

                    // 1. Revisamos si hay un usuario seleccionado
                    var idUsuario = document.getElementById('idUsuarioSeleccionado').value;
                    if (!idUsuario) {
                        evento.preventDefault(); // Detiene el envío al Servlet
                        alert("Por favor, selecciona un Lector antes de guardar.");
                        return;
                    }

                    // 2. Revisamos si hay al menos un libro en el carrito
                    var carrito = document.getElementById('carritoBody');
                    // Si la fila del "carrito vacío" existe, significa que no hay libros
                    if (document.getElementById('filaVacia')) {
                        evento.preventDefault(); // Detiene el envío al Servlet
                        alert("El carrito está vacío. Por favor agrega al menos un material.");
                        return;
                    }

                    // Si todo está bien, el formulario continuará su viaje normal al Servlet
                });
            </script>


        </body>

        </html>