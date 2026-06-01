<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html lang="es">

        <head>
            <meta charset="UTF-8">
            <title>Inventario General - Catálogo Polimórfico</title>
            <link href="${pageContext.request.contextPath}/assets/img/favicon.png" rel="icon">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f8f9fa;
                }

                table {
                    width: 100%;
                    border-collapse: collapse;
                    margin-top: 20px;
                    background: white;
                }

                th,
                td {
                    border: 1px solid #dee2e6;
                    padding: 12px;
                    text-align: left;
                }

                th {
                    background-color: #007bff;
                    color: white;
                }

                .card {
                    background: white;
                    padding: 20px;
                    border-radius: 5px;
                    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
                    margin-bottom: 25px;
                }

                .btn-add {
                    background-color: #28a745;
                    color: white;
                    padding: 8px 15px;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-weight: bold;
                }

                .btn-update {
                    background-color: #ffc107;
                    color: black;
                    padding: 8px 15px;
                    border: none;
                    border-radius: 4px;
                    cursor: pointer;
                    font-weight: bold;
                }

                .btn-menu {
                    display: inline-block;
                    background-color: #6c757d;
                    color: white;
                    padding: 8px 15px;
                    text-decoration: none;
                    border-radius: 4px;
                    font-weight: bold;
                    margin-bottom: 15px;
                }

                .form-grid {
                    display: grid;
                    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                    gap: 12px;
                    margin-bottom: 15px;
                }

                .form-grid input,
                .form-grid select {
                    padding: 8px;
                    border: 1px solid #ced4da;
                    border-radius: 4px;
                    width: 100%;
                    box-sizing: border-box;
                }

                .seccion-especifica {
                    display: none;
                    grid-column: span 3;
                    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
                    gap: 12px;
                }

                .badge {
                    padding: 5px 10px;
                    border-radius: 4px;
                    color: white;
                    font-size: 12px;
                    font-weight: bold;
                }

                .badge-admin {
                    background-color: #dc3545;
                }

                .badge-user {
                    background-color: #17a2b8;
                }

                .badge-activo {
                    background-color: #28a745;
                    color: white;
                }

                .badge-inactivo {
                    background-color: #6c757d;
                    color: white;
                }

                .link-op {
                    font-weight: bold;
                    text-decoration: none;
                    margin: 0 4px;
                }
            </style>

            <%-- Bloque JS protegido de forma segura --%>
                <c:if test="${sessionScope.usuarioLogueado.nombreRol eq 'Administrador'}">
                    <script type="text/javascript">
                        function cambiarFormulario() {
                            var tipo = document.getElementById("selectorTipo").value;
                            document.getElementById("camposLibro").style.display = (tipo == "1") ? "grid" : "none";
                            document.getElementById("camposRevista").style.display = (tipo == "2") ? "grid" : "none";
                            document.getElementById("camposCD").style.display = (tipo == "3") ? "grid" : "none";

                            configurarRequeridos("camposLibro", tipo == "1");
                            configurarRequeridos("camposRevista", tipo == "2");
                            configurarRequeridos("camposCD", tipo == "3");
                        }

                        function configurarRequeridos(idContenedor, activar) {
                            var idC = document.getElementById(idContenedor);
                            if (idC) {
                                var inputs = idC.getElementsByTagName("input");
                                for (var i = 0; i < inputs.length; i++) {
                                    if (activar) inputs[i].setAttribute("required", "required");
                                    else inputs[i].removeAttribute("required");
                                }
                            }
                        }
                        window.onload = function () { cambiarFormulario(); };
                    </script>
                </c:if>
        </head>

        <body>

            <div
                style="background-color: #e9ecef; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; border-radius: 4px;">
                <span>
                    Usuario Activo: <strong>
                        <c:out value="${sessionScope.usuarioLogueado.nombres}" />
                    </strong>
                    | Rol:
                    <c:choose>
                        <c:when test="${sessionScope.usuarioLogueado.nombreRol eq 'Administrador'}">
                            <span class="badge badge-admin">Administrador (CRUD Total)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-user">
                                <c:out value="${sessionScope.usuarioLogueado.nombreRol}" /> (Solo Lectura)
                            </span>
                        </c:otherwise>
                    </c:choose>
                </span>
                <a href="${pageContext.request.contextPath}/login?accion=logout"
                    style="color: #dc3545; font-weight: bold; text-decoration: none;">Cerrar Sesión</a>
            </div>

            <a href="${pageContext.request.contextPath}/menu" class="btn-menu">← Volver al Menú Principal</a>

            <h2>Catálogo Unificado de Documentos</h2>

            <c:if test="${sessionScope.usuarioLogueado.nombreRol eq 'Administrador'}">
                <div class="card">
                    <h3>
                        <c:out
                            value="${not empty docEditar ? 'Modificar Registro Existente' : 'Añadir Nuevo Recurso al Inventario'}" />
                    </h3>
                    <form
                        action="${pageContext.request.contextPath}/documentos?accion=${not empty docEditar ? 'actualizar' : 'insertar'}"
                        method="POST">
                        <input type="hidden" name="idDocumento" value="${docEditar.idDocumento}">

                        <div class="form-grid">
                            <div>
                                <label>Tipo Documento:</label>
                                <select name="idTipoDoc" id="selectorTipo" onchange="cambiarFormulario()" ${not empty
                                    docEditar ? 'disabled' : '' }>
                                    <option value="1" ${docEditar.idTipoDoc==1 ? 'selected' : '' }>Libro</option>
                                    <option value="2" ${docEditar.idTipoDoc==2 ? 'selected' : '' }>Revista</option>
                                    <option value="3" ${docEditar.idTipoDoc==3 ? 'selected' : '' }>CD Multimedia
                                    </option>
                                </select>
                                <c:if test="${not empty docEditar}">
                                    <input type="hidden" name="idTipoDoc" value="${docEditar.idTipoDoc}">
                                </c:if>
                            </div>
                            <div><label>Título:</label><input type="text" name="titulo"
                                    value="<c:out value='${docEditar.titulo}'/>" required></div>
                            <div><label>Autor / Creador:</label><input type="text" name="autor"
                                    value="<c:out value='${docEditar.autor}'/>" required></div>
                            <div><label>Ubicación Física:</label><input type="text" name="ubicacionFisica"
                                    value="<c:out value='${docEditar.ubicacionFisica}'/>" required></div>
                            <div><label>Código de Barras:</label><input type="text" name="codigoDeBarras"
                                    value="<c:out value='${docEditar.codigoDeBarras}'/>" required></div>
                            <div>
                                <label>Estado del Documento:</label>
                                <select name="estado">
                                    <option value="Activo" ${docEditar.estado=='Activo' ? 'selected' : '' }>Activo
                                    </option>
                                    <option value="Inactivo" ${docEditar.estado=='Inactivo' ? 'selected' : '' }>Inactivo
                                    </option>
                                </select>
                            </div>
                        </div>

                        <div class="form-grid">
                            <div id="camposLibro" class="seccion-especifica">
                                <input type="text" name="isbn" placeholder="ISBN"
                                    value="${docEditar.idTipoDoc == 1 ? docEditar.isbn : ''}">
                                <input type="text" name="editorial" placeholder="Editorial"
                                    value="${docEditar.idTipoDoc == 1 ? docEditar.editorial : ''}">
                                <input type="text" name="edicion" placeholder="Edición"
                                    value="${docEditar.idTipoDoc == 1 ? docEditar.edicion : ''}">
                            </div>

                            <div id="camposRevista" class="seccion-especifica">
                                <input type="text" name="issn" placeholder="ISSN"
                                    value="${docEditar.idTipoDoc == 2 ? docEditar.issn : ''}">
                                <input type="text" name="volumen" placeholder="Volumen"
                                    value="${docEditar.idTipoDoc == 2 ? docEditar.volumen : ''}">
                                <input type="text" name="mesPublicacion" placeholder="Mes de Publicación"
                                    value="${docEditar.idTipoDoc == 2 ? docEditar.mesPublicacion : ''}">
                            </div>

                            <div id="camposCD" class="seccion-especifica">
                                <input type="number" name="duracionMinutos" placeholder="Duración (Minutos)"
                                    value="${docEditar.idTipoDoc == 3 ? docEditar.duracionMinutos : ''}">
                                <input type="text" name="tipoContenido" placeholder="Tipo de Contenido"
                                    value="${docEditar.idTipoDoc == 3 ? docEditar.tipoContenido : ''}">
                            </div>
                        </div>

                        <button type="submit" class="${not empty docEditar ? 'btn-update' : 'btn-add'}">
                            ${not empty docEditar ? 'Guardar Cambios' : 'Registrar Documento'}
                        </button>
                        <c:if test="${not empty docEditar}">
                            <a href="${pageContext.request.contextPath}/documentos"
                                style="margin-left: 10px; color: #6c757d; text-decoration: none;">Cancelar Edición</a>
                        </c:if>
                    </form>
                </div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-octagon me-1"></i>
                    <strong>¡Atención!</strong>
                    <c:choose>
                        <c:when test="${param.error == 'FalloPersistencia'}">
                            No se pudo registrar el documento. Es muy probable que el Código de Barras, ISBN o ISSN ya
                            estén registrados en otro material.
                        </c:when>
                        <c:when test="${param.error == 'FalloActualizacion'}">
                            No se pudieron guardar los cambios del documento. Verifica que los datos no estén
                            duplicados.
                        </c:when>
                        <c:when test="${param.error == 'AccesoDenegado'}">
                            No tienes los permisos necesarios para realizar esta acción.
                        </c:when>
                        <c:when test="${param.error == 'DatosInvalidos'}">
                            Los datos enviados no tienen un formato válido. Revisa que los números y textos sean
                            correctos.
                        </c:when>
                        <c:otherwise>
                            Ocurrió un problema en el sistema: ${param.error}
                        </c:otherwise>
                    </c:choose>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>

                <script>
                    if (window.history.replaceState) {
                        let urlParams = new URLSearchParams(window.location.search);
                        urlParams.delete('error');
                        let nuevaUrl = window.location.pathname + (urlParams.toString() ? "?" + urlParams.toString() : "");
                        window.history.replaceState(null, null, nuevaUrl);
                    }
                </script>
            </c:if>

            <c:if test="${not empty param.msg}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle me-1"></i>
                    <c:choose>
                        <c:when test="${param.msg == 'InsercionExitosa'}">¡El documento se registró correctamente en el
                            catálogo!</c:when>
                        <c:when test="${param.msg == 'ActualizacionExitosa'}">¡Los datos del documento se actualizaron
                            con éxito!</c:when>
                        <c:when test="${param.msg == 'EliminacionExitosa'}">¡El documento fue eliminado del sistema!
                        </c:when>
                        <c:otherwise>¡Operación realizada con éxito!</c:otherwise>
                    </c:choose>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>

                <script>
                    if (window.history.replaceState) {
                        let urlParams = new URLSearchParams(window.location.search);
                        urlParams.delete('msg');
                        let nuevaUrl = window.location.pathname + (urlParams.toString() ? "?" + urlParams.toString() : "");
                        window.history.replaceState(null, null, nuevaUrl);
                    }
                </script>
            </c:if>

            <h3>Existencias Disponibles</h3>

            <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                <input type="text" id="buscador" onkeyup="filtrarTabla()"
                    placeholder="Buscar por título, autor, código..."
                    style="flex: 1; padding: 12px; border: 1px solid #ced4da; border-radius: 4px; box-sizing: border-box; font-size: 16px;">

                <select id="filtroTipo" onchange="filtrarTabla()"
                    style="padding: 12px; border: 1px solid #ced4da; border-radius: 4px; font-size: 16px; background-color: white; min-width: 200px;">
                    <option value="">Todos los materiales</option>
                    <option value="LIBRO">Solo Libros</option>
                    <option value="REVISTA">Solo Revistas</option>
                    <option value="CD">Solo CDs Multimedia</option>
                </select>
            </div>

            <table id="tablaDocumentos">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Tipo de Documento</th>
                        <th>Título</th>
                        <th>Autor</th>
                        <th>Estado</th>
                        <th>Propiedades Específicas</th>
                        <c:if test="${sessionScope.usuarioLogueado.nombreRol eq 'Administrador'}">
                            <th>Operaciones</th>
                        </c:if>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="doc" items="${documentos}">
                        <tr>
                            <td>
                                <c:out value="${doc.idDocumento}" />
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${doc.idTipoDoc == 1}"><span
                                            style="color:blue; font-weight:bold;">LIBRO</span></c:when>
                                    <c:when test="${doc.idTipoDoc == 2}"><span
                                            style="color:red; font-weight:bold;">REVISTA</span></c:when>
                                    <c:otherwise><span style="color:purple; font-weight:bold;">CD</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><strong>
                                    <c:out value="${doc.titulo}" />
                                </strong></td>
                            <td>
                                <c:out value="${doc.autor}" />
                            </td>
                            <td>
                                <%-- Visualización de estados optimizada estéticamente con Badges --%>
                                    <c:choose>
                                        <c:when test="${doc.estado eq 'Activo'}">
                                            <span class="badge badge-activo">Activo</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-inactivo">Inactivo</span>
                                        </c:otherwise>
                                    </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${doc.idTipoDoc == 1}">
                                        <strong>ISBN:</strong>
                                        <c:out value="${doc.isbn}" /> |
                                        <strong>Editorial:</strong>
                                        <c:out value="${doc.editorial}" /> |
                                        <strong>Edición:</strong>
                                        <c:out value="${doc.edicion}" />
                                    </c:when>
                                    <c:when test="${doc.idTipoDoc == 2}">
                                        <strong>ISSN:</strong>
                                        <c:out value="${doc.issn}" /> |
                                        <strong>Volumen:</strong>
                                        <c:out value="${doc.volumen}" /> |
                                        <strong>Mes:</strong>
                                        <c:out value="${doc.mesPublicacion}" />
                                    </c:when>
                                    <c:otherwise>
                                        <strong>Duración:</strong>
                                        <c:out value="${doc.duracionMinutos}" /> min |
                                        <strong>Contenido:</strong>
                                        <c:out value="${doc.tipoContenido}" />
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <c:if test="${sessionScope.usuarioLogueado.nombreRol eq 'Administrador'}">
                                <td>
                                    <a href="${pageContext.request.contextPath}/ejemplares?idDocumento=${doc.idDocumento}"
                                        class="link-op" style="color: #20c997;">Ejemplares</a> |
                                    <a href="${pageContext.request.contextPath}/documentos?accion=cargarEditar&id=${doc.idDocumento}"
                                        class="link-op" style="color: #ffc107;">Editar</a> |
                                    <a href="${pageContext.request.contextPath}/documentos?accion=eliminar&id=${doc.idDocumento}"
                                        onclick="return confirm('¿Eliminar de forma permanente este recurso?');"
                                        class="link-op" style="color:#dc3545;">Eliminar</a>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <script type="text/javascript">
                function filtrarTabla() {
                    var inputBuscador = document.getElementById("buscador").value.toUpperCase();
                    var filtroTipo = document.getElementById("filtroTipo").value.toUpperCase();

                    var tabla = document.getElementById("tablaDocumentos");
                    var tr = tabla.getElementsByTagName("tr");

                    for (var i = 1; i < tr.length; i++) {
                        var coincideTexto = false;
                        var coincideTipo = false;

                        var tds = tr[i].getElementsByTagName("td");

                        if (tds.length > 0) {
                            var textoTipo = tds[1].textContent || tds[1].innerText;
                            if (filtroTipo === "" || textoTipo.toUpperCase().indexOf(filtroTipo) > -1) {
                                coincideTipo = true;
                            }

                            for (var j = 0; j < tds.length; j++) {
                                if (tds[j]) {
                                    var textoColumna = tds[j].textContent || tds[j].innerText;
                                    if (textoColumna.toUpperCase().indexOf(inputBuscador) > -1) {
                                        coincideTexto = true;
                                        break;
                                    }
                                }
                            }
                            if (coincideTexto && coincideTipo) {
                                tr[i].style.display = "";
                            } else {
                                tr[i].style.display = "none";
                            }
                        }
                    }
                }
            </script>
        </body>

        </html>