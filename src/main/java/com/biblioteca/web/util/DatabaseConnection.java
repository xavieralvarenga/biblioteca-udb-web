package com.biblioteca.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos de la biblioteca.
 * Proporciona un método estático para obtener una conexión activa a MySQL.
 *
 * @author TuNombre
 * @version 1.0
 * @since 2026
 */
public class DatabaseConnection {

    /** URL de conexión a la base de datos con configuración de zona horaria y SSL desactivado. */
    private static final String URL = "jdbc:mysql://localhost:3306/db_biblioteca_don_bosco";

    /** Usuario de acceso a la base de datos MySQL. */
    private static final String USER = "root";

    /** Contraseña de acceso a la base de datos MySQL. */
    private static final String PASSWORD = "Eljokernoesunvillano1$";

    /**
     * Constructor privado para evitar la instanciación de esta clase de utilidad.
     */
    private DatabaseConnection() {
        throw new IllegalStateException("Clase de utilidad - no instanciable");
    }

    /**
     * Establece y devuelve una conexión activa a la base de datos.
     *
     * @return Objeto {@link Connection} hacia la base de datos configurada.
     * @throws SQLException Si ocurre un error al intentar establecer la conexión.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Forzamos la carga del driver en el ClassLoader de la aplicación web
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de MySQL en el classpath.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}