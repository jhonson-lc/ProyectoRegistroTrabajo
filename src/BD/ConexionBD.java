package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static String url = "jdbc:mysql://ul3qplptb9ukfwi7:JYSgnBqbgDIJFT3hzRQy@bgheueonehtydbtoeqca-mysql.services.clever-cloud.com/bgheueonehtydbtoeqca";
    private static String user = "ul3qplptb9ukfwi7";
    private static String pass = "JYSgnBqbgDIJFT3hzRQy";
    
    public static Connection conectar() {
        try {            
            Connection conn = DriverManager.getConnection(url,user,pass);
            return conn;
        } catch (SQLException e) {
            System.out.println("Error en la conexion " + e.getMessage());
        }
        return (null);
    }
}
