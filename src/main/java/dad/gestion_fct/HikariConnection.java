package dad.gestion_fct;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HikariConnection {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/gestionfct"); // Cambia "empresa" por el nombre de tu base de datos
        config.setUsername("root"); // Cambia "tu_usuario" por tu usuario de base de datos
        config.setPassword(""); // Cambia "tu_contraseña" por tu contraseña de base de datos
        config.setMaximumPoolSize(10); // Tamaño máximo del pool
        config.setMinimumIdle(2); // Número mínimo de conexiones inactivas
        config.setConnectionTimeout(30000); // Tiempo de espera para obtener una conexión (30 segundos)
        config.setIdleTimeout(600000); // Tiempo máximo de inactividad de una conexión antes de ser eliminada (10 minutos)
        config.setMaxLifetime(1800000); // Vida útil máxima de una conexión en el pool (30 minutos)

        dataSource = new HikariDataSource(config);
    }

    // Método para obtener una conexión del pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Método para cerrar el pool de conexiones
    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }


}
