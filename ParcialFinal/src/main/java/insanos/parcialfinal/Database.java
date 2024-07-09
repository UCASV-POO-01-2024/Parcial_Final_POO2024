package insanos.parcialfinal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database { //00191223 // Declaracion de la clase que conectara la base de datos,
    private static final String URL = "jdbc:mysql://localhost:3306/bdBancoNlogonia"; //00191223 URL de la base de datos
    private static final String USER = "root"; //00191223el usuario de la base de datos.
    private static final String PASSWORD = "17052003Brandon"; //00191223 contraseña del usuario de la base de datos.

    public static Connection getConnection() throws SQLException { //00191223 Metodo estatico que devuelve una conexión a la base de datos y si falla tira una excepción SQLException.
        return DriverManager.getConnection(URL, USER, PASSWORD); //00191223 Devuelve una conexión a la base de datos
    }
}
