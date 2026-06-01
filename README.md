Script para el diseño de la base de datos (DDL).

CREATE DATABASE `db_biblioteca_don_bosco` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE DATABASE `db_biblioteca_don_bosco` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `CD` (
  `id_documento` int NOT NULL,
  `duracion_minutos` int DEFAULT NULL,
  `tipo_contenido` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  CONSTRAINT `cd_ibfk_1` FOREIGN KEY (`id_documento`) REFERENCES `Documento` (`id_documento`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `detalle_prestamo` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_prestamo` int NOT NULL,
  `id_ejemplar` int NOT NULL,
  `fecha_limite` date NOT NULL,
  `fecha_devolucion_real` date DEFAULT NULL,
  `estado_item` enum('Activo','Devuelto') DEFAULT 'Activo',
  `dias_retraso` int DEFAULT '0',
  `monto_mora` decimal(10,2) DEFAULT '0.00',
  `monto_pagado` decimal(10,2) DEFAULT '0.00',
  `estado_pago_mora` enum('Sin Mora','Pendiente','Pagado') DEFAULT 'Sin Mora',
  PRIMARY KEY (`id_detalle`),
  KEY `id_prestamo` (`id_prestamo`),
  KEY `id_ejemplar` (`id_ejemplar`),
  CONSTRAINT `detalle_prestamo_ibfk_1` FOREIGN KEY (`id_prestamo`) REFERENCES `Prestamo` (`id_prestamo`) ON DELETE CASCADE,
  CONSTRAINT `detalle_prestamo_ibfk_2` FOREIGN KEY (`id_ejemplar`) REFERENCES `Ejemplar` (`id_ejemplar`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Devolucion` (
  `id_devolucion` int NOT NULL AUTO_INCREMENT,
  `id_detalle` int NOT NULL,
  `fecha_devolucion` datetime DEFAULT CURRENT_TIMESTAMP,
  `observaciones_estado_fisico` text,
  PRIMARY KEY (`id_devolucion`),
  UNIQUE KEY `id_detalle` (`id_detalle`),
  CONSTRAINT `devolucion_ibfk_1` FOREIGN KEY (`id_detalle`) REFERENCES `detalle_prestamo` (`id_detalle`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Documento` (
  `id_documento` int NOT NULL AUTO_INCREMENT,
  `id_tipo_doc` int NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `autor` varchar(150) NOT NULL,
  `ubicacion_fisica` varchar(100) DEFAULT NULL,
  `codigo_de_barras` varchar(50) DEFAULT NULL,
  `estado` varchar(50) DEFAULT 'Disponible',
  PRIMARY KEY (`id_documento`),
  UNIQUE KEY `codigo_de_barras` (`codigo_de_barras`),
  KEY `id_tipo_doc` (`id_tipo_doc`),
  CONSTRAINT `documento_ibfk_1` FOREIGN KEY (`id_tipo_doc`) REFERENCES `TipoDocumento` (`id_tipo_doc`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Ejemplar` (
  `id_ejemplar` int NOT NULL AUTO_INCREMENT,
  `id_documento` int NOT NULL,
  `codigo_de_barras` varchar(50) NOT NULL,
  `estado` varchar(50) DEFAULT 'Disponible',
  PRIMARY KEY (`id_ejemplar`),
  UNIQUE KEY `codigo_de_barras` (`codigo_de_barras`),
  KEY `id_documento` (`id_documento`),
  CONSTRAINT `ejemplar_ibfk_1` FOREIGN KEY (`id_documento`) REFERENCES `Documento` (`id_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Libro` (
  `id_documento` int NOT NULL,
  `isbn` varchar(30) DEFAULT NULL,
  `editorial` varchar(100) DEFAULT NULL,
  `edicion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  CONSTRAINT `libro_ibfk_1` FOREIGN KEY (`id_documento`) REFERENCES `Documento` (`id_documento`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Mora_Anual` (
  `anio` int NOT NULL,
  `tarifa_diaria` decimal(10,2) NOT NULL,
  PRIMARY KEY (`anio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Prestamo` (
  `id_prestamo` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `fecha_prestamo` date NOT NULL,
  `estado_general` enum('Activo','Parcial','Con Deuda','Finalizado') DEFAULT 'Activo',
  `monto_pagado` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id_prestamo`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `prestamo_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `Usuarios` (`ID_Usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Revista` (
  `id_documento` int NOT NULL,
  `issn` varchar(30) DEFAULT NULL,
  `volumen` varchar(50) DEFAULT NULL,
  `mes_publicacion` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_documento`),
  CONSTRAINT `revista_ibfk_1` FOREIGN KEY (`id_documento`) REFERENCES `Documento` (`id_documento`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `TipoDocumento` (
  `id_tipo_doc` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(50) NOT NULL,
  `Parametro_mora` varchar(50) NOT NULL,
  `valor_mora` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id_tipo_doc`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `TipoUsuario` (
  `id_tipo` int NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) NOT NULL,
  `max_libros_permitidos` int NOT NULL,
  `max_dias_prestamo` int NOT NULL,
  PRIMARY KEY (`id_tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `Usuarios` (
  `ID_Usuario` int NOT NULL AUTO_INCREMENT,
  `id_tipo` int NOT NULL,
  `Nombres` varchar(100) NOT NULL,
  `Apellidos` varchar(100) NOT NULL,
  `carnet_docente_alumno` varchar(20) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `estado_mora` tinyint(1) DEFAULT '0',
  `Estado` varchar(20) DEFAULT 'Activo',
  PRIMARY KEY (`ID_Usuario`),
  UNIQUE KEY `carnet_docente_alumno` (`carnet_docente_alumno`),
  KEY `id_tipo` (`id_tipo`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_tipo`) REFERENCES `TipoUsuario` (`id_tipo`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
